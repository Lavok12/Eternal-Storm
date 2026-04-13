package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.WallContext

class MapSystem(
    var mapController: MapController,
) {
    val width: Int = mapController.dimension.width
    val height: Int = mapController.dimension.height
    private val size = width * height

    private var tiles: Array<AbstractTileType?> = arrayOfNulls(size)
    private var tilesHp: IntArray = IntArray(size)

    private var walls: Array<AbstractWallType?> = arrayOfNulls(size)
    private var wallsHp: IntArray = IntArray(size)

    private fun getIndex(x: Int, y: Int): Int = y * width + x

    fun isInside(x: Int, y: Int): Boolean =
        x in 0 until width && y in 0 until height

    // --------------------------------------------------------
    // TILES
    // --------------------------------------------------------

    fun getTileType(x: Int, y: Int): AbstractTileType? =
        if (isInside(x, y)) tiles[getIndex(x, y)] else null

    fun getTileHp(x: Int, y: Int): Int =
        if (isInside(x, y)) tilesHp[getIndex(x, y)] else 0

    fun getTileContext(x: Int, y: Int): TileContext? {
        if (!isInside(x, y)) return null
        val idx = getIndex(x, y)
        return TileContext(x, y, tilesHp[idx], tiles[idx], mapController.dimension)
    }

    fun containsTile(x: Int, y: Int): Boolean =
        isInside(x, y) && tiles[getIndex(x, y)] != null

    fun setTile(tileContext: TileContext) {
        val x = tileContext.positionX
        val y = tileContext.positionY
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        tiles[idx] = tileContext.tileType
        tilesHp[idx] = tileContext.hp
    }

    fun setTileType(x: Int, y: Int, type: AbstractTileType?) {
        if (!isInside(x, y)) return
        tiles[getIndex(x, y)] = type
    }

    fun setTileHp(x: Int, y: Int, hp: Int) {
        if (!isInside(x, y)) return
        tilesHp[getIndex(x, y)] = hp
    }

    fun maxHp(x: Int, y: Int) {
        if (!containsTile(x, y)) return
        val idx = getIndex(x, y)
        tilesHp[idx] = tiles[idx]?.maxHp ?: 0
    }

    fun callPlace(x: Int, y: Int, item: Item) {
        getTileType(x, y)?.place(x, y, item, mapController)
    }

    fun deactivateTile(x: Int, y: Int, reason: Any? = null) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return
        val context = TileContext(x, y, tilesHp[idx], tileType, mapController.dimension)
        tileType.onRemoved(x, y, context, reason)
        tiles[idx] = null
        tilesHp[idx] = 0
    }

    fun damageTile(x: Int, y: Int, damage: Int) {
        if (!containsTile(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return

        val contextBefore = TileContext(x, y, tilesHp[idx], tileType, mapController.dimension)
        tileType.damage(x, y, damage, contextBefore, mapController)
        tilesHp[idx] -= damage

        if (tilesHp[idx] <= 0) {
            val contextBreak = TileContext(x, y, tilesHp[idx], tileType, mapController.dimension)
            tileType.onRemoved(x, y, contextBreak, "absolute_damage")
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }

    fun mineTile(x: Int, y: Int, mineData: MineData) {
        if (!containsTile(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return

        if (mineData.power < tileType.blockStrength) return

        val contextBefore = TileContext(x, y, tilesHp[idx], tileType, mapController.dimension)
        tileType.damage(x, y, mineData.value, contextBefore, mapController)
        tilesHp[idx] -= mineData.value

        if (tilesHp[idx] <= 0) {
            val contextBreak = TileContext(x, y, tilesHp[idx], tileType, mapController.dimension)
            tileType.onMined(x, y, mineData, contextBreak, mapController)
            tileType.onRemoved(x, y, contextBreak, mineData)
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }

    // --------------------------------------------------------
    // WALLS
    // --------------------------------------------------------

    fun getWallType(x: Int, y: Int): AbstractWallType? =
        if (isInside(x, y)) walls[getIndex(x, y)] else null

    fun getWallHp(x: Int, y: Int): Int =
        if (isInside(x, y)) wallsHp[getIndex(x, y)] else 0

    fun getWallContext(x: Int, y: Int): WallContext? {
        if (!isInside(x, y)) return null
        val idx = getIndex(x, y)
        return WallContext(x, y, wallsHp[idx], walls[idx], mapController.dimension)
    }

    fun containsWall(x: Int, y: Int): Boolean =
        isInside(x, y) && walls[getIndex(x, y)] != null

    fun setWall(wallContext: WallContext) {
        val x = wallContext.positionX
        val y = wallContext.positionY
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        walls[idx] = wallContext.wallType
        wallsHp[idx] = wallContext.hp
    }

    fun setWallType(x: Int, y: Int, type: AbstractWallType?) {
        if (!isInside(x, y)) return
        walls[getIndex(x, y)] = type
    }

    fun setWallHp(x: Int, y: Int, hp: Int) {
        if (!isInside(x, y)) return
        wallsHp[getIndex(x, y)] = hp
    }

    fun maxHpWall(x: Int, y: Int) {
        if (!containsWall(x, y)) return
        val idx = getIndex(x, y)
        wallsHp[idx] = walls[idx]?.maxHp ?: 0
    }

    fun callPlaceWall(x: Int, y: Int, item: Item) {
        getWallType(x, y)?.place(x, y, item, mapController)
    }

    fun deactivateWall(x: Int, y: Int, reason: Any? = null) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return
        val context = WallContext(x, y, wallsHp[idx], wallType, mapController.dimension)
        wallType.onRemoved(x, y, context, reason)
        walls[idx] = null
        wallsHp[idx] = 0
    }

    fun damageWall(x: Int, y: Int, damage: Int) {
        if (!containsWall(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return

        val contextBefore = WallContext(x, y, wallsHp[idx], wallType, mapController.dimension)
        wallType.damage(x, y, damage, contextBefore, mapController)
        wallsHp[idx] -= damage

        if (wallsHp[idx] <= 0) {
            val contextBreak = WallContext(x, y, wallsHp[idx], wallType, mapController.dimension)
            wallType.onRemoved(x, y, contextBreak, "absolute_damage")
            walls[idx] = null
            wallsHp[idx] = 0
        }
    }

    fun mineWall(x: Int, y: Int, mineData: MineData) {
        if (!containsWall(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return

        if (mineData.power < wallType.blockStrength) return

        val contextBefore = WallContext(x, y, wallsHp[idx], wallType, mapController.dimension)
        wallType.damage(x, y, mineData.value, contextBefore, mapController)
        wallsHp[idx] -= mineData.value

        if (wallsHp[idx] <= 0) {
            val contextBreak = WallContext(x, y, wallsHp[idx], wallType, mapController.dimension)
            wallType.onMined(x, y, mineData, contextBreak, mapController)
            wallType.onRemoved(x, y, contextBreak, mineData)
            walls[idx] = null
            wallsHp[idx] = 0
        }
    }
}