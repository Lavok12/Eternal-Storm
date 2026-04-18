package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class CopperBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.copper_brick_tile
    override val blockStrength: Int = 50
    override val maxHp: Int = 40
    override val texture: String = AppState.res("copper_bricks.png")
    override val drop: DropEntry = SingleDrop(ItemsList.copper_brick_tile)
    override val tags = setOf(BlockTags.SOLID)
}
