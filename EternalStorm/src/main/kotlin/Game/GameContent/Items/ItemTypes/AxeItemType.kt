package la.vok.Game.GameContent.Items.ItemTypes

import la.vok.Game.GameContent.Items.Items.AxeItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.State.AppState

class AxeItemType : AbstractItemType() {
    override val tag: String = ItemsList.axe
    override val sprite = "axe.png"
    override val maxInStack: Int
        get() = 1

    override fun createItem(gameCycle: GameCycle) : Item {
        return AxeItem(this, gameCycle)
    }
}