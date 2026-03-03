package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Ai.AbstractAI
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Entities.EntityRender.HpRender
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.GravityComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.EntityComponents.MobInventory
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.Game.GameSystems.EntityComponents.RigidBody
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.EntityParticle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import java.util.Vector

@Suppress("UNCHECKED_CAST")
open class Entity(var entityType: AbstractEntityType, var gameCycle: GameCycle) {

    // ─── References ──────────────────────────────────────────────────────────

    val gameController: GameController get() = gameCycle.gameController
    val coreController: CoreController get() = gameController.coreController

    // ─── State ───────────────────────────────────────────────────────────────

    var position = Vec2.ZERO
    var size = 1 v 1
    var systemId = 0L
    var facing = 1
    var invulnerabilityTicks: Int = 0
    var isDead = false

    var physicTicks = 0L
    var logicalTicks = 0L
    var renderFrames = 0L

    open var knockBackResistanse = 0f
    open var bodyDamage = 10
    open var bodyKnockBack = 0.12f

    open fun changeFacing(newFacing: Int) {
        facing = newFacing
    }

    // ─── Components ──────────────────────────────────────────────────────────

    open var ai: AbstractAI? = null
    open var inventory: MobInventory? = null

    var rigidBody: RigidBody? = RigidBody(this)
    var gravityComponent: GravityComponent? = GravityComponent(this, rigidBody!!, -0.035f)
    var hpBody: HpBody? = HpBody(this)

    // ─── Hitboxes ────────────────────────────────────────────────────────────

    var collisionDetector: CollisionDetector? = null
    val hitboxes = LinkedHashMap<String, HitboxComponent>()
    var hasCollisionDetector = true
    var hasDownTrigger = true

    var mainHitbox: HitboxComponent?
        get() = hitboxes["main"]
        set(value) {
            if (value != null) hitboxes["main"] = value
            else hitboxes.remove("main")
        }

    var downTrigger: HitboxComponent?
        get() = hitboxes["down trigger"]
        set(value) {
            if (value != null) hitboxes["down trigger"] = value
            else hitboxes.remove("down trigger")
        }

    fun addHitbox(
        name: String,
        type: HitboxTypes = HitboxTypes.COLLISION,
        rigidBody: RigidBody? = this.rigidBody
    ): HitboxComponent = HitboxComponent(type, this, rigidBody).also { hitboxes[name] = it }

    fun isHitboxBlockCollision(name: String): Boolean =
        hitboxes[name]?.blocksCollision == true

    fun isAnyPhysicBlockCollision(): Boolean =
        hitboxes.values.any { it.hitboxType == HitboxTypes.COLLISION && it.blocksCollision }

    // ─── Render ──────────────────────────────────────────────────────────────

    fun getRenderLayer(): LayersRenderContainer =
        gameController.gameRender.renderLayer

    open var renderEntity: RenderObjectInterface? = BaseRenderEntity(getRenderLayer())
    open var hpRender: HpRender? = null

    init {
        if (hpBody != null) hpRender = HpRender(getRenderLayer(), hpBody!!, 0 v -1)
    }

    // ─── Spawn / Setup ───────────────────────────────────────────────────────

    open fun spawn() {
        hpBody?.let { hp ->
            hp.maxHp = entityType.baseHp
            hp.fullHp()
        }
        size = entityType.baseSize.copy()
        createBaseHitbox()
        createDownTrigger()
        createCustomHitboxes()
        ai?.spawn()
    }

    fun createBaseHitbox() {
        addHitbox("main").size = size.copy()
        if (hasCollisionDetector) createBaseCollisionDetector()
    }

    open fun createDownTrigger() {
        if (hasDownTrigger) {
            val new = addHitbox("down trigger", HitboxTypes.ONLY_TRIGGER)
            new.size = size.x * 0.9f v 0.05f
            new.delta.y = -size.y / 2f - 0.025f
            new.ignoreCollision = true
        }
    }

