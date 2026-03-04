package la.vok.Game.GameContent.Entities.Ai

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class SlimeAI(entity: Entity, gameCycle: GameCycle) : AbstractAI(entity, gameCycle) {
    open var direction = -1
    open var jumpPower = 0.4 v 0.6
    open var jumpTimer = 200

    var lastJump = jumpTimer

    fun getTargetDirection() {
        val target = gameCycle.entityApi.getNearestEntity(entity.position, 50f, EntitiesList.player)
        if (target == null) return
        if (target.position.x > entity.position.x) {
            direction = 1
        } else {
            direction = -1
        }
    }
    override fun physicUpdate() {
        lastJump--
        if (lastJump < 0) {
            lastJump = jumpTimer
            getTargetDirection()
            if (entity.downTrigger?.blocksCollision ?: false) {
                entity.rigidBody?.addForce(jumpPower * (direction v 1))
            }
        }
    }

    override fun targetScreenPos(): Vec2 {
        return worldToScreen(targetWorldPos())
    }

    override fun targetWorldPos(): Vec2 {
        val target = gameCycle.entityApi.getNearestEntity(entity.position, 50f, EntitiesList.player)
        return target?.position ?: (Vec2.ZERO)
    }

    override fun targetMapPos(): LPoint {
        return worldToMap(targetWorldPos())
    }
}