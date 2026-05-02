package la.vok.Game.GameContent.Items.Items.Hoes

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.List.HoeHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

class CopperHoeItem(itemType: AbstractItemType, gameCycle: GameCycle) : HoeItem(itemType, gameCycle) {
    override val handItemTexture = "copper_hoe.png"
}
