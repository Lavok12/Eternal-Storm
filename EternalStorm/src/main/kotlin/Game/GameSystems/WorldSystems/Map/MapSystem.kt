package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.LavokLibrary.Vectors.p

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

    private val registrationBuffer = mutableListOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val unregistrationBuffer = mutableListOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    // --- Granular Subscriptions ---
    private val preLogicalSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val mainLogicalSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val postLogicalSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private val preSecondSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val mainSecondSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val postSecondSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private val preMinuteSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val mainMinuteSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val postMinuteSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private val prePhysicsSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val mainPhysicsSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val postPhysicsSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private val preRenderSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val mainRenderSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private val postRenderSet = mutableSetOf<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    // --- Cached Arrays (Zero-Allocation Tick) ---
    private var preLogicalArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var mainLogicalArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var postLogicalArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private var preSecondArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var mainSecondArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var postSecondArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private var preMinuteArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var mainMinuteArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var postMinuteArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private var prePhysicsArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var mainPhysicsArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var postPhysicsArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private var preRenderArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var mainRenderArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()
    private var postRenderArr = emptyArray<la.vok.Game.GameSystems.WorldSystems.Map.IBlockData>()

    private fun getIndex(x: Int, y: Int): Int = y * width + x
    private fun coordToKey(x: Int, y: Int): Long = (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)

    private fun registerBlockData(data: la.vok.Game.GameSystems.WorldSystems.Map.IBlockData) {
        data.disabled = false
        registrationBuffer.add(data)
    }

    private fun unregisterBlockData(data: la.vok.Game.GameSystems.WorldSystems.Map.IBlockData) {
        data.disabled = true
        unregistrationBuffer.add(data)
    }

    fun applyBufferedUpdates() {
        var changed = false
        if (unregistrationBuffer.isNotEmpty()) {
            unregistrationBuffer.forEach { data ->
                preLogicalSet.remove(data)
                mainLogicalSet.remove(data)
                postLogicalSet.remove(data)

                preSecondSet.remove(data)
                mainSecondSet.remove(data)
                postSecondSet.remove(data)

                preMinuteSet.remove(data)
                mainMinuteSet.remove(data)
                postMinuteSet.remove(data)

                prePhysicsSet.remove(data)
                mainPhysicsSet.remove(data)
                postPhysicsSet.remove(data)

                preRenderSet.remove(data)
                mainRenderSet.remove(data)
                postRenderSet.remove(data)
            }
            unregistrationBuffer.clear()
            changed = true
        }

        if (registrationBuffer.isNotEmpty()) {
            registrationBuffer.forEach { data ->
                if (data.wantsPreLogical) preLogicalSet.add(data)
                if (data.wantsMainLogical) mainLogicalSet.add(data)
                if (data.wantsPostLogical) postLogicalSet.add(data)

                if (data.wantsPreSecond) preSecondSet.add(data)
                if (data.wantsMainSecond) mainSecondSet.add(data)
                if (data.wantsPostSecond) postSecondSet.add(data)

                if (data.wantsPreMinute) preMinuteSet.add(data)
                if (data.wantsMainMinute) mainMinuteSet.add(data)
                if (data.wantsPostMinute) postMinuteSet.add(data)

                if (data.wantsPrePhysics) prePhysicsSet.add(data)
                if (data.wantsMainPhysics) mainPhysicsSet.add(data)
                if (data.wantsPostPhysics) postPhysicsSet.add(data)

                if (data.wantsPreRender) preRenderSet.add(data)
                if (data.wantsMainRender) mainRenderSet.add(data)
                if (data.wantsPostRender) postRenderSet.add(data)
            }
            registrationBuffer.clear()
            changed = true
        }

        if (changed) {
            preLogicalArr = preLogicalSet.toTypedArray()
            mainLogicalArr = mainLogicalSet.toTypedArray()
            postLogicalArr = postLogicalSet.toTypedArray()

            preSecondArr = preSecondSet.toTypedArray()
            mainSecondArr = mainSecondSet.toTypedArray()
            postSecondArr = postSecondSet.toTypedArray()

            preMinuteArr = preMinuteSet.toTypedArray()
            mainMinuteArr = mainMinuteSet.toTypedArray()
            postMinuteArr = postMinuteSet.toTypedArray()

            prePhysicsArr = prePhysicsSet.toTypedArray()
            mainPhysicsArr = mainPhysicsSet.toTypedArray()
            postPhysicsArr = postPhysicsSet.toTypedArray()

            preRenderArr = preRenderSet.toTypedArray()
            mainRenderArr = mainRenderSet.toTypedArray()
            postRenderArr = postRenderSet.toTypedArray()
        }
    }

    fun dispatchLogicalUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> preLogicalArr.forEach { if (!it.disabled) it.preLogicalTick() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> mainLogicalArr.forEach { if (!it.disabled) it.logicalTick() }
            la.vok.Game.GameController.UpdatePhase.POST -> postLogicalArr.forEach { if (!it.disabled) it.postLogicalTick() }
        }
    }

    fun dispatchSecondUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> preSecondArr.forEach { if (!it.disabled) it.preSecondUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> mainSecondArr.forEach { if (!it.disabled) it.secondUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> postSecondArr.forEach { if (!it.disabled) it.postSecondUpdate() }
        }
    }

    fun dispatchMinuteUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> preMinuteArr.forEach { if (!it.disabled) it.preMinuteUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> mainMinuteArr.forEach { if (!it.disabled) it.minuteUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> postMinuteArr.forEach { if (!it.disabled) it.postMinuteUpdate() }
        }
    }

    fun dispatchPhysicsUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> prePhysicsArr.forEach { if (!it.disabled) it.prePhysicsUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> mainPhysicsArr.forEach { if (!it.disabled) it.physicsUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> postPhysicsArr.forEach { if (!it.disabled) it.postPhysicsUpdate() }
        }
    }

    fun dispatchRenderUpdate(phase: la.vok.Game.GameController.UpdatePhase) {
        when (phase) {
            la.vok.Game.GameController.UpdatePhase.PRE -> preRenderArr.forEach { if (!it.disabled) it.preRenderUpdate() }
            la.vok.Game.GameController.UpdatePhase.MAIN -> mainRenderArr.forEach { if (!it.disabled) it.renderUpdate() }
            la.vok.Game.GameController.UpdatePhase.POST -> postRenderArr.forEach { if (!it.disabled) it.postRenderUpdate() }
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
        if (!isInside(x, y)) return
        val key = coordToKey(x, y)
        val old = tileDataMap.remove(key)
        if (old != null) unregisterBlockData(old)
        
        if (data != null) {
            tileDataMap[key] = data
            registerBlockData(data)
        }
    }

    fun removeTileData(x: Int, y: Int) {
        if (!isInside(x, y)) return
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
        val data = getTileData(x, y)
        tiles[idx] = null
        tilesHp[idx] = 0
        data?.onRemoved(reason)
        removeTileData(x, y)
    }

    fun damageTile(x: Int, y: Int, damage: Int) {
        if (!containsTile(x, y)) return
        val tileType = getTileType(x, y) ?: return

        tileType.damage(x, y, damage, mapController.dimension, mapController)
        val idx = getIndex(x, y)
        tilesHp[idx] -= damage

        if (tilesHp[idx] <= 0) {
            val reason = "absolute_damage"
            tileType.onRemoved(x, y, mapController.dimension, mapController, reason)
            removeMultiTileParts(x, y, tileType)
            tiles[idx] = null
            tilesHp[idx] = 0
            val data = getTileData(x, y)
            data?.onDestroyed(reason)
            data?.onRemoved(reason)
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
            data?.onDestroyed(mineData)
            data?.onRemoved(mineData)
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
            setTileData(toX, toY, data)
            
            // --- Coordinate Safety: Copy Multitile Dummies ---
            if (type.width > 1 || type.height > 1) {
                for (dx in 0 until type.width) {
                    for (dy in 0 until type.height) {
                        if (dx == 0 && dy == 0) continue
                        val nx = toX + dx
                        val ny = toY + dy
                        if (isInside(nx, ny)) {
                            deactivateTile(nx, ny, "multitile_reconstruction")
                            tiles[getIndex(nx, ny)] = la.vok.Game.GameContent.Tiles.System.MultiTileDummyType(type.tag + "_dummy", -dx p -dy)
                            tilesHp[getIndex(nx, ny)] = hp
                        }
                    }
                }
            }
        }
        
        type.onPositionChanged(fromX, fromY, toX, toY, mapController.dimension)
    }

    /**
     * Atomically swaps Data between two locations. No data loss.
     */
    private fun swapTileData(x1: Int, y1: Int, x2: Int, y2: Int) {
        val key1 = coordToKey(x1, y1)
        val key2 = coordToKey(x2, y2)
        
        val d1 = tileDataMap.remove(key1)
        val d2 = tileDataMap.remove(key2)
        
        if (d1 != null) {
            d1.x = x2; d1.y = y2
            tileDataMap[key2] = d1
            d1.onRelocated(x1, y1, x2, y2)
            d1.onPositionChanged(x1, y1, x2, y2)
        }
        if (d2 != null) {
            d2.x = x1; d2.y = y1
            tileDataMap[key1] = d2
            d2.onRelocated(x2, y2, x1, y1)
            d2.onPositionChanged(x2, y2, x1, y1)
        }
    }

    fun swapTiles(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (!isInside(x1, y1) || !isInside(x2, y2)) return
        
        val type1 = getTileType(x1, y1)
        val type2 = getTileType(x2, y2)

        // For multitiles, use swapArea for safety
        if ((type1 != null && (type1.width > 1 || type1.height > 1 || type1.isDummy)) ||
            (type2 != null && (type2.width > 1 || type2.height > 1 || type2.isDummy))) {
            val w = Math.max(type1?.width ?: 1, type2?.width ?: 1)
            val h = Math.max(type1?.height ?: 1, type2?.height ?: 1)
            swapArea(x1, y1, x2, y2, w, h)
            return
        }

        val idx1 = getIndex(x1, y1)
        val idx2 = getIndex(x2, y2)
        
        val t1 = tiles[idx1]
        val h1 = tilesHp[idx1]
        
        val t2 = tiles[idx2]
        val h2 = tilesHp[idx2]
        
        tiles[idx1] = t2
        tilesHp[idx1] = h2
        
        tiles[idx2] = t1
        tilesHp[idx2] = h1
        
        swapTileData(x1, y1, x2, y2)
        
        t1?.onPositionChanged(x1, y1, x2, y2, mapController.dimension)
        t2?.onPositionChanged(x2, y2, x1, y1, mapController.dimension)
    }

    /**
     * Swaps two rectangular areas of the map.
     * Safely handles all tiles, HP, and MetaData.
     * Uses buffering to support overlapping areas.
     */
    fun swapArea(x1: Int, y1: Int, x2: Int, y2: Int, w: Int, h: Int) {
        // Buffer types and hp to prevent overlap corruption
        val bufferTiles1 = Array<AbstractTileType?>(w * h) { null }
        val bufferHp1 = IntArray(w * h)
        val bufferData1 = arrayOfNulls<la.vok.Game.GameContent.TileData.AbstractTileData>(w * h)

        val bufferTiles2 = Array<AbstractTileType?>(w * h) { null }
        val bufferHp2 = IntArray(w * h)
        val bufferData2 = arrayOfNulls<la.vok.Game.GameContent.TileData.AbstractTileData>(w * h)

        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val bIdx = dy * w + dx
                if (isInside(x1 + dx, y1 + dy)) {
                    bufferTiles1[bIdx] = getTileType(x1 + dx, y1 + dy)
                    bufferHp1[bIdx] = getTileHp(x1 + dx, y1 + dy)
                    bufferData1[bIdx] = tileDataMap.remove(coordToKey(x1 + dx, y1 + dy))
                }
                if (isInside(x2 + dx, y2 + dy)) {
                    bufferTiles2[bIdx] = getTileType(x2 + dx, y2 + dy)
                    bufferHp2[bIdx] = getTileHp(x2 + dx, y2 + dy)
                    bufferData2[bIdx] = tileDataMap.remove(coordToKey(x2 + dx, y2 + dy))
                }
            }
        }

        // Apply back swapped
        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val bIdx = dy * w + dx
                
                // Write Area 2 content to Area 1
                val p1x = x1 + dx
                val p1y = y1 + dy
                if (isInside(p1x, p1y)) {
                    tiles[getIndex(p1x, p1y)] = bufferTiles2[bIdx]
                    tilesHp[getIndex(p1x, p1y)] = bufferHp2[bIdx]
                    val d2 = bufferData2[bIdx]
                    if (d2 != null) {
                        d2.x = p1x; d2.y = p1y
                        tileDataMap[coordToKey(p1x, p1y)] = d2
                        d2.onRelocated(x2 + dx, y2 + dy, p1x, p1y)
                        d2.onPositionChanged(x2 + dx, y2 + dy, p1x, p1y)
                    }
                    bufferTiles1[bIdx]?.onPositionChanged(x1 + dx, y1 + dy, x2 + dx, y2 + dy, mapController.dimension)
                }

                // Write Area 1 content to Area 2
                val p2x = x2 + dx
                val p2y = y2 + dy
                if (isInside(p2x, p2y)) {
                    tiles[getIndex(p2x, p2y)] = bufferTiles1[bIdx]
                    tilesHp[getIndex(p2x, p2y)] = bufferHp1[bIdx]
                    val d1 = bufferData1[bIdx]
                    if (d1 != null) {
                        d1.x = p2x; d1.y = p2y
                        tileDataMap[coordToKey(p2x, p2y)] = d1
                        d1.onRelocated(x1 + dx, y1 + dy, p2x, p2y)
                        d1.onPositionChanged(x1 + dx, y1 + dy, p2x, p2y)
                    }
                    bufferTiles2[bIdx]?.onPositionChanged(x2 + dx, y2 + dy, x1 + dx, y1 + dy, mapController.dimension)
                }
            }
        }
    }

    /**
     * Copies a rectangular area from one location to another.
     * Creates new instances of TileData.
     */
    fun copyArea(fromX: Int, fromY: Int, toX: Int, toY: Int, w: Int, h: Int) {
        val bufferTiles = Array<AbstractTileType?>(w * h) { null }
        val bufferHp = IntArray(w * h)
        // Note: We don't buffer TileData objects because we must create NEW ones.

        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val bIdx = dy * w + dx
                if (isInside(fromX + dx, fromY + dy)) {
                    bufferTiles[bIdx] = getTileType(fromX + dx, fromY + dy)
                    bufferHp[bIdx] = getTileHp(fromX + dx, fromY + dy)
                }
            }
        }

        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val bIdx = dy * w + dx
                val tx = toX + dx
                val ty = toY + dy
                if (isInside(tx, ty)) {
                    val type = bufferTiles[bIdx]
                    val hp = bufferHp[bIdx]
                    
                    deactivateTile(tx, ty, "replaced_by_copy_area")
                    
                    tiles[getIndex(tx, ty)] = type
                    tilesHp[getIndex(tx, ty)] = hp
                    
                    if (type != null && !type.isDummy) {
                        val newData = type.createTileData(tx, ty, mapController.dimension)
                        setTileData(tx, ty, newData)
                    }
                    
                    type?.onPositionChanged(fromX + dx, fromY + dy, tx, ty, mapController.dimension)
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
        if (!isInside(x, y)) return
        val key = coordToKey(x, y)
        val old = wallDataMap.remove(key)
        if (old != null) unregisterBlockData(old)

        if (data != null) {
            wallDataMap[key] = data
            registerBlockData(data)
        }
    }

    fun removeWallData(x: Int, y: Int) {
        if (!isInside(x, y)) return
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
        setWallData(toX, toY, data)
        
        type.onPositionChanged(fromX, fromY, toX, toY, mapController.dimension)
    }

    private fun swapWallData(x1: Int, y1: Int, x2: Int, y2: Int) {
        val key1 = coordToKey(x1, y1)
        val key2 = coordToKey(x2, y2)
        
        val d1 = wallDataMap.remove(key1)
        val d2 = wallDataMap.remove(key2)
        
        if (d1 != null) {
            d1.x = x2; d1.y = y2
            wallDataMap[key2] = d1
            d1.onRelocated(x1, y1, x2, y2)
        }
        if (d2 != null) {
            d2.x = x1; d2.y = y1
            wallDataMap[key1] = d2
            d2.onRelocated(x2, y2, x1, y1)
        }
    }

    fun swapWalls(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (!isInside(x1, y1) || !isInside(x2, y2)) return
        val idx1 = getIndex(x1, y1)
        val idx2 = getIndex(x2, y2)
        
        val t1 = walls[idx1]
        val h1 = wallsHp[idx1]
        
        val t2 = walls[idx2]
        val h2 = wallsHp[idx2]
        
        walls[idx1] = t2
        wallsHp[idx1] = h2
        
        walls[idx2] = t1
        wallsHp[idx2] = h1

        swapWallData(x1, y1, x2, y2)
        
        t1?.onPositionChanged(x1, y1, x2, y2, mapController.dimension)
        t2?.onPositionChanged(x2, y2, x1, y1, mapController.dimension)
    }

    /**
     * Optimized area replacement/copy for walls.
     */
    fun copyAreaWalls(fromX: Int, fromY: Int, toX: Int, toY: Int, w: Int, h: Int) {
        val bufferWalls = Array<AbstractWallType?>(w * h) { null }
        val bufferHp = IntArray(w * h)

        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val bIdx = dy * w + dx
                if (isInside(fromX + dx, fromY + dy)) {
                    bufferWalls[bIdx] = getWallType(fromX + dx, fromY + dy)
                    bufferHp[bIdx] = getWallHp(fromX + dx, fromY + dy)
                }
            }
        }

        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val bIdx = dy * w + dx
                val tx = toX + dx
                val ty = toY + dy
                if (isInside(tx, ty)) {
                    val type = bufferWalls[bIdx]
                    val hp = bufferHp[bIdx]
                    
                    deactivateWall(tx, ty, "replaced_by_copy_area")
                    
                    walls[getIndex(tx, ty)] = type
                    wallsHp[getIndex(tx, ty)] = hp
                    
                    if (type != null) {
                        val newData = type.createWallData(tx, ty, mapController.dimension)
                        setWallData(tx, ty, newData)
                    }
                    
                    type?.onPositionChanged(fromX + dx, fromY + dy, tx, ty, mapController.dimension)
                }
            }
        }
    }

    fun deactivateWall(x: Int, y: Int, reason: Any? = null) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return
        wallType.onRemoved(x, y, mapController.dimension, mapController, reason)
        val data = getWallData(x, y)
        walls[idx] = null
        wallsHp[idx] = 0
        data?.onRemoved(reason)
        removeWallData(x, y)
    }

    fun damageWall(x: Int, y: Int, damage: Int) {
        if (!containsWall(x, y)) return
        val idx = getIndex(x, y)
        val wallType = walls[idx] ?: return

        wallType.damage(x, y, damage, mapController.dimension, mapController)
        wallsHp[idx] -= damage

        if (wallsHp[idx] <= 0) {
            val reason = "absolute_damage"
            wallType.onRemoved(x, y, mapController.dimension, mapController, reason)
            walls[idx] = null
            wallsHp[idx] = 0
            val data = getWallData(x, y)
            data?.onDestroyed(reason)
            data?.onRemoved(reason)
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
            data?.onDestroyed(mineData)
            data?.onRemoved(mineData)
            removeWallData(x, y)
        }
    }
}