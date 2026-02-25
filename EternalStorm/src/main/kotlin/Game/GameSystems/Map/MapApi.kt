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
        val fx = pos.x
        val fy = pos.y

        val ix = fx.toInt()
        val rx = if (fx < ix.toFloat()) ix - 1 else ix

        val iy = fy.toInt()
        val ry = if (fy < iy.toFloat()) iy - 1 else iy

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