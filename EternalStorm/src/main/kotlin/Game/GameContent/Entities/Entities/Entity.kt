package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Entities.EntityRender.HpRender
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.EntityComponents.GravityComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.Game.GameSystems.EntityComponents.RigidBody
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsApi
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class Entity(var entityType: AbstractEntityType, var gameCycle: GameCycle) {

    val gameController: GameController get() = gameCycle.gameController
    val coreController: CoreController get() = gameController.coreController
    val entityApi: EntityApi get() = gameCycle.entityApi
    val mapApi: MapApi = gameCycle.mapApi
    val vfxObjectsApi: VfxObjectsApi = gameCycle.vfxObjectsApi

    // ─── State ───────────────────────────────────────────────────────────────

    var position = 0 v 0
    var size = 1 v 1
    var systemId = 0L
    var facing = 1

    // ─── Physics components ──────────────────────────────────────────────────

    var rigidBody: RigidBody? = RigidBody(this)
    var gravityComponent: GravityComponent? = GravityComponent(this, rigidBody!!, -0.035f)
    var hpBody: HpBody? = HpBody(this)

    // ─── Hitboxes ─────────────────────────────────────────────────────────────

    var collisionDetector: CollisionDetector? = null
    val hitboxes = LinkedHashMap<String, HitboxComponent>()

    var physicTicks = 0L
    var logicalTicks = 0L
    var renderFrames = 0L

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

    // ─── Render components ───────────────────────────────────────────────────

    fun getEntityRenderContainer(): LayersRenderContainer<RenderLayers.Main> =
        gameController.gameRender.getEntityContainer()

    open var renderEntity: RenderObjectInterface<RenderLayers.Main>? =
        BaseRenderEntity(getEntityRenderContainer())

    open var hpRender: HpRender? = null

    init {
        if (hpBody != null) hpRender = HpRender(getEntityRenderContainer(), hpBody!!, 0 v -1)
    }

    // ─── Lifecycle ───────────────────────────────────────────────────────────

    open fun spawn() {
        hpBody?.let { hp ->
            hp.maxHp = entityType.baseHp
            hp.fullHp()
        }
        size = entityType.baseSize.copy()
        createBaseHitbox()
        createDownTrigger()
        createCustomHitboxes()
    }

    var hasCollisionDetector = true

    fun createBaseHitbox() {
        addHitbox("main").size = size.copy()
        if (hasCollisionDetector) {
            createBaseCollisionDetector()
        }
    }

    var hasDownTrigger = true
    open fun createDownTrigger() {
        if (hasDownTrigger) {
            val new = addHitbox("down trigger", HitboxTypes.ONLY_TRIGGER)
            new.size = size.x*0.95f v 0.05f
            new.delta.y = -size.y/2f-0.025f
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
        hitboxes.values.forEach { it.resetBlockCollision() }
        gravityComponent?.useGravity()
        updateHitboxes()
        updateMoving()
    }

    open fun updateMoving(updateDetector: Boolean = false) {
        val rb = rigidBody ?: return
        rb.useSpeed()
        rb.useFriction()
        if (updateDetector) collisionDetector?.update()
        do {
            rb.deltaStep()
            updateHitboxes()
            if (updateDetector) collisionDetector?.update()
        } while (rb.containsSteps())
    }

    open fun updateHitboxes() {
        hitboxes.values.forEach { it.hitboxUpdate() }
    }
    open fun updateRenderHitboxes() {
        hitboxes.values.forEach { it.renderUpdate() }
    }

    open fun logicalUpdate() {
        logicalTicks++
    }

    open fun renderUpdate() {
        renderFrames++
        renderEntity?.let {
            it.ROI_pos = position.copy()
            it.ROI_size = size.copy()
        }
        hpRender?.update()
        updateRenderHitboxes()
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
        hitboxes.values.forEach { it.hitboxRender.show() }
    }

    open fun hide() {
        visualHide()
        hitboxes.values.forEach { it.hitboxRender.hide() }
    }

    var isDead = false

    open fun kill() {
        isDead = true
        entityApi.hideEntity(this)
    }

    fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent) : Boolean {
        entityApi.entityDamage(this, damage)
        return true
    }

    open fun knockback(force: Vec2) {
        rigidBody?.addForce(force)
    }
}