package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.Items.MaterialPickaxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.State.AppState

class MaterialPickaxeItemType(
    override val tag: String,
    override val texture: String,
    val mineDamage: Int,
    val minePower: Int,
    val useDuration: Float,
    val handDamage: Int
) : PickaxeItemType() {
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return MaterialPickaxeItem(
            this,
            gameCycle,
            mineDamage,
            minePower,
            useDuration,
            AppState.res("Pickaxes/$texture"), // Sprites are in Pickaxes/ subfolder
            handDamage
        )
    }
}
