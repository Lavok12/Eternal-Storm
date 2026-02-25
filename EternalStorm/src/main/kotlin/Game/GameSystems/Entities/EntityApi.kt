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
        return objectRegistration.entities[tag]!!.createEntity(gameController).apply {
            entitySystem = entityController.entitySystem
            mapSystem = gameController.mapController.mapSystem
            spawn()
        }
    }
    fun getRegisteredEntityType(tag: String) : AbstractEntityType {
        return objectRegistration.entities[tag]!!
    }
    fun getRegisteredEntityByType(type: AbstractEntityType) : Entity {
        return type.createEntity(gameController).apply {
            entitySystem = entityController.entitySystem
            mapSystem = gameController.mapController.mapSystem
            spawn()
        }
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
    fun addInSystemWithId(id: Long, entity: Entity, pos: Vec2) : Long {
        entityController.entitySystem.add(id, entity, pos)
        return entity.systemId
    }
    fun addInSystemWithId(id: Long, entity: Entity) : Long {
        entityController.entitySystem.add(id, entity)
        return entity.systemId
    }
    fun deleteInSystem(entity: Entity) {
        entityController.entitySystem.delete(entity)
    }
    fun isExist(entity: Entity) : Boolean {
        return entityController.entitySystem.isExist(entity)
    }
    fun getActiveEntities() : HashSet<Entity> {
        return entityController.entitySystem.entities
    }
    fun containsEntityById(id: Long) : Boolean {
        return entityController.entitySystem.isExist(id)
    }
    fun getById(id: Long) : Entity? {
        return entityController.entitySystem.idMap[id]
    }


    fun getFirstByTag(tag: String): Entity? {
        return entityController.entitySystem.entities.firstOrNull { it.entityType.tags.contains(tag) }
    }
    fun getAllByTag(tag: String): List<Entity> {
        return entityController.entitySystem.entities.filter { it.entityType.tags.contains(tag) }
    }
    fun getByAnyTags(filterTags: Collection<String>): List<Entity> {
        return entityController.entitySystem.entities.filter { entity ->
            entity.entityType.tags.any { it in filterTags }
        }
    }
    fun getByAllTags(filterTags: Collection<String>): List<Entity> {
        return entityController.entitySystem.entities.filter { entity ->
            filterTags.all { tag -> entity.entityType.tags.contains(tag) }
        }
    }
    fun find(predicate: (Entity) -> Boolean): List<Entity> {
        return entityController.entitySystem.entities.filter(predicate)
    }
    fun hasTag(entity: Entity, tag: String): Boolean {
        return entity.entityType.tags.contains(tag)
    }
    fun hasAllTags(entity: Entity, filterTags: Collection<String>): Boolean {
        return filterTags.all { it in entity.entityType.tags }
    }
    fun hasAnyTag(entity: Entity, filterTags: Collection<String>): Boolean {
        return entity.entityType.tags.any { it in filterTags }
    }
    fun typeHasTag(type: AbstractEntityType, tag: String): Boolean {
        return type.tags.contains(tag)
    }

    fun typeHasAllTags(type: AbstractEntityType, filterTags: Collection<String>): Boolean {
        return filterTags.all { it in type.tags }
    }

    fun typeHasAnyTag(type: AbstractEntityType, filterTags: Collection<String>): Boolean {
        return type.tags.any { it in filterTags }
    }
}