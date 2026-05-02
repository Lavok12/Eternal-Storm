package la.vok.Game.GameContent.Items.Items.Other

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.List.SeedHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameContent.TileTypes.WheatTileType

class WheatSeedsItem(itemType: AbstractItemType, gameCycle: GameCycle) : Item(itemType, gameCycle) {
    override fun getHandItem(component: HandItemComponent): HandItem? {
        val tileType = gameCycle.gameController.coreController.objectRegistration.tiles[TilesList.wheat_block] as WheatTileType
        return SeedHandItem(this, component, tileType, itemType.texture)
    }
}
