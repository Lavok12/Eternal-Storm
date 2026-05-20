package la.vok.Game.GameSystems.WorldSystems.Entities

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.Vectors.Vec2

class WorldQuery(val gameCycle: GameCycle) {

    fun getById(dimension: AbstractDimension, id: Long): Entity? {
        return dimension.entitySystem.idMap[id]
    }

    fun getActiveEntities(dimension: AbstractDimension): List<Entity> {
        return dimension.entitySystem.entities.toList()
    }

    fun isExist(dimension: AbstractDimension, entity: Entity): Boolean {
        return dimension.entitySystem.entities.contains(entity)
    }

    fun containsEntityById(dimension: AbstractDimension, id: Long): Boolean {
        return dimension.entitySystem.idMap.containsKey(id)
    }

    fun hasTag(entity: Entity, tag: String): Boolean = entity.entityType.tags.contains(tag)

    /**
     * Finds the nearest entity matching the given filter.
     */
    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, tagFilter: TagFilter = TagFilter.Any): Entity? {
        return dimension.entitySystem.entities
            .filter { tagFilter.matches(it.entityType.tags.toList() + it.entityType.tag) }
            .minByOrNull { (it.position - pos).length() }
    }

    /**
     * Finds the nearest entity matching the given filter within a maximum distance.
     */
    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, maxDistance: Float, tagFilter: TagFilter = TagFilter.Any): Entity? {
        return dimension.entitySystem.entities
            .filter { tagFilter.matches(it.entityType.tags.toList() + it.entityType.tag) }
            .filter { (it.position - pos).length() <= maxDistance }
            .minByOrNull { (it.position - pos).length() }
    }

    fun delete(dimension: AbstractDimension, id: Long) {
        val entity = getById(dimension, id) ?: return
        dimension.entitySystem.delete(entity)
    }

    fun kill(dimension: AbstractDimension, id: Long) {
        val entity = getById(dimension, id) ?: return
        dimension.entitySystem.kill(entity)
    }

    fun containsEntityAcrossDimensions(entity: Entity): Boolean {
        return gameCycle.dimensionsController.dimensions.values.any { it.entitySystem.entities.contains(entity) }
    }

    fun containsEntityByIdAcrossDimensions(id: Long): Boolean {
        return gameCycle.dimensionsController.dimensions.values.any { it.entitySystem.idMap.containsKey(id) }
    }

    fun getByIdAcrossDimensions(id: Long): Entity? {
        return gameCycle.dimensionsController.dimensions.values.firstNotNullOfOrNull { it.entitySystem.idMap[id] }
    }

    fun getAllAcrossDimensions(): List<Entity> = gameCycle.dimensionsController.dimensions.values.flatMap { it.entitySystem.entities }

    fun getAllByFilterAcrossDimensions(tagFilter: TagFilter): List<Entity> {
        return gameCycle.dimensionsController.dimensions.values.flatMap { dim ->
            dim.entitySystem.entities.filter { tagFilter.matches(it.entityType.tags.toList() + it.entityType.tag) }
        }
    }
}
