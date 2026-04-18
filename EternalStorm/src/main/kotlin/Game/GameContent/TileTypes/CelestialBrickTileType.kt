package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class CelestialBrickTileType() : AbstractTileType() {
    override val tag: String = TilesList.celestial_brick_tile
    override val blockStrength: Int = 250
    override val maxHp: Int = 200
    override val texture: String = AppState.res("celestial_bricks.png")
    override val drop: DropEntry = SingleDrop(ItemsList.celestial_brick_tile)
    override val tags = setOf(BlockTags.SOLID)
}
