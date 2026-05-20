package la.vok.Game.GameSystems.WorldSystems.Entities

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2

class TeleportService(val gameCycle: GameCycle) {

    fun teleport(entity: Entity, pos: Vec2) {
        entity.position = pos.copy()
        entity.rigidBody?.speed = Vec2.ZERO
    }

    fun teleport(entity: Entity, targetDimension: AbstractDimension, pos: Vec2) {
        val currentDim = entity.dimension ?: return
        if (currentDim === targetDimension) {
            teleport(entity, pos)
            return
        }

        gameCycle.entityApi.hideEntity(currentDim, entity)
        currentDim.entitySystem.removeImmediate(entity)
        
        entity.dimension = targetDimension
        targetDimension.entitySystem.add(entity.systemId, entity, pos)
        
        gameCycle.entityApi.showEntity(targetDimension, entity)
        entity.rigidBody?.speed = Vec2.ZERO
    }
}
