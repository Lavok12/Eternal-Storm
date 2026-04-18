package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.CosmicPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class CosmicPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.cosmic_pickaxe
    override val texture: String = AppState.res("cosmic_pickaxe.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return CosmicPickaxeItem(this, gameCycle)
    }
}
