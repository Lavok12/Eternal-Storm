package la.vok.Game.GameSystems.WorldSystems.Items

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Special.ItemEntity
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class ItemsApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    fun getRegisteredItemType(tag: String): AbstractItemType {
        AppState.logger.debug("Getting registered item type by tag: $tag")
        return objectRegistration.items[tag]!!
    }

    fun getRegisteredItem(tag: String, count: Int = 1): Item {
        val item = getRegisteredItemType(tag).createItem(gameCycle)
        item.count = count
        AppState.logger.debug("Created registered item from tag: $tag (Item: $item)")
        return item
    }

    fun getRegisteredItem(type: AbstractItemType, count: Int = 1): Item {
        val item = type.createItem(gameCycle)
        item.count = count
        AppState.logger.debug("Created registered item from type: ${type::class.simpleName} (Item: $item)")
        return item
    }

    fun spawnItemEntity(dimension: AbstractDimension, item: Item, pos: Vec2): ItemEntity {
        val entity = ItemEntity(gameCycle)
        entity.setItem(item)
        gameCycle.entityApi.addInSystem(dimension, entity, pos)
        gameCycle.entityApi.initEntity(dimension, entity)
        return entity
    }

    fun spawnItemEntity(dimension: AbstractDimension, tag: String, pos: Vec2, count: Int = 1): ItemEntity {
        return spawnItemEntity(dimension, getRegisteredItem(tag, count), pos)
    }

    fun spawnItemEntity(dimension: AbstractDimension, type: AbstractItemType, pos: Vec2, count: Int = 1): ItemEntity {
        return spawnItemEntity(dimension, getRegisteredItem(type, count), pos)
    }

    fun spawnItemEntity(dimension: AbstractDimension, item: Item, pos: Vec2, randomVelocity: Boolean = false): ItemEntity {
        val entity = ItemEntity(gameCycle)
        entity.setItem(item)
        gameCycle.entityApi.addInSystem(dimension, entity, pos)
        gameCycle.entityApi.initEntity(dimension, entity)
        if (randomVelocity) {
            val angle = Math.random() * Math.PI * 2
            val speed = 0.05f + Math.random().toFloat() * 0.1f
            entity.rigidBody?.addForce(
                (Math.cos(angle) * speed).toFloat() v (Math.sin(angle) * speed + 0.15f).toFloat()
            )
        }
        return entity
    }

    fun spawnItemEntity(dimension: AbstractDimension, tag: String, pos: Vec2, count: Int = 1, randomVelocity: Boolean = false): ItemEntity {
        return spawnItemEntity(dimension, getRegisteredItem(tag, count), pos, randomVelocity)
    }

    fun spawnItemEntity(dimension: AbstractDimension, type: AbstractItemType, pos: Vec2, count: Int = 1, randomVelocity: Boolean = false): ItemEntity {
        return spawnItemEntity(dimension, getRegisteredItem(type, count), pos, randomVelocity)
    }

    fun spawnDropTable(dimension: AbstractDimension, dropEntry: DropEntry, pos: Vec2, randomVelocity: Boolean = true) {
        dropEntry.resolve().forEach { (tag, count) ->
            spawnItemEntity(dimension, tag, pos, count, randomVelocity)
        }
    }
}