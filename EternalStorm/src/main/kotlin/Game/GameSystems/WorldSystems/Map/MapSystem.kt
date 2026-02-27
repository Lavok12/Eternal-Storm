package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileContext

class MapSystem(
    var mapController: MapController,
) {
    val width: Int = 100
    val height: Int = 100
    private val size = width * height

    private var tiles: Array<AbstractTileType?> = arrayOfNulls(size)
    private var tilesHp: IntArray = IntArray(size)

    private fun getIndex(x: Int, y: Int): Int = y * width + x

    fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    fun getTileType(x: Int, y: Int): AbstractTileType? {
        return if (isInside(x, y)) tiles[getIndex(x, y)] else null
    }

    fun getTileHp(x: Int, y: Int): Int {
        return if (isInside(x, y)) tilesHp[getIndex(x, y)] else 0
    }

    fun getTile(x: Int, y: Int): TileContext? {
        if (!isInside(x, y)) return null
        val idx = getIndex(x, y)
        return TileContext(x, y, tilesHp[idx], tiles[idx])
    }

    fun setTile(tileContext: TileContext) {
        val x = tileContext.positionX
        val y = tileContext.positionY
        if (isInside(x, y)) {
            val idx = getIndex(x, y)
            tiles[idx] = tileContext.tileType
            tilesHp[idx] = tileContext.hp
        }
    }

    fun setTileType(x: Int, y: Int, abstractTileType: AbstractTileType?) {
        if (isInside(x, y)) {
            tiles[getIndex(x, y)] = abstractTileType
        }
    }

    fun setTileHp(x: Int, y: Int, hp: Int) {
        if (isInside(x, y)) {
            tilesHp[getIndex(x, y)] = hp
        }
    }

    fun deactivateTile(x: Int, y: Int) {
        if (isInside(x, y)) {
            val idx = getIndex(x, y)
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }

    fun containsTile(x: Int, y: Int): Boolean {
        return isInside(x, y) && tiles[getIndex(x, y)] != null
    }
}