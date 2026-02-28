package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.Weapons.TileHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent

@Suppress("UNCHECKED_CAST")
open class StoneItem(itemType: AbstractItemType, gameCycle: GameCycle) : Item(itemType, gameCycle) {
}