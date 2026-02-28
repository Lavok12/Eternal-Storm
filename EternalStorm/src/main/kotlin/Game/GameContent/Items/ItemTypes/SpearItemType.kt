package la.vok.Game.GameContent.Items.ItemTypes

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Items.SpearItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.State.AppState

class SpearItemType : AbstractItemType() {
    override val tag: String = ItemsList.spear
    override val sprite = "spear.png"

    override fun createItem(gameCycle: GameCycle) : Item {
        return SpearItem(this, gameCycle)
    }
}