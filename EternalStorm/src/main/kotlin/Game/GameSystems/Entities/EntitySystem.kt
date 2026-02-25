package la.vok.Game.GameSystems.Entities

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class EntitySystem(var entityController: EntityController) {
    var entities = HashSet<Entity>()
    var ids: Long = 0L

    fun setId(entity: Entity) {
        entity.systemId = ids
        ids++
    }
    fun add(entity: Entity, pos: Vec2) {
        AppState.logger.info("Add Entity $entity, $pos")
        setId(entity)
        entities.add(entity)
        entity.position = pos
        entityController.entityApi.showEntity(entity)
    }
    fun add(entity: Entity) {
        AppState.logger.info("Add Entity $entity")
        setId(entity)
        entities.add(entity)
        entityController.entityApi.showEntity(entity)
    }
    fun delete(entity: Entity) {
        AppState.logger.info("Delete Entity $entity")
        entity.hide()
        entities.remove(entity)
    }
    fun isExist(entity: Entity) : Boolean {
        return entities.contains(entity)
    }
}