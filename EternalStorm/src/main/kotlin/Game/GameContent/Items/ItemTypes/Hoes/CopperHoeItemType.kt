package la.vok.Game.GameContent.Items.ItemTypes.Hoes

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.Hoes.CopperHoeItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class CopperHoeItemType : HoeItemType() {
    override val tag: String = ItemsList.copper_hoe
    override val texture: String = AppState.res("copper_hoe.png")

    override fun createItem(gameCycle: GameCycle): Item {
        return CopperHoeItem(this, gameCycle)
    }
}
