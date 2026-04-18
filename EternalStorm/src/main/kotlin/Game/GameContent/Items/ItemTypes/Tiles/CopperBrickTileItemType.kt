package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class CopperBrickTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.copper_brick_tile
    override val texture: String = AppState.res("copper_bricks.png")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.copper_brick_tile)
}
