package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.Items.AxeItem
import la.vok.Game.GameContent.Items.Items.PickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.State.AppState

class AxeItemType : AbstractItemType() {
    override val tag: String = ItemsList.axe
    override val texture = AppState.res("axe.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle) : Item {
        return AxeItem(this, gameCycle)
    }
}