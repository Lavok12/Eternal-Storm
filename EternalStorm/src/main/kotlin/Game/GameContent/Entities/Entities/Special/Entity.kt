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
import la.vok.Game.GameSystems.EntityComponents.BuffController
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.*
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.EntityParticle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

@Suppress("UNCHECKED_CAST")
open class Entity(var entityType: AbstractEntityType, var gameCycle: GameCycle) {
    var dimension: AbstractDimension = gameCycle.dimensionsApi.getMainDimension()

    // ─── Component System ───────────────────────────────────────────────────

    @PublishedApi
    internal val components = mutableListOf<IEntityComponent>()

    fun addComponent(component: IEntityComponent): Entity {
        components.add(component)
        component.onAttach(this)
        return this
    }

    inline fun <reified T : IEntityComponent> getComponent(): T? {
        return components.find { it is T } as? T
    }

    inline fun <reified T : IEntityComponent> removeComponent() {
        components.removeAll { it is T }
    }

    fun sendEvent(event: EntityEvent) {
        components.forEach { it.onEvent(event) }
    }

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

    open var baseBackResistance = 0f
    open var bodyDamage = 10
    open var bodyKnockBack = 0.12f

    open fun changeFacing(newFacing: Int) {
        facing = newFacing
    }

    // ─── Render ──────────────────────────────────────────────────────────────

    fun getRenderLayer(): LayersRenderContainer =
        gameController.gameRender.renderLayer

    open var renderEntity: RenderObjectInterface? = BaseRenderEntity(getRenderLayer())
    open var hpRender: HpRender? = null

    // ─── Legacy Components (Soon to be migrated) ──────────────────────────

    open var ai: AbstractAI? = null
    open var inventory: MobInventory? = null

    // ─── Core Components (Stored as components, accessible via properties for compatibility) ──────────────────

    var rigidBody: RigidBody? = null
        get() = getComponent<PhysicsComponent>()?.rigidBody ?: field
    
    var gravityComponent: GravityComponent? = null
        get() = getComponent<GravityComponent>() ?: field
    
    var hpBody: HpBody? = null
        get() = getComponent<HpBody>() ?: field

    var buffController: BuffController = BuffController(this)

    init {
        // Initialize legacy components as new modular components
        val rb = RigidBody(this)
        addComponent(PhysicsComponent(this, rb))
        addComponent(GravityComponent(this, rb, -0.035f))
        addComponent(HpBody(this))
        
        // hpRender is still legacy for now as it's tied to rendering layers deeply
        if (getComponent<HpBody>() != null) {
            hpRender = HpRender(getRenderLayer(), getComponent<HpBody>()!!, 0 v -1)
        }
    }

    open fun useBuffs() {}
    
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
    ): HitboxComponent {
        val oldHitbox = hitboxes[name]
        if (AppState.hitboxDebug) {
            oldHitbox?.hitboxRender?.hide()
        }

        val newHitbox = HitboxComponent(type, this, rigidBody)
        hitboxes[name] = newHitbox
        newHitbox.initRender()

        if (AppState.hitboxDebug) {
            newHitbox.hitboxRender?.show()
        }

        return newHitbox
    }

    fun isHitboxBlockCollision(name: String): Boolean =
        hitboxes[name]?.blocksCollision == true

    open fun isAnyPhysicBlockCollision(): Boolean =
        hitboxes.values.any { it.hitboxType == HitboxTypes.COLLISION && it.blocksCollision } || downTrigger?.blocksCollision ?: false


    // ─── Spawn / Setup ───────────────────────────────────────────────────────
    
    open fun spawn() {
        hpBody?.let { hp ->
            hp.rawMaxHp = entityType.baseHp
            hp.fullHp()
        }
        size = entityType.baseSize.copy()
        createBaseHitbox()
        createDownTrigger()
        createCustomHitboxes()
        ai?.spawn()

        components.forEach { it.onSpawn() }
        sendEvent(EntityEvent.Spawned)
    }

    open fun createBaseHitbox() {
        addHitbox("main").size = size.copy()
        if (hasCollisionDetector) {
            createBaseCollisionDetector()
            getComponent<PhysicsComponent>()?.hasCollisionDetector = true
        } else {
            getComponent<PhysicsComponent>()?.hasCollisionDetector = false
        }
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
        
        // Most logic now in components!
        components.forEach { it.onPhysicUpdate() }
    }

    open fun logicalUpdate() {
        logicalTicks++
        inventory?.logicalUpdate()
        
        components.forEach { it.onLogicalUpdate() }
    }

    open fun renderUpdate() {
        renderFrames++
        inventory?.renderUpdate()
        renderEntity?.let {
            it.ROI_pos = position.copy()
            it.ROI_size = size.copy()
        }
        hpRender?.update()
        if (AppState.hitboxDebug) updateRenderHitboxes()
        
        components.forEach { it.onRenderUpdate() }
    }

    /**
     * Legacy method for manual movement, now mostly handled by PhysicsComponent.
     */
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
        hitboxes.values.forEach { it.resetBlockCollision() }
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
        if (AppState.hitboxDebug) {hitboxes.values.forEach { it.hitboxRender?.show() }}
    }

    open fun hide() {
        visualHide()
        hitboxes.values.forEach { it.hitboxRender?.hide() }
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
        sendEvent(EntityEvent.Damage(damage, hitboxComponent))
        gameCycle.entityApi.absoluteDamage(dimension!!, this, damage)
        return true
    }

    open fun damage(damage: DamageData) {
        ai?.damage(damage)
    }

    open fun knockback(force: Vec2) {
        rigidBody?.addForce(force * (1f - baseBackResistance) * (1f - buffController.knockbackResistance))
    }

    // ─── Death ───────────────────────────────────────────────────────────────

    open fun spawnDieParticles() {
        if (entityType.imgPreview == "") return
        repeat(3) {
            var randomDir = Vec2(AppState.main.random(-1f, 1f), AppState.main.random(-1f, 1f)).normalize()
            gameCycle.particlesApi.addParticle(
                dimension!!,
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
        sendEvent(EntityEvent.Death)
        spawnDieParticles()
        ai?.die()
        isDead = true
        drop()
        gameCycle.entityApi.hideEntity(dimension!!, this)
    }

    open fun drop() {
        gameCycle.itemsApi.spawnDropTable(dimension!!, entityType.drop, position, true)
    }
}