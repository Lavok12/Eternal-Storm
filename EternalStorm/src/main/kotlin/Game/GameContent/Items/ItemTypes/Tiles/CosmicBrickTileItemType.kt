package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class CosmicBrickTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.cosmic_brick_tile
    override val texture = "cosmic_bricks.png"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.cosmic_brick_tile)
}
