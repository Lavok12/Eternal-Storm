package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class CosmicBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.cosmic_brick_tile
    override val blockStrength: Int = 400
    override val maxHp: Int = 350
    override val texture: String = AppState.res("cosmic_bricks.png")
    override val drop: DropEntry = SingleDrop(ItemsList.cosmic_brick_tile)
    override val tags = setOf(BlockTags.SOLID)
}
