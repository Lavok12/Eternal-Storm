package la.vok.Game.GameContent.Map

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class MapApi(var mapController: MapController) {
    val gameController: GameController get() = mapController.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    fun getTilePos(point: LPoint) : Vec2 {
        return point.x v  point.y
    }
    fun getTileSize() : Vec2 {
        return 1f v 1f
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {

        val centerX = pos.x + 0.5f
        val centerY = pos.y + 0.5f

        val rx = kotlin.math.floor(centerX).toInt()
        val ry = kotlin.math.floor(centerY).toInt()

        return LPoint(rx, ry)
    }

    fun getRegisteredTile(tag: String) : Tile {
        return objectRegistration.tiles[tag]!!.createTile().apply { mapSystem = mapController.mapSystem }
    }
    fun getRegisteredTileType(tag: String) : AbstractTileType {
        return objectRegistration.tiles[tag]!!
    }
    fun getRegisteredTileByType(type: AbstractTileType) : Tile {
        return type.createTile().apply { mapSystem = mapController.mapSystem }
    }

    fun isInsideMap(point: LPoint): Boolean {
        return mapController.mapSystem.isInside(point.x, point.y)
    }
    fun getTileFromMap(point: LPoint) : Tile? {
        return mapController.mapSystem.getMapTile(point.x, point.y)?.tile
    }
    fun getMapTileFromMap(point: LPoint) : MapTile? {
        return mapController.mapSystem.getMapTile(point.x, point.y)
    }
    fun getTileFromMap(x: Int, y: Int) : Tile? {
        return mapController.mapSystem.getMapTile(x, y)?.tile
    }
    fun getMapTileFromMap(x: Int, y: Int) : MapTile? {
        return mapController.mapSystem.getMapTile(x, y)
    }
    fun setTile(tile: Tile, x: Int, y: Int) {
        mapController.mapSystem.setTile(x, y, tile)
    }
    fun placeTile(tile: Tile, x: Int, y: Int) {
        setTile(tile, x, y)
    }
    fun deleteTile(x: Int, y: Int) {
        mapController.mapSystem.deactivateTile(x, y)
    }
    fun setTile(tile: Tile, point: LPoint) {
        mapController.mapSystem.setTile(point.x, point.y, tile)
    }
    fun placeTile(tile: Tile, point: LPoint) {
        setTile(tile, point)
    }
    fun deleteTile(point: LPoint) {
        mapController.mapSystem.deactivateTile(point.x, point.y)
    }
}