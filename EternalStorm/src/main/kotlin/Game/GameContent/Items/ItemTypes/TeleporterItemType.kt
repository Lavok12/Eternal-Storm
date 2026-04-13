package la.vok.Game.GameContent.Items.ItemTypes

import la.vok.Game.GameContent.Items.Items.TeleporterItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Items.Other.Item

open class TeleporterItemType : AbstractItemType() {
    override val tag: String = ItemsList.teleporter
    override val texture = "t4.png"

    override fun createItem(gameCycle: GameCycle): Item {
        return TeleporterItem(this, gameCycle)
    }
}
