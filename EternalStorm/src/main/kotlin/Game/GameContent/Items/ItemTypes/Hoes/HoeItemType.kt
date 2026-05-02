package la.vok.Game.GameContent.Items.ItemTypes.Hoes

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.Items.Hoes.HoeItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

abstract class HoeItemType : AbstractItemType() {
    override val tags = setOf(ItemTags.TOOL, ItemTags.HOE)

    override fun createItem(gameCycle: GameCycle) : Item {
        return HoeItem(this, gameCycle)
    }
}