    open fun createBaseCollisionDetector() {
        collisionDetector = CollisionDetector(this, mainHitbox!!, gameCycle)
    }

    open fun createCustomHitboxes() {}

    // ─── Updates ─────────────────────────────────────────────────────────────

    open fun physicUpdate() {
        physicTicks++
        if (invulnerabilityTicks > 0) invulnerabilityTicks--
        ai?.physicUpdate()
        inventory?.physicUpdate()
        hitboxes.values.forEach { it.resetBlockCollision() }
        gravityComponent?.useGravity()
        updateHitboxes()
        updateMoving(hasCollisionDetector)
    }

    open fun logicalUpdate() {
        logicalTicks++
        inventory?.logicalUpdate()
    }

    open fun renderUpdate() {
        renderFrames++
        inventory?.renderUpdate()
        renderEntity?.let {
            it.ROI_pos = position.copy()
            it.ROI_size = size.copy()
        }
        hpRender?.update()
        updateRenderHitboxes()
    }

    open fun updateMoving(updateDetector: Boolean = false) {
        val rb = rigidBody ?: return
        rb.useSpeed()
        rb.useFriction()
        if (updateDetector) collisionDetector?.update()
        do { moveStep(updateDetector) } while (rb.containsSteps())
    }

    open fun moveStep(updateDetector: Boolean = false) {
        rigidBody!!.deltaStep()
        updateHitboxes()
        if (updateDetector) collisionDetector?.update()
    }

    open fun updateHitboxes() {
        hitboxes.values.forEach { it.hitboxUpdate() }
    }

    open fun updateRenderHitboxes() {
        hitboxes.values.forEach { it.renderUpdate() }
    }

    // ─── Visibility ──────────────────────────────────────────────────────────

    open fun visualShow() {
        renderEntity?.show()
        hpRender?.show()
    }

    open fun visualHide() {
        renderEntity?.hide()
        hpRender?.hide()
    }

    open fun show() {
        visualShow()
        if (AppState.hitboxDebug) hitboxes.values.forEach { it.hitboxRender.show() }
    }

    open fun hide() {
        visualHide()
        if (AppState.hitboxDebug) hitboxes.values.forEach { it.hitboxRender.hide() }
    }

    // ─── Damage / Combat ─────────────────────────────────────────────────────

    open fun bodyDamage(hitboxComponent: HitboxComponent) {
        val damageFacing = if (position.x < hitboxComponent.entity.position.x) 1 else -1
        gameCycle.entityApi.logicalDamage(
            hitboxComponent.entity,
            DamageData(bodyDamage, (damageFacing v 1f) * bodyKnockBack, systemId, null),
            hitboxComponent
        )
    }

    open fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean {
        if (isDead) return false
        if (invulnerabilityTicks > 0) return false
        gameCycle.entityApi.absoluteDamage(this, damage)
        return true
    }

    open fun damage(damage: DamageData) {
        ai?.damage(damage)
    }

    open fun knockback(force: Vec2) {
        rigidBody?.addForce(force * (1f - knockBackResistanse))
    }

    // ─── Death ───────────────────────────────────────────────────────────────

    open fun spawnDieParticles() {
        if (entityType.imgPreview == "") return
        repeat(3) {
            var randomDir = Vec2(AppState.main.random(-1f, 1f), AppState.main.random(-1f, 1f)).normalize()
            gameCycle.particleController.particleSystem.addParticle(
                EntityParticle(
                    gameCycle,
                    coreController.spriteLoader.getValue(entityType.imgPreview),
                    position + randomDir * size / 2f,
                    randomDir * AppState.main.random(0.02f, 0.034f),
                    size / 1.5f
                )
            )
        }
    }

    open fun die() {
        spawnDieParticles()
        ai?.die()
        isDead = true
        drop()
        gameCycle.entityApi.hideEntity(this)
    }

    open fun drop() {
        gameCycle.itemsApi.spawnDropTable(entityType.drop, position, true)
    }
}