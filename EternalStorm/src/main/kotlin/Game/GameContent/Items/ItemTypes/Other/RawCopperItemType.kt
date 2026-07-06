package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.Other.RawCopperItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class RawCopperItemType : AbstractItemType() {
    override val tag: String = ItemsList.raw_copper_item
    override val texture: String = AppState.res("raw_copper_item.png")
    override val tags = setOf(ItemTags.MATERIAL)
    override val maxInStack: Int = 9999
    
    override fun createItem(gameCycle: GameCycle): Item {
        return RawCopperItem(this, gameCycle)
    }
}
