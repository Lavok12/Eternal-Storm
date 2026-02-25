package la.vok.Game.GameContent.Tiles.TileTypes

import la.vok.Game.GameContent.Tiles.Tiles.Tile

abstract class AbstractTileType  {
    open val tag: String = ""
    open val blockStrength: Int = 0
    open val hp: Int = 0

    open fun createTile() : Tile {
        return Tile(this)
    }
}