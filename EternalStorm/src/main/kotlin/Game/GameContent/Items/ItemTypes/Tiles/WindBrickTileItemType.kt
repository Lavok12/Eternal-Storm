package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class WindBrickTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.wind_brick_tile
    override val texture = "wind_bricks.png"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.wind_brick_tile)
}
