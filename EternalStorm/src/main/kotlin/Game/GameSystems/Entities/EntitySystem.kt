package la.vok.Game.GameSystems.Entities

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class EntitySystem(var entityController: EntityController) {
    var entities = HashSet<Entity>()
    var idMap = HashMap<Long, Entity>()

    var ids: Long = 1L

    fun setId(entity: Entity) {
        entity.systemId = ids
        ids++
    }
    fun add(entity: Entity, pos: Vec2) {
        AppState.logger.info("Add Entity $entity, $pos")
        setId(entity)
        entities.add(entity)
        idMap[entity.systemId] = entity
        entity.position = pos.copy()
        entityController.entityApi.showEntity(entity)
    }
    fun add(entity: Entity) {
        AppState.logger.info("Add Entity $entity")
        setId(entity)
        entities.add(entity)
        idMap[entity.systemId] = entity
        entityController.entityApi.showEntity(entity)
    }
    fun add(id: Long, entity: Entity, pos: Vec2) {
        AppState.logger.info("Add Entity $entity, $pos, $id")
        entity.systemId = id
        entities.add(entity)
        idMap[entity.systemId] = entity
        entity.position = pos.copy()
        entityController.entityApi.showEntity(entity)
    }
    fun add(id: Long, entity: Entity) {
        AppState.logger.info("Add Entity $entity, $id")
        entity.systemId = id
        entities.add(entity)
        idMap[entity.systemId] = entity
        entityController.entityApi.showEntity(entity)
    }
    fun delete(entity: Entity) {
        AppState.logger.info("Delete Entity $entity")
        entity.hide()
        idMap.remove(entity.systemId)
        entities.remove(entity)
    }
    fun delete(id: Long) {
        AppState.logger.info("Delete Entity $id")
        var entity = idMap[id] ?: return;
        entity.hide()
        idMap.remove(entity.systemId)
        entities.remove(entity)
    }
    fun isExist(entity: Entity) : Boolean {
        return entities.contains(entity)
    }
    fun isExist(id: Long) : Boolean {
        return idMap.containsKey(id)
    }
}