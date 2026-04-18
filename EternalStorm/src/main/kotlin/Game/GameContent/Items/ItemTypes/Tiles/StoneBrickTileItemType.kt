package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class StoneBrickTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.stone_brick_tile
    override val texture: String = AppState.res("stone_bricks.png")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.stone_brick_tile)
}
