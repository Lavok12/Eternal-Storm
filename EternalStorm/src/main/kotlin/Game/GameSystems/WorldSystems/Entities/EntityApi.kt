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
import la.vok.State.AppState
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Entities.Entities.Special.BlockEntity
import la.vok.Game.GameContent.Entities.Entities.Special.FallingBlockEntity
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType

class EntityApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController

    // Delegated systems
    val spawner = EntitySpawner(gameCycle)
    val damageSystem = DamageSystem(gameCycle)
    val query = WorldQuery(gameCycle)
    val teleportService = TeleportService(gameCycle)

    init {
        gameCycle.gameContext.apply {
            entitySpawner = this@EntityApi.spawner
            damageSystem = this@EntityApi.damageSystem
            worldQuery = this@EntityApi.query
            teleportService = this@EntityApi.teleportService
        }
    }

    // --- Visibility ---
    fun showEntity(dimension: AbstractDimension, entity: Entity) {
        if (dimension == gameController.playerControl.getPlayerEntity()?.dimension)
            AppState.logger.trace("ShowEntity $entity")
        entity.show()
    }

    fun hideEntity(dimension: AbstractDimension, entity: Entity) {
        AppState.logger.trace("HideEntity $entity")
        entity.hide()
    }

    fun showDimensionEntity(abstractDimension: AbstractDimension) {
        getActiveEntities(abstractDimension).forEach { showEntity(abstractDimension, it) }
    }

    fun hideDimensionEntity(abstractDimension: AbstractDimension) {
        getActiveEntities(abstractDimension).forEach { hideEntity(abstractDimension, it) }
    }

    // --- Spawner Delegation ---
    fun getRegisteredEntity(tag: String) = spawner.getRegisteredEntity(tag)
    fun getRegisteredEntityType(tag: String) = spawner.getRegisteredEntityType(tag)
    fun getRegisteredEntityByType(tag: String) = spawner.getRegisteredEntityType(tag) // Alias for compatibility
    
    fun spawnEntity(dimension: AbstractDimension, type: String, pos: Vec2 = Vec2.ZERO) = spawner.spawnEntity(dimension, type, pos)
    fun spawnEntity(dimension: AbstractDimension, entity: Entity, pos: Vec2 = Vec2.ZERO) = spawner.spawnEntity(dimension, entity, pos)
    fun spawnEntity(dimension: AbstractDimension, id: Long, type: String, pos: Vec2 = Vec2.ZERO) = spawner.spawnEntity(dimension, id, type, pos)
    fun spawnEntity(dimension: AbstractDimension, id: Long, entity: Entity, pos: Vec2 = Vec2.ZERO) = spawner.spawnEntity(dimension, id, entity, pos)
    
    fun spawnProjectile(dimension: AbstractDimension, projectile: ProjectileEntity, pos: Vec2, direction: Vec2, speed: Float, targetTags: List<String> = projectile.targetTags) = 
        spawner.spawnProjectile(dimension, projectile, pos, direction, speed, targetTags)
    fun spawnFallingBlock(dimension: AbstractDimension, blockTile: la.vok.Game.GameSystems.WorldSystems.Map.IBlockType, pos: Vec2) = spawner.spawnFallingBlock(dimension, blockTile, pos)
    fun spawnBlockEntity(dimension: AbstractDimension, blockTile: la.vok.Game.GameSystems.WorldSystems.Map.IBlockType, pos: Vec2) = spawner.spawnBlockEntity(dimension, blockTile, pos)

    fun addInSystem(dimension: AbstractDimension, entity: Entity, pos: Vec2) = spawner.addInSystem(dimension, entity, pos)
    fun addInSystem(dimension: AbstractDimension, id: Long, entity: Entity, pos: Vec2) = spawner.addInSystem(dimension, id, entity, pos)
    fun initEntity(dimension: AbstractDimension, entity: Entity) = spawner.initEntity(dimension, entity)

    fun deleteInSystem(dimension: AbstractDimension, entity: Entity) = dimension.entitySystem.delete(entity)
    fun deleteInSystem(dimension: AbstractDimension, id: Long) = query.delete(dimension, id)
    fun killInSystem(dimension: AbstractDimension, entity: Entity) = dimension.entitySystem.kill(entity)
    fun killInSystem(dimension: AbstractDimension, id: Long) = query.kill(dimension, id)

    // --- Query Delegation ---
    fun getById(dimension: AbstractDimension, id: Long) = query.getById(dimension, id)
    fun getActiveEntities(dimension: AbstractDimension) = query.getActiveEntities(dimension)
    fun isExist(dimension: AbstractDimension, entity: Entity) = query.isExist(dimension, entity)
    fun containsEntityById(dimension: AbstractDimension, id: Long) = query.containsEntityById(dimension, id)
    
    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, maxDist: Float, tagFilter: TagFilter = TagFilter.Any) = query.getNearestEntity(dimension, pos, maxDist, tagFilter)
    fun getNearestEntity(dimension: AbstractDimension, pos: Vec2, tagFilter: TagFilter = TagFilter.Any) = query.getNearestEntity(dimension, pos, tagFilter)
    
    fun getByIdAcrossDimensions(id: Long) = query.getByIdAcrossDimensions(id)
    fun getAllAcrossDimensions() = query.getAllAcrossDimensions()
    fun getAllByFilterAcrossDimensions(tagFilter: TagFilter) = query.getAllByFilterAcrossDimensions(tagFilter)
    fun containsEntityByIdAcrossDimensions(id: Long) = query.containsEntityByIdAcrossDimensions(id)
    fun hasTag(entity: Entity, tag: String) = query.hasTag(entity, tag)

    // --- Damage Delegation ---
    fun damageZone(dimension: AbstractDimension, pos: Vec2, size: Vec2, damage: DamageData, tagFilter: TagFilter = TagFilter.Any) = damageSystem.damageZone(dimension, pos, size, damage, tagFilter)
    fun logicalDamage(entity: Entity, damage: DamageData, hitbox: HitboxComponent) = damageSystem.logicalDamage(entity, damage, hitbox)
    fun absoluteDamage(dimension: AbstractDimension, entity: Entity, damage: DamageData) = damageSystem.absoluteDamage(dimension, entity, damage)

    // --- Teleport Delegation ---
    fun teleport(entity: Entity, pos: Vec2) = teleportService.teleport(entity, pos)
    fun teleport(entity: Entity, dim: AbstractDimension, pos: Vec2) = teleportService.teleport(entity, dim, pos)
}
