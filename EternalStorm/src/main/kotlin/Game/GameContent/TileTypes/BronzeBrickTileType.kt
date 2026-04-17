package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList

class BronzeBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.bronze_brick_tile
    override val blockStrength: Int = 70
    override val maxHp: Int = 60
    override val texture: String = "bronze_bricks.png"
    override val drop: DropEntry = SingleDrop(ItemsList.bronze_brick_tile)
    override val tags = setOf(BlockTags.SOLID)
}
