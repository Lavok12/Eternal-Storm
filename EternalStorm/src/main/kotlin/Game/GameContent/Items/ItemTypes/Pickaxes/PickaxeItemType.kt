package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.Items.Items.PickaxeItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameController.GameCycle
import la.vok.State.AppState

open class PickaxeItemType : AbstractItemType() {
    override val tag: String = ItemsList.pickaxe
    override val texture: String = AppState.res("pickaxe.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle) : Item {
        return PickaxeItem(this, gameCycle)
    }
}