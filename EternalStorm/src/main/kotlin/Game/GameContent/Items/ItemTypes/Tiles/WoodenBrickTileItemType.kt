package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class WoodenBrickTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.wooden_brick_tile
    override val texture = "wooden_bricks.png"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.wooden_brick_tile)
}
