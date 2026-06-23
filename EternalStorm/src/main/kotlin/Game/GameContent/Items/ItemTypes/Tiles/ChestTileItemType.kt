package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class ChestTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.chest
    override val texture: String = AppState.res("chest.png")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.chest_block)
}
