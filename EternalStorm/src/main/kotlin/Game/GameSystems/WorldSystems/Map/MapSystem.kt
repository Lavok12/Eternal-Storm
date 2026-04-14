package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData

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

    fun containsTile(x: Int, y: Int): Boolean =
        isInside(x, y) && tiles[getIndex(x, y)] != null

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
        val tileType = getTileType(x, y) ?: return
        
        if (tileType.isDummy) {
            return deactivateTile(x + tileType.masterOffset.x, y + tileType.masterOffset.y, reason)
        }

        tileType.onRemoved(x, y, mapController.dimension, mapController, reason)
        removeMultiTileParts(x, y, tileType)
        val idx = getIndex(x, y)
        tiles[idx] = null
        tilesHp[idx] = 0
    }

    fun damageTile(x: Int, y: Int, damage: Int) {
        if (!containsTile(x, y)) return
        val tileType = getTileType(x, y) ?: return

        if (tileType.isDummy) {
            return damageTile(x + tileType.masterOffset.x, y + tileType.masterOffset.y, damage)
        }

        tileType.damage(x, y, damage, mapController.dimension, mapController)
        val idx = getIndex(x, y)
        tilesHp[idx] -= damage

        if (tilesHp[idx] <= 0) {
            tileType.onRemoved(x, y, mapController.dimension, mapController, "absolute_damage")
            removeMultiTileParts(x, y, tileType)
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }

    fun mineTile(x: Int, y: Int, mineData: MineData) {
        if (!containsTile(x, y)) return
        val tileType = getTileType(x, y) ?: return

        if (tileType.isDummy) {
            return mineTile(x + tileType.masterOffset.x, y + tileType.masterOffset.y, mineData)
        }

        if (mineData.power < tileType.blockStrength) return

        tileType.damage(x, y, mineData.value, mapController.dimension, mapController)
        val idx = getIndex(x, y)
        tilesHp[idx] -= mineData.value

        if (tilesHp[idx] <= 0) {
            tileType.onMined(x, y, mineData, mapController.dimension, mapController)
            tileType.onRemoved(x, y, mapController.dimension, mapController, mineData)
            removeMultiTileParts(x, y, tileType)
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }

    private fun removeMultiTileParts(x: Int, y: Int, tile: AbstractTileType) {
        if (tile.width > 1 || tile.height > 1) {
            for (dx in 0 until tile.width) {
                for (dy in 0 until tile.height) {
                    if (dx == 0 && dy == 0) continue
                    val nx = x + dx
                    val ny = y + dy
                    if (isInside(nx, ny)) {
                        val part = tiles[getIndex(nx, ny)]
                        if (part != null && part.isDummy && part.masterOffset.x == -dx && part.masterOffset.y == -dy) {
                            tiles[getIndex(nx, ny)] = null
                            tilesHp[getIndex(nx, ny)] = 0
                        }
                    }
                }
            }
        }
    }

    // --------------------------------------------------------
    // WALLS
    // --------------------------------------------------------

    fun getWallType(x: Int, y: Int): AbstractWallType? =
        if (isInside(x, y)) walls[getIndex(x, y)] else null

    fun getWallHp(x: Int, y: Int): Int =
        if (isInside(x, y)) wallsHp[getIndex(x, y)] else 0

    fun containsWall(x: Int, y: Int): Boolean =
        isInside(x, y) && walls[getIndex(x, y)] != null

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
        wallType.onRemoved(x, y, mapController.dimension, mapController, reason)
        walls[idx] = null
        wallsHp[idx] = 0
    }

    fun damageWall(x: Int, y: Int, damage: Int) {
        if (!containsWall(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return

        wallType.damage(x, y, damage, mapController.dimension, mapController)
        wallsHp[idx] -= damage

        if (wallsHp[idx] <= 0) {
            wallType.onRemoved(x, y, mapController.dimension, mapController, "absolute_damage")
            walls[idx] = null
            wallsHp[idx] = 0
        }
    }

    fun mineWall(x: Int, y: Int, mineData: MineData) {
        if (!containsWall(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return

        if (mineData.power < wallType.blockStrength) return

        wallType.damage(x, y, mineData.value, mapController.dimension, mapController)
        wallsHp[idx] -= mineData.value

        if (wallsHp[idx] <= 0) {
            wallType.onMined(x, y, mineData, mapController.dimension, mapController)
            wallType.onRemoved(x, y, mapController.dimension, mapController, mineData)
            walls[idx] = null
            wallsHp[idx] = 0
        }
    }
}