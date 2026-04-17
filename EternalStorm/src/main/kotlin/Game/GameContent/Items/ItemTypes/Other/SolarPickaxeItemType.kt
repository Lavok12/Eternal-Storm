package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Items.Items.SolarPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle

class SolarPickaxeItemType : PickaxeItemType() {
    override val tag: String = ItemsList.solar_pickaxe
    override val texture = "solar_pickaxe.png"
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return SolarPickaxeItem(this, gameCycle)
    }
}
