package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.WindPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class WindPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.wind_pickaxe
    override val texture: String = AppState.res("wind_pickaxe.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return WindPickaxeItem(this, gameCycle)
    }
}
