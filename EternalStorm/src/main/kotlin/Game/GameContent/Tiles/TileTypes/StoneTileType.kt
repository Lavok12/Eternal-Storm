package la.vok.Game.GameContent.Tiles.TileTypes

import la.vok.Game.GameContent.Tiles.Tiles.StoneTile
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.State.AppState

class StoneTileType() : AbstractTileType() {
    override val tag: String = AppState.addNamespace("stone")
    override val blockStrength: Int = 50
    override val hp: Int = 0

    override fun createTile() : Tile {
        return StoneTile(this)
    }
}