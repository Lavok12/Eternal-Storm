package Game.GameContent.TileTypes

import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.Game.GameContent.Tiles.Tiles.GrassTile
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.State.AppState

class GrassTileType() : AbstractTileType() {
    override val tag: String = AppState.addNamespace("grass")
    override val blockStrength: Int = 50
    override val hp: Int = 0

    override fun createTile() : Tile {
        return GrassTile(this)
    }
}