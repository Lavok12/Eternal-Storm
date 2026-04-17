package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList

class WoodenBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.wooden_brick_tile
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = "wooden_bricks.png"
    override val drop: DropEntry = SingleDrop(ItemsList.wooden_brick_tile)
    override val tags = setOf(BlockTags.WOOD, BlockTags.SOLID)
}
