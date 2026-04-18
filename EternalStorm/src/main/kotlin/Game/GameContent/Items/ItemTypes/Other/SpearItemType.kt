package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.ContentList.ItemTags

class SpearItemType : AbstractItemType() {
    override val tag: String = ItemsList.spear
    override val texture: String = AppState.res("spear.png")
    override val tags = setOf(ItemTags.WEAPON)
}