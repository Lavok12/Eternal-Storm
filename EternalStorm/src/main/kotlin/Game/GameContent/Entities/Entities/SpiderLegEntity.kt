package la.vok.Game.GameContent.Entities.Entities

import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.EntityComponents.*
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class SpiderLegEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    
    var parentBody: SpiderBossEntity? = null
    var isAttached = false
    var isAttachedOnTile = false
    var anchorPoint: Vec2 = Vec2.ZERO
    var visualAnchorPoint: Vec2 = Vec2.ZERO
    var legIndex: Int = 0 // Used for sector logic in AI
    
    // Relative position on the body (shoulder)
    var shoulderOffset: Vec2 = Vec2.ZERO

    init {
        removeComponent<GravityComponent>()
        rigidBody?.friction = 0.1f
    }

    override fun spawn() {
        super.spawn()
        anchorPoint = position.copy()
        visualAnchorPoint = position.copy()
    }

    override fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean {
        val taken = super.takeDamage(damage, hitboxComponent)
        if (isDead) {
            parentBody?.onLegLost(this)
        }
        return taken
    }

    override fun physicUpdate() {
        super.physicUpdate()

        // Drag logic: if leg anchor has drifted too far from shoulder, pull anchor back
        val shoulder = (parentBody?.position ?: position) + shoulderOffset
        val distToShoulder = (anchorPoint - shoulder).length()
        val hardLimit = 14f
        if (distToShoulder > hardLimit) {
            // Gradually drag the anchor toward where the shoulder currently is
            anchorPoint = anchorPoint.lerped(shoulder + (anchorPoint - shoulder).normalized() * (hardLimit - 1f), 0.07f)
            // Also snap visual to avoid teleport flash
            visualAnchorPoint = visualAnchorPoint.lerped(anchorPoint, 0.2f)
        }

        // Slow smooth visual travel (less jerk)
        val lerpSpeed = 0.08f
        visualAnchorPoint = visualAnchorPoint.lerped(anchorPoint, lerpSpeed)

        if (isAttached) {
            position = anchorPoint.copy()
            rigidBody?.speed = Vec2.ZERO
        }
    }

    fun moveTo(newAnchor: Vec2) {
        anchorPoint = newAnchor.copy()
        isAttached = true
        isAttachedOnTile = true 
    }

    fun moveToAir(newPos: Vec2) {
        anchorPoint = newPos.copy()
        isAttached = true
        isAttachedOnTile = false
    }

    override fun isAnyPhysicBlockCollision(): Boolean = false

    fun isMovingToTarget(): Boolean {
        return (visualAnchorPoint - anchorPoint).length() > 0.1f
    }
}
