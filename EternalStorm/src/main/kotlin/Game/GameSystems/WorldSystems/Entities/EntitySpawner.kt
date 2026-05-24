package la.vok.Game.GameSystems.WorldSystems.Entities

import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.*
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Entities.Entities.Projectiles.ProjectileEntity
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.LavokLibrary.Vectors.Vec2

class EntitySpawner(val gameCycle: GameCycle) {
    val objectRegistration: ObjectRegistration get() = gameCycle.gameController.coreController.objectRegistration

    fun getRegisteredEntity(tag: String): Entity {
        return objectRegistration.entities[tag]!!.createEntity(gameCycle)
    }

    fun getRegisteredEntityType(tag: String): AbstractEntityType {
        return objectRegistration.entities[tag]!!
    }

    fun spawnEntity(dimension: AbstractDimension, type: String, pos: Vec2 = Vec2.ZERO): Entity? {
        val entity = getRegisteredEntity(type)
        addInSystem(dimension, entity, pos)
        initEntity(dimension, entity)
        return entity
    }

    fun spawnEntity(dimension: AbstractDimension, entity: Entity, pos: Vec2 = Vec2.ZERO): Entity? {
        addInSystem(dimension, entity, pos)
        initEntity(dimension, entity)
        return entity
    }

    fun spawnEntity(dimension: AbstractDimension, id: Long, type: String, pos: Vec2 = Vec2.ZERO): Entity? {
        val entity = getRegisteredEntity(type)
        addInSystem(dimension, id, entity, pos)
        initEntity(dimension, entity)
        return entity
    }

    fun spawnEntity(dimension: AbstractDimension, id: Long, entity: Entity, pos: Vec2 = Vec2.ZERO): Entity? {
        addInSystem(dimension, id, entity, pos)
        initEntity(dimension, entity)
        return entity
    }

    fun spawnBlockEntity(dimension: AbstractDimension, blockType: IBlockType, pos: Vec2): BlockEntity {
        val entity = BlockEntity(blockType, AbstractEntityType.BlockEntityType, gameCycle)
        addInSystem(dimension, entity, pos)
        initEntity(dimension, entity)
        return entity
    }

    fun initEntity(dimension: AbstractDimension, entity: Entity) {
        entity.spawn()
        gameCycle.entityApi.showEntity(dimension, entity)
    }

    fun addInSystem(dimension: AbstractDimension, entity: Entity, pos: Vec2): Long {
        entity.dimension = dimension
        dimension.entitySystem.add(entity, pos)
        return entity.systemId
    }

    fun addInSystem(dimension: AbstractDimension, id: Long, entity: Entity, pos: Vec2): Long {
        entity.dimension = dimension
        dimension.entitySystem.add(id, entity, pos)
        return entity.systemId
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

    fun spawnFallingBlock(dimension: AbstractDimension, blockType: IBlockType, pos: Vec2): FallingBlockEntity {
        val entity = FallingBlockEntity(blockType, gameCycle)
        addInSystem(dimension, entity, pos)
        initEntity(dimension, entity)
        return entity
    }
}
