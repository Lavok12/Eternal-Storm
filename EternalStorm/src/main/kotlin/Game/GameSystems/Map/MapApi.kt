package la.vok.Game.GameContent.Map

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class MapApi(var mapController: MapController) {
    val gameCycle: GameCycle = mapController.gameCycle
    val gameController: GameController get() = gameCycle.gameController
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
        return objectRegistration.tiles[tag]!!.createTile(gameController)
    }
    fun getRegisteredTileType(tag: String) : AbstractTileType {
        return objectRegistration.tiles[tag]!!
    }
    fun getRegisteredTileByType(type: AbstractTileType) : Tile {
        return type.createTile(gameController)
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

    @Suppress("NOTHING_TO_INLINE")
    inline fun raycast(
        start: Vec2,
        end: Vec2,
        maxLength: Float,
        filter: (MapTile) -> Boolean
    ): MapTile? {

        val dir = (end - start)
        val length = kotlin.math.sqrt(dir.x * dir.x + dir.y * dir.y)

        if (length == 0f) return null

        val normDir = dir * (1f / length)
        val rayLength = kotlin.math.min(length, maxLength)

        var currentPos = start
        var traveled = 0f

        // DDA шаг
        val step = 0.1f   // можно уменьшить если нужна точность

        while (traveled <= rayLength) {

            val point = getPointFromPos(currentPos)

            if (isInsideMap(point)) {
                val mapTile = getMapTileFromMap(point)
                if (mapTile != null && filter(mapTile)) {
                    return mapTile
                }
            }

            currentPos = currentPos + normDir * step
            traveled += step
        }

        return null
    }

    fun raycastGrid(
        start: Vec2,
        end: Vec2,
        maxLength: Float,
        filter: (MapTile) -> Boolean
    ): MapTile? {

        val dir = end - start
        val fullLength = kotlin.math.sqrt(dir.x * dir.x + dir.y * dir.y)
        if (fullLength == 0f) return null

        val length = kotlin.math.min(fullLength, maxLength)
        val dx = dir.x / fullLength
        val dy = dir.y / fullLength

        var x = kotlin.math.floor(start.x).toInt()
        var y = kotlin.math.floor(start.y).toInt()

        val stepX = if (dx > 0) 1 else -1
        val stepY = if (dy > 0) 1 else -1

        val tDeltaX = if (dx != 0f) kotlin.math.abs(1f / dx) else Float.POSITIVE_INFINITY
        val tDeltaY = if (dy != 0f) kotlin.math.abs(1f / dy) else Float.POSITIVE_INFINITY

        val nextBoundaryX = if (dx > 0) x + 1f else x.toFloat()
        val nextBoundaryY = if (dy > 0) y + 1f else y.toFloat()

        var tMaxX = if (dx != 0f) (nextBoundaryX - start.x) / dx else Float.POSITIVE_INFINITY
        var tMaxY = if (dy != 0f) (nextBoundaryY - start.y) / dy else Float.POSITIVE_INFINITY

        var traveled = 0f

        while (traveled <= length) {

            val point = LPoint(x, y)

            if (isInsideMap(point)) {
                val tile = getMapTileFromMap(point)
                if (tile != null && filter(tile)) {
                    return tile
                }
            }

            if (tMaxX < tMaxY) {
                x += stepX
                traveled = tMaxX
                tMaxX += tDeltaX
            } else {
                y += stepY
                traveled = tMaxY
                tMaxY += tDeltaY
            }
        }

        return null
    }
}