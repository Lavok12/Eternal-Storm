package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Items.Items.CopperPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class CopperPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.copper_pickaxe
    override val texture = "copper_pickaxe.png"
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return CopperPickaxeItem(this, gameCycle)
    }
}
