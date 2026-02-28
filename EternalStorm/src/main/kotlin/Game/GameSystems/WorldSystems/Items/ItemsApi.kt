package la.vok.Game.GameSystems.WorldSystems.Items

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState

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

}