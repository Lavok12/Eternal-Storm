package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState

class MapSystem(
    var mapController: MapController,
) {
    val width: Int = 100
    val height: Int = 100

    private var mapTile: Array<Array<Tile?>> = Array(width) { arrayOfNulls<Tile?>(height) }

    fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    fun getTile(x: Int, y: Int): Tile? {
        AppState.logger.trace("GetTile: $x, $y")
        return if (isInside(x, y)) {
            mapTile[x][y]
        } else {
            null
        }
    }
    fun setTile(x: Int, y: Int, tile: Tile?) {
        AppState.logger.trace("SetTile: $x, $y")
        if (isInside(x, y)) {
            mapTile[x][y] = tile
        } else {
            AppState.logger.error("Off-map placement: $x, $y")
        }
    }
    fun deactivateTile(x: Int, y: Int) {
        AppState.logger.debug("DeactivateTile: $x, $y")
        if (isInside(x, y)) {
            mapTile[x][y] = null
        }
    }
    fun containsTile(x: Int, y: Int) : Boolean {
        return mapTile[x][y] != null
    }

    init {
        for (x in 0 until width) {
            for (y in 0 until height) {
                mapTile[x][y] = null
            }
        }
    }
}