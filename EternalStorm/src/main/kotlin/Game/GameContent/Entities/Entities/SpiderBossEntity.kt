package la.vok.Game.GameContent.Entities.Entities

import la.vok.Game.GameContent.Entities.Ai.SpiderAi
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.SpiderBossRender
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.GravityComponent
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.Game.GameSystems.EntityComponents.PhysicsComponent
import la.vok.Game.GameSystems.EntityComponents.LiquidDetectorComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpiderBossEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    
    val legs = mutableListOf<SpiderLegEntity>()
    val maxLegs = 6

    override fun knockback(force: Vec2) {
        super.knockback(force*0.2f)
    }
    
    override var renderEntity: la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface? =
        SpiderBossRender(getRenderLayer(), this)

    init {
        removeComponent<GravityComponent>()
        rigidBody?.friction = 0.05f
        hasCollisionDetector = true 
        addComponent(LiquidDetectorComponent(this))
    }

    override fun spawn() {
        super.spawn()
        setupLegs()
        this.ai = SpiderAi(this, gameCycle)
    }

    private fun setupLegs() {
        for (i in 0 until maxLegs) {
            val leg = gameCycle.entityApi.getRegisteredEntity(la.vok.Game.GameContent.ContentList.EntitiesList.spider_leg) as SpiderLegEntity
            leg.parentBody = this
            leg.legIndex = i // Track index for sector logic
            
            val angle = (i.toFloat() / maxLegs) * PI.toFloat() * 2f
            leg.shoulderOffset = Vec2(cos(angle), sin(angle)) * (size.x * 0.4f) 
            
            legs.add(leg)
            val spawnPos = position + leg.shoulderOffset
            gameCycle.entityApi.spawnEntity(dimension!!, leg, spawnPos)
            leg.anchorPoint = spawnPos.copy()
            leg.visualAnchorPoint = spawnPos.copy()
            leg.moveTo(spawnPos)
        }
    }

    override fun physicUpdate() {
        super.physicUpdate()
        
        val attachedLegs = legs.filter { it.isAttached && !it.isDead }
        val onGroundCount = attachedLegs.count { it.isAttachedOnTile }

        if (attachedLegs.size < 3) {
            die()
            return
        }

        // 1. Move Body toward AI Target (Unconditional Drive)
        val target = ai?.targetWorldPos() ?: position
        val diff = target - position
        
        if (diff.length() > 0.5f) {
            // Persistent slow speed
            val moveSpeed = 0.25f
            val targetVelocity = diff.normalized() * moveSpeed
            
            // Apply speed directly with very little damping for persistence
            rigidBody?.speed = rigidBody!!.speed + targetVelocity * 0.02f
        } else {
            rigidBody?.speed = rigidBody?.speed?.times(0.8f) ?: Vec2.ZERO
        }


        // 2. Stability Constraint (Stay near legs)
        var coM = Vec2.ZERO
        attachedLegs.forEach { coM = coM + it.anchorPoint }
        coM = coM / attachedLegs.size.toFloat()
        
        val distToLegs = (position - coM).length()
        val maxLeashDist = 20f
        if (distToLegs > maxLeashDist) {
            // Physics-based pull back (no snapping)
            val pullBack = (coM - position).normalized() * (distToLegs - maxLeashDist) * 0.002f
            rigidBody?.speed = (rigidBody?.speed ?: Vec2.ZERO) + pullBack
        }
    }

    override fun createBaseHitbox() {
        super.createBaseHitbox()
        mainHitbox!!.hitboxType = HitboxTypes.ONLY_TRIGGER
    }

    fun onLegLost(leg: SpiderLegEntity) {
    }

    override fun die() {
        if (isDead) return
        super.die()
        legs.forEach { if (!it.isDead) it.die() }
    }

    override fun isAnyPhysicBlockCollision(): Boolean = false
}
