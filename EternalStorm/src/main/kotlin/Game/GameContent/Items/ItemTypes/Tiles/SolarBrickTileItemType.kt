package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class SolarBrickTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.solar_brick_tile
    override val texture = "solar_bricks.png"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.solar_brick_tile)
}
