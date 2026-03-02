package la.vok.Game.GameContent.Entities.Ai

import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2

class PlayerAI(entity: Entity, gameCycle: GameCycle) : AbstractAI(entity, gameCycle) {
    fun moveLeft() {
        entity.changeFacing(-1)
        if (entity.isAnyPhysicBlockCollision()) {
            entity.rigidBody?.speed?.x -= 1.25f * FrameLimiter.logicDeltaTime
        } else {
            entity.rigidBody?.speed?.x -= 0.3f * FrameLimiter.logicDeltaTime
        }
    }
    fun moveRight() {
        entity.changeFacing(1)
        if (entity.isAnyPhysicBlockCollision()) {
            entity.rigidBody?.speed?.x += 1.25f * FrameLimiter.logicDeltaTime
        } else {
            entity.rigidBody?.speed?.x += 0.3f * FrameLimiter.logicDeltaTime
        }
    }
    fun tryJump() {
        if (entity.downTrigger?.blocksCollision ?: false) {
            entity.rigidBody?.speed?.y = 0.55f
        }
    }

    override fun targetScreenPos(): Vec2 =
        gameCycle.gameController.playerControl.getTarget()

    override fun targetWorldPos(): Vec2 =
        screenToWorld(targetScreenPos())

    override fun targetMapPos(): LPoint =
        worldToMap(targetWorldPos())
}