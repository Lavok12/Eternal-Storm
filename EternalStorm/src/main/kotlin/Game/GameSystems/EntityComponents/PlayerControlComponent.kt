package la.vok.Game.GameSystems.EntityComponents

import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Entities.Entities.Entity

class PlayerControlComponent(entity: Entity) : EntityComponent(entity) {
    fun moveLeft() {
        if (entity.isAnyPhysicBlockCollision()) {
            entity.rigidBody?.speed?.x -= 1.25f * FrameLimiter.logicDeltaTime
        } else {
            entity.rigidBody?.speed?.x -= 0.3f * FrameLimiter.logicDeltaTime
        }
    }
    fun moveRight() {
        if (entity.isAnyPhysicBlockCollision()) {
            entity.rigidBody?.speed?.x += 1.25f * FrameLimiter.logicDeltaTime
        } else {
            entity.rigidBody?.speed?.x += 0.3f * FrameLimiter.logicDeltaTime
        }
    }
    fun tryJump() {
        if (entity.hitboxes["down trigger"]?.blocksCollision ?: false) {
            entity.rigidBody?.speed?.y = 0.55f
        }
    }
}