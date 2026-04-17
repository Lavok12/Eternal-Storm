package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList

class MagicalBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.magical_brick_tile
    override val blockStrength: Int = 150
    override val maxHp: Int = 120
    override val texture: String = "magical_bricks.png"
    override val drop: DropEntry = SingleDrop(ItemsList.magical_brick_tile)
    override val tags = setOf(BlockTags.SOLID)
}
