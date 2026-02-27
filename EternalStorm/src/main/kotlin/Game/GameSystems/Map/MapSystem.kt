package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState

class MapSystem(
    var mapController: MapController,
) {
    val width: Int = 100
    val height: Int = 100

    private var mapTile: Array<Array<MapTile?>> = Array(width) { arrayOfNulls<MapTile>(height) }


    fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    fun getMapTile(x: Int, y: Int): MapTile? {
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
            mapTile[x][y]!!.tile = tile
        } else {
            AppState.logger.error("Off-map placement: $x, $y")
        }
    }
    fun deactivateTile(x: Int, y: Int) {
        AppState.logger.debug("DeactivateTile: $x, $y")
        if (isInside(x, y)) {
            mapTile[x][y]!!.deactivate()
            mapTile[x][y]!!.tile = null
        }
    }

    init {
        for (x in 0 until width) {
            for (y in 0 until height) {
                mapTile[x][y] = MapTile()
            }
        }
    }
}