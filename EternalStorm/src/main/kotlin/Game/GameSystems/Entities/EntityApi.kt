package la.vok.Game.GameSystems.Entities

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class EntityApi(var entityController: EntityController) {
    val gameController: GameController get() = entityController.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {
        return entityController.gameController.mapController.mapApi.getPointFromPos(pos)
    }
    fun getRegisteredEntity(tag: String) : Entity {
        return objectRegistration.entities[tag]!!.createEntity(gameController).apply { entitySystem = entityController.entitySystem }
    }
    fun getRegisteredEntityType(tag: String) : AbstractEntityType {
        return objectRegistration.entities[tag]!!
    }
    fun getRegisteredEntityByType(type: AbstractEntityType) : Entity {
        return type.createEntity(gameController).apply { entitySystem = entityController.entitySystem }
    }
    fun showEntity(entity: Entity) {
        AppState.logger.trace("ShowEntity $entity")
        entity.show()
    }
    fun hideEntity(entity: Entity) {
        AppState.logger.trace("HideEntity $entity")
        entity.hide()
    }

    fun addInSystem(entity: Entity, pos: Vec2) : Long {
        entityController.entitySystem.add(entity, pos)
        return entity.systemId
    }
    fun addInSystem(entity: Entity) : Long {
        entityController.entitySystem.add(entity)
        return entity.systemId
    }
    fun deleteInSystem(entity: Entity) {
        entityController.entitySystem.delete(entity)
    }
    fun isExist(entity: Entity) : Boolean {
        return entityController.entitySystem.isExist(entity)
    }
}