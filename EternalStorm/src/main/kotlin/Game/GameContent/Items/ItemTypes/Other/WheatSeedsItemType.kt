package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.Other.WheatSeedsItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class WheatSeedsItemType : AbstractItemType() {
    override val tag: String = ItemsList.wheat_seeds
    override val texture: String = AppState.res("seed_item.png")
    override val tags = setOf(ItemTags.PLACEABLE)
    override val maxInStack: Int = 9999

    override fun createItem(gameCycle: GameCycle): Item {
        return WheatSeedsItem(this, gameCycle)
    }
}
