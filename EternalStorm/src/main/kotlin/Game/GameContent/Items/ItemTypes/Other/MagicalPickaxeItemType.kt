package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Items.Items.MagicalPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class MagicalPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.magical_pickaxe
    override val texture = "magical_pickaxe.png"
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return MagicalPickaxeItem(this, gameCycle)
    }
}
