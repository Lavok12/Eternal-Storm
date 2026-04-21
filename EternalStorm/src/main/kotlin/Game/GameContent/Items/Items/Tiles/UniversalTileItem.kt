package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

@Suppress("UNCHECKED_CAST")
open class UniversalTileItem(itemType: AbstractItemType, gameCycle: GameCycle) : Item(itemType, gameCycle)