package la.vok.Game.GameSystems.Entities

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.DamageEntity
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class EntityApi(var entityController: EntityController) {
    val gameCycle: GameCycle = entityController.gameCycle
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {
        return gameCycle.mapController.mapApi.getPointFromPos(pos)
    }
    fun getRegisteredEntity(tag: String) : Entity {
        return objectRegistration.entities[tag]!!.createEntity(gameCycle).apply {
            spawn()
        }
    }
    fun getRegisteredEntityType(tag: String) : AbstractEntityType {
        return objectRegistration.entities[tag]!!
    }
    fun getRegisteredEntityByType(type: AbstractEntityType) : Entity {
        return type.createEntity(gameCycle).apply {
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
    fun spawnEntity(type: String, pos: Vec2 = 0 v 0) : Entity? {
        val entity = getRegisteredEntity(type)
        addInSystem(entity, pos)
        return entity
    }

    fun deleteInSystem(entity: Entity) {
        entityController.entitySystem.delete(entity)
    }
    fun deleteInSystem(id: Long) {
        entityController.entitySystem.delete(id)
    }
    fun killInSystem(entity: Entity) {
        entityController.entitySystem.kill(entity)
    }
    fun killInSystem(id: Long) {
        entityController.entitySystem.kill(id)
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
    fun getEntityHitboxes(entity: Entity) : HashMap<String, HitboxComponent> {
        return entity.hitboxes
    }
    fun getEntityHitboxes(id: Long) : HashMap<String, HitboxComponent>? {
        return getById(id)?.hitboxes
    }
    fun damageZone(pos: Vec2, size: Vec2, damage: DamageData, tagFilter: TagFilter = TagFilter.Any) : Entity {
        AppState.logger.info("Spawn Damage Zone $pos $size $damage $tagFilter")
        val entity = DamageEntity(gameCycle, pos, size, damage, tagFilter)
        addInSystem(entity)
        entity.damage()
        return entity
    }
    fun entityDamage(entity: Entity, damage: DamageData) {
        if (entity.hpBody == null) return
        entity.knockback(damage.force)
        entity.hpBody?.hp -= damage.damage
        checkEntityHp(entity)
    }
    fun entityDamage(id: Long, damage: DamageData) {
        var entity = getById(id)
        if (entity?.hpBody == null) return
        entity.knockback(damage.force)
        entity.hpBody?.hp -= damage.damage
        checkEntityHp(entity)
    }
    fun checkEntityHp(id: Long) {
        var entity = getById(id)
        if (entity?.hpBody == null) return
        if (entity.hpBody!!.hp <= 0) {
            killInSystem(entity)
        }
    }
    fun checkEntityHp(entity: Entity) {
        if (entity.hpBody == null) return
        if (entity.hpBody!!.hp <= 0) {
            killInSystem(entity)
        }
    }
}