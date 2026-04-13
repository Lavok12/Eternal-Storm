package la.vok.Game.GameSystems.WorldSystems.Entities

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.DamageEntity
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.Entities.Special.ProjectileEntity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class EntityApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {
        return gameCycle.mapApi.getPointFromPos(pos)
    }
    fun getRegisteredEntity(tag: String) : Entity {
        return objectRegistration.entities[tag]!!.createEntity(gameCycle).apply {
        }
    }
    fun getRegisteredEntityType(tag: String) : AbstractEntityType {
        return objectRegistration.entities[tag]!!
    }
    fun getRegisteredEntityByType(type: AbstractEntityType) : Entity {
        return type.createEntity(gameCycle).apply {
        }
    }
    fun showEntity(dimension: AbstractDimension, entity: Entity) {
        if (dimension == gameController.playerControl.getPlayerEntity()?.dimension)
        AppState.logger.trace("ShowEntity $entity")
        entity.show()
    }
    fun hideEntity(dimension: AbstractDimension, entity: Entity) {
        AppState.logger.trace("HideEntity $entity")
        entity.hide()
    }

    fun addInSystem(dimension: AbstractDimension, entity: Entity, pos: Vec2) : Long {
        entity.dimension = dimension
        dimension.entitySystem.add(entity, pos)
        return entity.systemId
    }
    fun addInSystem(dimension: AbstractDimension, entity: Entity) : Long {
        entity.dimension = dimension
        dimension.entitySystem.add(entity)
        return entity.systemId
    }
    fun addInSystemWithId(dimension: AbstractDimension, id: Long, entity: Entity, pos: Vec2) : Long {
        entity.dimension = dimension
        dimension.entitySystem.add(id, entity, pos)
        return entity.systemId
    }
    fun addInSystemWithId(dimension: AbstractDimension, id: Long, entity: Entity) : Long {
        entity.dimension = dimension
        dimension.entitySystem.add(id, entity)
        return entity.systemId
    }
    fun spawnEntity(dimension: AbstractDimension, type: String, pos: Vec2 = Vec2.ZERO) : Entity? {
        val entity = getRegisteredEntity(type)
        addInSystem(dimension, entity, pos)
        initEntity(dimension, entity)
        return entity
    }
    fun spawnEntity(dimension: AbstractDimension, entity: Entity, pos: Vec2 = Vec2.ZERO) : Entity? {
        addInSystem(dimension, entity, pos)
        initEntity(dimension, entity)
        return entity
    }
    fun spawnEntity(dimension: AbstractDimension, id: Long, type: String, pos: Vec2 = Vec2.ZERO) : Entity? {
        val entity = getRegisteredEntity(type)
        addInSystemWithId(dimension, id, entity, pos)
        initEntity(dimension, entity)
        return entity
    }
    fun spawnEntity(dimension: AbstractDimension, id: Long, entity: Entity, pos: Vec2 = Vec2.ZERO) : Entity? {
        addInSystemWithId(dimension, id, entity, pos)
        initEntity(dimension, entity)
        return entity
    }

    fun initEntity(dimension: AbstractDimension, entity: Entity) {
        entity.spawn()
        showEntity(dimension, entity)
    }

    fun deleteInSystem(dimension: AbstractDimension, entity: Entity) {
        dimension.entitySystem.delete(entity)
    }
    fun deleteInSystem(dimension: AbstractDimension, id: Long) {
        dimension.entitySystem.delete(id)
    }
    fun killInSystem(dimension: AbstractDimension, entity: Entity) {
        dimension.entitySystem.kill(entity)
    }
    fun killInSystem(dimension: AbstractDimension, id: Long) {
        dimension.entitySystem.kill(id)
    }
    fun isExist(dimension: AbstractDimension, entity: Entity) : Boolean {
        return dimension.entitySystem.isExist(entity)
    }
    fun getActiveEntities(dimension: AbstractDimension) : HashSet<Entity> {
        return dimension.entitySystem.entities
    }
    fun containsEntityById(dimension: AbstractDimension, id: Long) : Boolean {
        return dimension.entitySystem.isExist(id)
    }
    fun getById(dimension: AbstractDimension, id: Long) : Entity? {
        return dimension.entitySystem.idMap[id]
    }

    fun getFirstByTag(dimension: AbstractDimension, tag: String): Entity? {
        return dimension.entitySystem.entities.firstOrNull { it.entityType.tags.contains(tag) }
    }
    fun getAllByTag(dimension: AbstractDimension, tag: String): List<Entity> {
        return dimension.entitySystem.entities.filter { it.entityType.tags.contains(tag) }
    }
    fun getByAnyTags(dimension: AbstractDimension, filterTags: Collection<String>): List<Entity> {
        return dimension.entitySystem.entities.filter { entity ->
            entity.entityType.tags.any { it in filterTags }
        }
    }
    fun getByAllTags(dimension: AbstractDimension, filterTags: Collection<String>): List<Entity> {
        return dimension.entitySystem.entities.filter { entity ->
            filterTags.all { tag -> entity.entityType.tags.contains(tag) }
        }
    }
    fun find(dimension: AbstractDimension, predicate: (Entity) -> Boolean): List<Entity> {
        return dimension.entitySystem.entities.filter(predicate)
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
    fun getEntityHitboxes(dimension: AbstractDimension, id: Long) : HashMap<String, HitboxComponent>? {
        return getById(dimension, id)?.hitboxes
    }
    fun damageZone(dimension: AbstractDimension, pos: Vec2, size: Vec2, damage: DamageData, tagFilter: TagFilter = TagFilter.Any) : Entity {
        AppState.logger.info("Spawn Damage Zone $pos $size $damage $tagFilter")
        val entity = DamageEntity(gameCycle, pos, size, damage, tagFilter)
        spawnEntity(dimension, entity, pos)
        entity.damage()
        return entity
    }
    fun logicalDamage(entity: Entity, damage: DamageData, hitboxComponent: HitboxComponent) {
        entity.takeDamage(damage, hitboxComponent)
    }
    fun absoluteDamage(dimension: AbstractDimension, entity: Entity, damage: DamageData) {
        if (entity.hpBody == null) return
        entity.knockback(damage.force)
        entity.hpBody?.hp -= damage.value
        checkEntityHp(dimension, entity)
        entity.damage(damage)
        gameCycle.vfxObjectsApi.spawnDamageValue(dimension, entity.position, damage.value)
    }
    fun absoluteDamage(dimension: AbstractDimension, id: Long, damage: DamageData) {
        var entity = getById(dimension, id)
        if (entity == null) return
        absoluteDamage(dimension, entity, damage)
    }
    fun checkEntityHp(dimension: AbstractDimension, id: Long) {
        var entity = getById(dimension, id)
        if (entity?.hpBody == null) return
        if (entity.hpBody!!.hp <= 0) {
            killInSystem(dimension, entity)
        }
    }
    fun checkEntityHp(dimension: AbstractDimension, entity: Entity) {
        if (entity.hpBody == null) return
        if (entity.hpBody!!.hp <= 0) {
            killInSystem(dimension, entity)
        }
    }

    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, tagFilter: TagFilter = TagFilter.Any): Entity? {
        return dimension.entitySystem.entities
            .filter { tagFilter.matches(it.entityType.tags.toList()) }
            .minByOrNull { (it.position - pos).length() }
    }

    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, entityType: AbstractEntityType): Entity? {
        return dimension.entitySystem.entities
            .filter { it.entityType === entityType }
            .minByOrNull { (it.position - pos).length() }
    }

    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, entityTypeTag: String): Entity? {
        return getNearestEntity(dimension, pos, getRegisteredEntityType(entityTypeTag))
    }

    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, maxDistance: Float, tagFilter: TagFilter = TagFilter.Any): Entity? {
        return dimension.entitySystem.entities
            .filter { tagFilter.matches(it.entityType.tags.toList()) }
            .filter { (it.position - pos).length() <= maxDistance }
            .minByOrNull { (it.position - pos).length() }
    }

    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, maxDistance: Float, entityType: AbstractEntityType): Entity? {
        return dimension.entitySystem.entities
            .filter { it.entityType === entityType }
            .filter { (it.position - pos).length() <= maxDistance }
            .minByOrNull { (it.position - pos).length() }
    }

    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, maxDistance: Float, entityTypeTag: String): Entity? {
        return getNearestEntity(dimension, pos, maxDistance, getRegisteredEntityType(entityTypeTag))
    }

    fun spawnProjectile(
        dimension: AbstractDimension,
        projectile: ProjectileEntity,
        pos: Vec2,
        direction: Vec2,
        speed: Float,
        targetTags: List<String> = projectile.targetTags
    ): ProjectileEntity {
        projectile.targetTags = targetTags
        spawnEntity(dimension, projectile, pos)
        projectile.launch(direction, speed)
        return projectile
    }

    fun showDimensionEntity(abstractDimension: AbstractDimension) {
        for (i in getActiveEntities(abstractDimension)) {
            showEntity(abstractDimension, i)
        }
    }
    fun hideDimensionEntity(abstractDimension: AbstractDimension) {
        for (i in getActiveEntities(abstractDimension)) {
            hideEntity(abstractDimension, i)
        }
    }

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

        hideEntity(currentDim, entity)
        currentDim.entitySystem.removeImmediate(entity)
        addInSystemWithId(targetDimension, entity.systemId, entity, pos)
        showEntity(targetDimension, entity)

        entity.rigidBody?.speed = Vec2.ZERO
    }

    fun syncVisibility(entity: Entity, activeDimension: AbstractDimension) {
        if (entity.dimension === activeDimension) {
            showEntity(activeDimension, entity)
        } else {
            hideEntity(activeDimension, entity)
        }
    }

    fun syncAllVisibility(entities: Iterable<Entity>, activeDimension: AbstractDimension) {
        entities.forEach { syncVisibility(it, activeDimension) }
    }

    fun getByIdAcrossDimensions(id: Long): Entity? {
        return gameCycle.dimensionsController.dimensions.values.firstNotNullOfOrNull { it.entitySystem.idMap[id] }
    }

    fun getAllAcrossDimensions(): List<Entity> {
        return gameCycle.dimensionsController.dimensions.values.flatMap { it.entitySystem.entities }
    }

    fun getAllByTagAcrossDimensions(tag: String): List<Entity> {
        return gameCycle.dimensionsController.dimensions.values.flatMap { dim ->
            dim.entitySystem.entities.filter { it.entityType.tags.contains(tag) }
        }
    }

    fun getFirstByTagAcrossDimensions(tag: String): Entity? {
        return gameCycle.dimensionsController.dimensions.values.firstNotNullOfOrNull { dim ->
            dim.entitySystem.entities.firstOrNull { it.entityType.tags.contains(tag) }
        }
    }

    fun findAcrossDimensions(predicate: (Entity) -> Boolean): List<Entity> {
        return gameCycle.dimensionsController.dimensions.values.flatMap { dim ->
            dim.entitySystem.entities.filter(predicate)
        }
    }

    fun containsEntityAcrossDimensions(entity: Entity): Boolean {
        return gameCycle.dimensionsController.dimensions.values.any { it.entitySystem.entities.contains(entity) }
    }

    fun containsEntityByIdAcrossDimensions(id: Long): Boolean {
        return gameCycle.dimensionsController.dimensions.values.any { it.entitySystem.idMap.containsKey(id) }
    }
}