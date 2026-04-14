package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.ItemTags

class AxeItemType : AbstractItemType() {
    override val tag: String = ItemsList.axe
    override val texture = "axe.png"
    override val tags = setOf(ItemTags.TOOL)
}