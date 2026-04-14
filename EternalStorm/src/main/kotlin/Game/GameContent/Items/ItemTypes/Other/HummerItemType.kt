package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.Items.HummerItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

open class HummerItemType : AbstractItemType() {
    override val tag: String = ItemsList.hummer
    override val texture = "hummer.png"
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle) : Item {
        return HummerItem(this, gameCycle)
    }
}