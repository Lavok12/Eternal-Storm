package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.Items.Items.TeleporterItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Items.Other.Item

open class TeleporterItemType : AbstractItemType() {
    override val tag: String = ItemsList.teleporter
    override val texture: String = AppState.res("AxeSwingTrace.png")
    override val tags = setOf(ItemTags.TOOL)

    override fun createItem(gameCycle: GameCycle): Item {
        return TeleporterItem(this, gameCycle)
    }
}
