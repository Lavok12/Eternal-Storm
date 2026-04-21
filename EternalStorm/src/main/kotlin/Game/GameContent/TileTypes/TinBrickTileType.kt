package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.Game.GameContent.TileData.DistributeTileData
import la.vok.State.AppState

class TinBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.tin_brick_tile
    override val blockStrength: Int = 50
    override val maxHp: Int = 40
    override val texture: String = AppState.res("tin_bricks.png")
    override val drop: DropEntry = SingleDrop(ItemsList.tin_brick_tile)
    override val tags = setOf(BlockTags.SOLID)
}
