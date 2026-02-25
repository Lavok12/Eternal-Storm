package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.LPoint

class MapTile(var mapSystem: MapSystem, var point: LPoint) {
    val mapApi: MapApi get() = mapSystem.mapController.mapApi
    var isActive = false
        private set
    var tile: Tile? = null

    fun activate() {
        isActive = true
    }
    fun deactivate() {
        isActive = false
    }
}