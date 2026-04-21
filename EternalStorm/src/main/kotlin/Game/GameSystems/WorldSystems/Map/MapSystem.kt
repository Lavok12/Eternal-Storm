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

    private val tiles: Array<AbstractTileType?> = arrayOfNulls(size)
    private val tilesHp: IntArray = IntArray(size)

    private val walls: Array<AbstractWallType?> = arrayOfNulls(size)
    private val wallsHp: IntArray = IntArray(size)

    private val tileDataMap = HashMap<Long, la.vok.Game.GameContent.TileData.AbstractTileData>()
    private val wallDataMap = HashMap<Long, la.vok.Game.GameContent.WallData.AbstractWallData>()

    private val logicalUpdatables = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val secondUpdatables = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val minuteUpdatables = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val physicsUpdatables = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val renderUpdatables = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private fun getIndex(x: Int, y: Int): Int = y * width + x
    private fun coordToKey(x: Int, y: Int): Long = (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)

    private fun registerBlockData(data: la.vok.Game.GameSystems.WorldSystems.Map.IBlockData) {
        if (data.wantsLogicalUpdate) logicalUpdatables.add(data)
        if (data.wantsSecondUpdate) secondUpdatables.add(data)
        if (data.wantsMinuteUpdate) minuteUpdatables.add(data)
        if (data.wantsPhysicsUpdate) physicsUpdatables.add(data)
        if (data.wantsRenderUpdate) renderUpdatables.add(data)
    }

    private fun unregisterBlockData(data: la.vok.Game.GameSystems.WorldSystems.Map.IBlockData) {
        logicalUpdatables.remove(data)
        secondUpdatables.remove(data)
        minuteUpdatables.remove(data)
        physicsUpdatables.remove(data)
        renderUpdatables.remove(data)
    }

    fun dispatchLogicalUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        val list = logicalUpdatables.toTypedArray()
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> list.forEach { it.preLogicalTick() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> list.forEach { it.logicalTick() }
            la.vok.Game.GameController.UpdatePhase.POST -> list.forEach { it.postLogicalTick() }
        }
    }

    fun dispatchSecondUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        val list = secondUpdatables.toTypedArray()
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> list.forEach { it.preSecondUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> list.forEach { it.secondUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> list.forEach { it.postSecondUpdate() }
        }
    }

    fun dispatchMinuteUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        val list = minuteUpdatables.toTypedArray()
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> list.forEach { it.preMinuteUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> list.forEach { it.minuteUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> list.forEach { it.postMinuteUpdate() }
        }
    }

    fun dispatchPhysicsUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        val list = physicsUpdatables.toTypedArray()
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> list.forEach { it.prePhysicsUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> list.forEach { it.physicsUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> list.forEach { it.postPhysicsUpdate() }
        }
    }

    fun dispatchRenderUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        val list = renderUpdatables.toTypedArray()
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> list.forEach { it.preRenderUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> list.forEach { it.renderUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> list.forEach { it.postRenderUpdate() }
        }
    }

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
        
        // --- TileData Lifecycle ---
        removeTileData(x, y)
        
        if (type != null && !type.isDummy) {
            val data = type.createTileData(x, y, mapController.dimension)
            if (data != null) {
                setTileData(x, y, data)
            }
        }
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

    fun callPlace(x: Int, y: Int, item: la.vok.Game.GameContent.Items.Other.Item) {
        getTileType(x, y)?.place(x, y, item, mapController)
    }

    fun getTileData(x: Int, y: Int): la.vok.Game.GameContent.TileData.AbstractTileData? = tileDataMap[coordToKey(x, y)]

    fun setTileData(x: Int, y: Int, data: la.vok.Game.GameContent.TileData.AbstractTileData?) {
        val key = coordToKey(x, y)
        val old = tileDataMap.remove(key)
        if (old != null) unregisterBlockData(old)
        
        if (data != null) {
            tileDataMap[key] = data
            registerBlockData(data)
        }
    }

    fun removeTileData(x: Int, y: Int) {
        val key = coordToKey(x, y)
        val old = tileDataMap.remove(key)
        if (old != null) unregisterBlockData(old)
    }

    fun deactivateTile(x: Int, y: Int, reason: Any? = null) {
        if (!isInside(x, y)) return
        val tileType = getTileType(x, y) ?: return
        
        tileType.onRemoved(x, y, mapController.dimension, mapController, reason)
        removeMultiTileParts(x, y, tileType)
        val idx = getIndex(x, y)
        tiles[idx] = null
        tilesHp[idx] = 0
        getTileData(x, y)?.onRemoved()
        removeTileData(x, y)
    }

    fun damageTile(x: Int, y: Int, damage: Int) {
        if (!containsTile(x, y)) return
        val tileType = getTileType(x, y) ?: return

        tileType.damage(x, y, damage, mapController.dimension, mapController)
        val idx = getIndex(x, y)
        tilesHp[idx] -= damage

        if (tilesHp[idx] <= 0) {
            tileType.onRemoved(x, y, mapController.dimension, mapController, "absolute_damage")
            removeMultiTileParts(x, y, tileType)
            tiles[idx] = null
            tilesHp[idx] = 0
            val data = getTileData(x, y)
            data?.onDestroyed()
            data?.onRemoved()
            removeTileData(x, y)
        }
    }

    fun mineTile(x: Int, y: Int, mineData: MineData) {
        if (!containsTile(x, y)) return
        val tileType = getTileType(x, y) ?: return

        if (mineData.power < tileType.blockStrength) return

        tileType.damage(x, y, mineData.value, mapController.dimension, mapController)
        tileType.mine(x, y, mineData, mapController.dimension, mapController)

        val idx = getIndex(x, y)
        tilesHp[idx] -= mineData.value

        if (tilesHp[idx] <= 0) {
            tileType.onMined(x, y, mineData, mapController.dimension, mapController)
            tileType.onRemoved(x, y, mapController.dimension, mapController, mineData)
            removeMultiTileParts(x, y, tileType)
            tiles[idx] = null
            tilesHp[idx] = 0
            val data = getTileData(x, y)
            data?.onDestroyed()
            data?.onRemoved()
            removeTileData(x, y)
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

    fun copyTile(fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (!isInside(fromX, fromY) || !isInside(toX, toY)) return
        val type = getTileType(fromX, fromY) ?: return
        val hp = getTileHp(fromX, fromY)
        
        deactivateTile(toX, toY, "replaced_by_copy")
        
        val toIdx = getIndex(toX, toY)
        tiles[toIdx] = type
        tilesHp[toIdx] = hp
        
        // Create fresh data if needed (don't call place as per request)
        if (!type.isDummy) {
            val data = type.createTileData(toX, toY, mapController.dimension)
            if (data != null) {
                tileDataMap[coordToKey(toX, toY)] = data
            }
        }
        
        type.onPositionChanged(fromX, fromY, toX, toY, mapController.dimension)
    }

    fun swapTiles(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (!isInside(x1, y1) || !isInside(x2, y2)) return
        val idx1 = getIndex(x1, y1)
        val idx2 = getIndex(x2, y2)
        
        val type1 = tiles[idx1]
        val hp1 = tilesHp[idx1]
        val data1 = getTileData(x1, y1)
        
        val type2 = tiles[idx2]
        val hp2 = tilesHp[idx2]
        val data2 = getTileData(x2, y2)
        
        tiles[idx1] = type2
        tilesHp[idx1] = hp2
        
        tiles[idx2] = type1
        tilesHp[idx2] = hp1
        
        // Update Data positions
        tileDataMap.remove(coordToKey(x1, y1))
        tileDataMap.remove(coordToKey(x2, y2))
        
        if (data1 != null) {
            data1.x = x2
            data1.y = y2
            tileDataMap[coordToKey(x2, y2)] = data1
            data1.onPositionChanged(x1, y1, x2, y2)
        }
        if (data2 != null) {
            data2.x = x1
            data2.y = y1
            tileDataMap[coordToKey(x1, y1)] = data2
            data2.onPositionChanged(x2, y2, x1, y1)
        }
        
        type1?.onPositionChanged(x1, y1, x2, y2, mapController.dimension)
        type2?.onPositionChanged(x2, y2, x1, y1, mapController.dimension)
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

        // --- WallData Lifecycle ---
        removeWallData(x, y)

        if (type != null) {
            val data = type.createWallData(x, y, mapController.dimension)
            if (data != null) {
                setWallData(x, y, data)
            }
        }
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

    fun getWallData(x: Int, y: Int): la.vok.Game.GameContent.WallData.AbstractWallData? = wallDataMap[coordToKey(x, y)]

    fun setWallData(x: Int, y: Int, data: la.vok.Game.GameContent.WallData.AbstractWallData?) {
        val key = coordToKey(x, y)
        val old = wallDataMap.remove(key)
        if (old != null) unregisterBlockData(old)

        if (data != null) {
            wallDataMap[key] = data
            registerBlockData(data)
        }
    }

    fun removeWallData(x: Int, y: Int) {
        val key = coordToKey(x, y)
        val old = wallDataMap.remove(key)
        if (old != null) unregisterBlockData(old)
    }

    fun callPlaceWall(x: Int, y: Int, item: Item) {
        getWallType(x, y)?.place(x, y, item, mapController)
    }

    fun copyWall(fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (!isInside(fromX, fromY) || !isInside(toX, toY)) return
        val type = getWallType(fromX, fromY) ?: return
        val hp = getWallHp(fromX, fromY)
        
        deactivateWall(toX, toY, "replaced_by_copy")
        
        val toIdx = getIndex(toX, toY)
        walls[toIdx] = type
        wallsHp[toIdx] = hp
        
        // Data Copy
        val data = type.createWallData(toX, toY, mapController.dimension)
        if (data != null) {
            setWallData(toX, toY, data)
        }
        
        type.onPositionChanged(fromX, fromY, toX, toY, mapController.dimension)
    }

    fun swapWalls(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (!isInside(x1, y1) || !isInside(x2, y2)) return
        val idx1 = getIndex(x1, y1)
        val idx2 = getIndex(x2, y2)
        
        val type1 = walls[idx1]
        val hp1 = wallsHp[idx1]
        val data1 = getWallData(x1, y1)
        
        val type2 = walls[idx2]
        val hp2 = wallsHp[idx2]
        val data2 = getWallData(x2, y2)
        
        walls[idx1] = type2
        wallsHp[idx1] = hp2
        
        walls[idx2] = type1
        wallsHp[idx2] = hp1

        // Update Data
        wallDataMap.remove(coordToKey(x1, y1))
        wallDataMap.remove(coordToKey(x2, y2))

        if (data1 != null) {
            data1.x = x2
            data1.y = y2
            wallDataMap[coordToKey(x2, y2)] = data1
            data1.onPositionChanged(x1, y1, x2, y2)
        }
        if (data2 != null) {
            data2.x = x1
            data2.y = y1
            wallDataMap[coordToKey(x1, y1)] = data2
            data2.onPositionChanged(x2, y2, x1, y1)
        }
        
        type1?.onPositionChanged(x1, y1, x2, y2, mapController.dimension)
        type2?.onPositionChanged(x2, y2, x1, y1, mapController.dimension)
    }

    fun deactivateWall(x: Int, y: Int, reason: Any? = null) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return
        wallType.onRemoved(x, y, mapController.dimension, mapController, reason)
        walls[idx] = null
        wallsHp[idx] = 0
        getWallData(x, y)?.onRemoved()
        removeWallData(x, y)
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
            val data = getWallData(x, y)
            data?.onDestroyed()
            data?.onRemoved()
            removeWallData(x, y)
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
            val data = getWallData(x, y)
            data?.onDestroyed()
            data?.onRemoved()
            removeWallData(x, y)
        }
    }
}