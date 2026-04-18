package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.WoodenPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class WoodenPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.wooden_pickaxe
    override val texture: String = AppState.res("wooden_pickaxe.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return WoodenPickaxeItem(this, gameCycle)
    }
}
