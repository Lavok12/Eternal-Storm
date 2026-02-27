package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.LPoint

class MapTile() {
    fun containsTile() : Boolean {
        return tile != null
    }
    var tile: Tile? = null

    fun deactivate() {
        tile = null
    }
}