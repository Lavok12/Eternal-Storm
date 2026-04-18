package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Items.CelestialPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class CelestialPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.celestial_pickaxe
    override val texture: String = AppState.res("celestial_pickaxe.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return CelestialPickaxeItem(this, gameCycle)
    }
}
