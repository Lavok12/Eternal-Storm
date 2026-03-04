package la.vok.Game.GameContent.Items.ItemTypes

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Items.SpearItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameController.GameCycle

class SpearItemType : AbstractItemType() {
    override val tag: String = ItemsList.spear
    override val texture = "spear.png"

    override fun createItem(gameCycle: GameCycle) : Item {
        return SpearItem(this, gameCycle)
    }
}