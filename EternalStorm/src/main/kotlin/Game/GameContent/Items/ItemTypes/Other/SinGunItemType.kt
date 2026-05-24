package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Items.Items.MostCommonStickItem
import la.vok.Game.GameContent.Items.Items.SinGunItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class SinGunItemType : AbstractItemType() {
    override val tag: String = ItemsList.sin_gun
    override val texture: String = AppState.res("sin_gun.png")

    override val tags = setOf(ItemTags.WEAPON)

    override val maxInStack: Int = 1

    init {
        renderConfig.worldDelta = 0 v -0.23f
    }

    override fun createItem(gameCycle: GameCycle) : Item {
        return SinGunItem(this, gameCycle)
    }
}