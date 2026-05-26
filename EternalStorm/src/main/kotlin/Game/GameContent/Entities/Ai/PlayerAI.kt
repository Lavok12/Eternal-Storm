package la.vok.Game.GameContent.Entities.Ai

import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.Game.GameContent.ContentList.BuffTags

class PlayerAI(entity: Entity, gameCycle: GameCycle) : AbstractAI(entity, gameCycle) {
    fun moveLeft() {
        entity.changeFacing(-1)
        if (entity.isAnyPhysicBlockCollision()) {
            entity.rigidBody?.speed?.x -= 1.25f * FrameLimiter.logicDeltaTime * entity.buffController.getStat(BuffTags.STAT_SPEED)
        } else {
            entity.rigidBody?.speed?.x -= 0.3f * FrameLimiter.logicDeltaTime * entity.buffController.getStat(BuffTags.STAT_SPEED)
        }
    }
    fun moveRight() {
        entity.changeFacing(1)
        if (entity.isAnyPhysicBlockCollision()) {
            entity.rigidBody?.speed?.x += 1.25f * FrameLimiter.logicDeltaTime * entity.buffController.getStat(BuffTags.STAT_SPEED)
        } else {
            entity.rigidBody?.speed?.x += 0.3f * FrameLimiter.logicDeltaTime * entity.buffController.getStat(BuffTags.STAT_SPEED)
        }
    }
    fun tryJump() {
        if (entity.downTrigger?.blocksCollision ?: false) {
            entity.rigidBody?.speed?.y = 0.65f * entity.buffController.getStat(BuffTags.STAT_JUMP_POWER)
        }
    }

    override fun targetScreenPos(): Vec2 =
        gameCycle.gameController.playerControl.getTarget()

    override fun targetWorldPos(): Vec2 =
        screenToWorld(targetScreenPos())

    override fun targetMapPos(): LPoint =
        worldToMap(targetWorldPos())
}