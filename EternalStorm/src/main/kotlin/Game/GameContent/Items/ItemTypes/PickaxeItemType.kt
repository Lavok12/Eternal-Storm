package la.vok.Game.GameContent.Items.ItemTypes

import la.vok.Game.GameContent.Items.Items.PickaxeItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameController.GameCycle

open class PickaxeItemType : AbstractItemType() {
    override val tag: String = ItemsList.pickaxe
    override val texture = "pickaxe.png"

    override fun createItem(gameCycle: GameCycle) : Item {
        return PickaxeItem(this, gameCycle)
    }
}