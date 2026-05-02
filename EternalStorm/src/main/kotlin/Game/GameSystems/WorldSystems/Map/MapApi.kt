package la.vok.Game.GameContent.Map

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameContent.Tiles.System.MultiTileDummyType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.Game.GameSystems.WorldSystems.Map.WallPlaceType
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameController.UpdatePhase
import la.vok.LavokLibrary.Vectors.p

class MapApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration
    
    fun applyBufferedUpdates() {
        gameCycle.dimensionsController.dimensions.values.forEach { 
            it.mapSystem.applyBufferedUpdates()
        }
    }

    fun applyBufferedUpdates(dimension: AbstractDimension) {
        dimension.mapSystem.applyBufferedUpdates()
    }

    fun dispatchLogicalUpdate(phase: UpdatePhase) {
        gameCycle.dimensionsController.dimensions.values.forEach { 
            it.mapSystem.dispatchLogicalUpdate(phase)
        }
    }

    fun dispatchSecondUpdate(phase: UpdatePhase) {
        gameCycle.dimensionsController.dimensions.values.forEach { 
            it.mapSystem.dispatchSecondUpdate(phase)
        }
    }

    fun dispatchMinuteUpdate(phase: UpdatePhase) {
        gameCycle.dimensionsController.dimensions.values.forEach { 
            it.mapSystem.dispatchMinuteUpdate(phase)
        }
    }

    /**
     * Update specific dimension
     */
    fun dispatchLogicalUpdate(dimension: AbstractDimension, phase: UpdatePhase) {
        dimension.mapSystem.dispatchLogicalUpdate(phase)
    }

    fun dispatchSecondUpdate(dimension: AbstractDimension, phase: UpdatePhase) {
        dimension.mapSystem.dispatchSecondUpdate(phase)
    }

    fun dispatchMinuteUpdate(dimension: AbstractDimension, phase: UpdatePhase) {
        dimension.mapSystem.dispatchMinuteUpdate(phase)
    }

    fun dispatchPhysicsUpdate(phase: UpdatePhase) {
        gameCycle.dimensionsController.dimensions.values.forEach { 
            it.mapSystem.dispatchPhysicsUpdate(phase)
        }
    }

    fun dispatchPhysicsUpdate(dimension: AbstractDimension, phase: UpdatePhase) {
        dimension.mapSystem.dispatchPhysicsUpdate(phase)
    }

    fun dispatchRenderUpdate(phase: UpdatePhase) {
        gameCycle.dimensionsController.dimensions.values.forEach { 
            it.mapSystem.dispatchRenderUpdate(phase)
        }
    }

    fun dispatchRenderUpdate(dimension: AbstractDimension, phase: UpdatePhase) {
        dimension.mapSystem.dispatchRenderUpdate(phase)
    }

    // --- Block Updates ---

    fun updateTile(dimension: AbstractDimension, x: Int, y: Int) {
        dimension.mapSystem.updateTile(x, y)
    }

    fun updateWall(dimension: AbstractDimension, x: Int, y: Int) {
        dimension.mapSystem.updateWall(x, y)
    }

    fun updateBlock(dimension: AbstractDimension, x: Int, y: Int) {
        dimension.mapSystem.updateBlock(x, y)
    }

    fun updateTileNeighbors(dimension: AbstractDimension, x: Int, y: Int) {
        dimension.mapSystem.updateTileNeighbors(x, y)
    }

    fun updateWallNeighbors(dimension: AbstractDimension, x: Int, y: Int) {
        dimension.mapSystem.updateWallNeighbors(x, y)
    }


    fun getTileData(dimension: AbstractDimension, x: Int, y: Int): AbstractTileData? {
        val rawTile = dimension.mapSystem.getTileType(x, y)
        val cx = if (rawTile != null && rawTile.isDummy) x + rawTile.masterOffset.x else x
        val cy = if (rawTile != null && rawTile.isDummy) y + rawTile.masterOffset.y else y
        return dimension.mapSystem.getTileData(cx, cy)
    }

    fun setTileData(dimension: AbstractDimension, x: Int, y: Int, data: la.vok.Game.GameContent.TileData.AbstractTileData?) {
        val rawTile = dimension.mapSystem.getTileType(x, y)
        val cx = if (rawTile != null && rawTile.isDummy) x + rawTile.masterOffset.x else x
        val cy = if (rawTile != null && rawTile.isDummy) y + rawTile.masterOffset.y else y
        dimension.mapSystem.setTileData(cx, cy, data)
    }

    fun hasTileData(dimension: AbstractDimension, x: Int, y: Int): Boolean =
        getTileData(dimension, x, y) != null

    fun getWallData(dimension: AbstractDimension, x: Int, y: Int): la.vok.Game.GameContent.WallData.AbstractWallData? =
        dimension.mapSystem.getWallData(x, y)

    fun setWallData(dimension: AbstractDimension, x: Int, y: Int, data: la.vok.Game.GameContent.WallData.AbstractWallData?) =
        dimension.mapSystem.setWallData(x, y, data)

    fun hasWallData(dimension: AbstractDimension, x: Int, y: Int): Boolean =
        getWallData(dimension, x, y) != null

    private fun markDirtyFootprint(dimension: AbstractDimension, x: Int, y: Int) {
        if (!isInsideMap(dimension, x, y)) return
        val rawTile = dimension.mapSystem.getTileType(x, y)
        val cx = if (rawTile != null && rawTile.isDummy) x + rawTile.masterOffset.x else x
        val cy = if (rawTile != null && rawTile.isDummy) y + rawTile.masterOffset.y else y
        
        val master = dimension.mapSystem.getTileType(cx, cy)
        val w = master?.width ?: 1
        val h = master?.height ?: 1
        for (dx in 0 until w) {
            for (dy in 0 until h) {
                gameCycle.batchApi.markDirty(cx + dx, cy + dy)
            }
        }
    }

    // --------------------------------------------------------
    // TILES
    // --------------------------------------------------------

    fun isInsideMap(dimension: AbstractDimension, x: Int, y: Int): Boolean =
        dimension.mapSystem.isInside(x, y)

    fun tileIsActive(dimension: AbstractDimension, x: Int, y: Int): Boolean =
        dimension.mapSystem.containsTile(x, y)

    fun getTileType(dimension: AbstractDimension, x: Int, y: Int): AbstractTileType? {
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            return dimension.mapSystem.getTileType(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y)
        }
        return rawTile
    }

    fun getMasterPoint(dimension: AbstractDimension, x: Int, y: Int): LPoint {
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            return LPoint(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y)
        }
        return LPoint(x, y)
    }

    fun getTileHp(dimension: AbstractDimension, x: Int, y: Int): Int {
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            return dimension.mapSystem.getTileHp(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y)
        }
        return dimension.mapSystem.getTileHp(x, y)
    }

    fun setTileHp(dimension: AbstractDimension, x: Int, y: Int, hp: Int) {
        if (!isInsideMap(dimension, x, y)) return
        markDirtyFootprint(dimension, x, y)
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            dimension.mapSystem.setTileHp(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y, hp)
        } else {
            dimension.mapSystem.setTileHp(x, y, hp)
        }
    }

    fun damageTile(dimension: AbstractDimension, x: Int, y: Int, damage: Int, notify: Boolean = true) {
        if (!isInsideMap(dimension, x, y)) return
        val master = getMasterPoint(dimension, x, y)
        val masterType = getTileType(dimension, master.x, master.y) ?: return
        
        // High-level: call visual/logic effects (at clicked pos)
        val clickedType = getTileType(dimension, x, y) ?: masterType
        clickedType.damage(x, y, damage, dimension, dimension.mapController)
        markDirtyFootprint(dimension, x, y)
        
        val ms = dimension.mapSystem
        ms.setTileHp(master.x, master.y, ms.getTileHp(master.x, master.y) - damage)
        
        if (ms.getTileHp(master.x, master.y) <= 0) {
            masterType.onMined(master.x, master.y, MineData(damage, 1000000, null, null, null), dimension, dimension.mapController)
            deactivateTile(dimension, master.x, master.y, "absolute_damage", notify)
        }
    }

    fun killTile(dimension: AbstractDimension, x: Int, y: Int, notify: Boolean = true) {
        val type = getTileType(dimension, x, y) ?: return
        mineTile(dimension, x, y, MineData(
            value = type.maxHp + 1000,
            power = 1000000,
            sourceId = null,
            instrument = null,
            item = null
        ), notify)
    }

    fun mineTile(dimension: AbstractDimension, x: Int, y: Int, mineData: MineData, notify: Boolean = true) {
        if (!tileIsActive(dimension, x, y)) return
        val master = getMasterPoint(dimension, x, y)
        val masterType = getTileType(dimension, master.x, master.y) ?: return
        
        if (mineData.power < masterType.blockStrength) return
        
        // High-level: visual effects (at click position)
        val clickedType = getTileType(dimension, x, y) ?: masterType
        clickedType.damage(x, y, mineData.value, dimension, dimension.mapController)
        clickedType.mine(x, y, mineData, dimension, dimension.mapController)
        markDirtyFootprint(dimension, x, y)
        
        val ms = dimension.mapSystem
        ms.setTileHp(master.x, master.y, ms.getTileHp(master.x, master.y) - mineData.value)

        if (ms.getTileHp(master.x, master.y) <= 0) {
            masterType.onMined(master.x, master.y, mineData, dimension, dimension.mapController)
            deactivateTile(dimension, master.x, master.y, mineData, notify)
        }
    }

    fun repairTile(dimension: AbstractDimension, x: Int, y: Int, amount: Int) {
        val type = getTileType(dimension, x, y) ?: return
        val newHp = (getTileHp(dimension, x, y) + amount).coerceAtMost(type.maxHp)
        setTileHp(dimension, x, y, newHp)
    }

    fun controlPlaceTile(dimension: AbstractDimension, type: AbstractTileType, x: Int, y: Int, item: Item, sourceId: Long? = 0, consumed: Boolean = true): Boolean {
        val px = x + type.placeOffset.x
        val py = y + type.placeOffset.y

        if (item.count < 1 && consumed) return false
        if (!isInsideMap(dimension, px, py)) return false
        if (!canPlaceTile(dimension, type, px, py)) return false

        // Replace check and mine
        for (dx in 0 until type.width) {
            for (dy in 0 until type.height) {
                val nx = px + dx
                val ny = py + dy
                val existingTile = getTileType(dimension, nx, ny)
                if (existingTile != null && existingTile.canBeReplaced) {
                    val masterPoint = getMasterPoint(dimension, nx, ny)
                    val master = getTileType(dimension, masterPoint.x, masterPoint.y)!!
                    mineTile(dimension, masterPoint.x, masterPoint.y, MineData(
                        value = master.maxHp,
                        power = 10000,
                        sourceId = sourceId,
                        instrument = gameController.playerControl.getPlayerEntity()?.handItemComponent?.currentHandItem,
                        item = item
                    ))
                }
            }
        }

        // Place Master
        dimension.mapSystem.setTileType(px, py, type)
        maxHp(dimension, px, py)
        type.place(px, py, item, dimension.mapController)
        markDirtyFootprint(dimension, px, py)
        
        // Place Dummies
        if (type.width > 1 || type.height > 1) {
            for (dx in 0 until type.width) {
                for (dy in 0 until type.height) {
                    if (dx == 0 && dy == 0) continue
                    val nx = px + dx
                    val ny = py + dy
                    if (isInsideMap(dimension, nx, ny)) {
                        dimension.mapSystem.setTileType(nx, ny,
                            MultiTileDummyType(type.tag + "_dummy", -dx p -dy)
                        )
                        dimension.mapSystem.setTileHp(nx, ny, type.maxHp)
                    }
                }
            }
        }
        
        if (consumed) item.count--
        return true
    }

    fun updateAreaNeighbors(dimension: AbstractDimension, x: Int, y: Int, w: Int, h: Int) {
        val ms = dimension.mapSystem
        // Horizontal neighbors (top and bottom)
        for (dx in 0 until w) {
            ms.updateBlock(x + dx, y - 1)
            ms.updateBlock(x + dx, y + h)
        }
        // Vertical neighbors (left and right)
        for (dy in 0 until h) {
            ms.updateBlock(x - 1, y + dy)
            ms.updateBlock(x + w, y + dy)
        }
    }

    fun updateNeighbors(dimension: AbstractDimension, x: Int, y: Int) {
        val type = dimension.mapSystem.getTileType(x, y)
        if (type != null && !type.isDummy) {
            updateAreaNeighbors(dimension, x, y, type.width, type.height)
        } else {
            dimension.mapSystem.updateNeighbors(x, y)
        }
    }

    fun handleInteraction(dimension: AbstractDimension, x: Int, y: Int, type: BlockInteractionType, interactor: Entity?): Boolean {
        if (!isInsideMap(dimension, x, y)) return false

        val tile = getTileType(dimension, x, y)
        val context = BlockInteractionContext(x, y, dimension, interactor, this)
        
        if (tile != null) {
            if (tile.onInteract(type, context)) return true
        }
        
        val wall = getWallType(dimension, x, y)
        if (wall != null) {
            if (wall.onInteract(type, context)) return true
        }
        
        return false
    }

    fun deactivateTile(dimension: AbstractDimension, x: Int, y: Int, reason: Any? = null, notify: Boolean = true) {
        if (!isInsideMap(dimension, x, y)) return
        val rawTile = dimension.mapSystem.getTileType(x, y) ?: return
        val type = getTileType(dimension, x, y)!!
        val master = getMasterPoint(dimension, x, y)
        
        dimension.mapSystem.deactivateTile(master.x, master.y, reason, false)
        
        if (notify) {
            updateAreaNeighbors(dimension, master.x, master.y, type.width, type.height)
        }
    }

    fun removeTileSilent(dimension: AbstractDimension, x: Int, y: Int) =
        deactivateTile(dimension, x, y, notify = false)

    fun generateTile(dimension: AbstractDimension, type: AbstractTileType?, x: Int, y: Int) {
        setTileType(dimension, type, x, y)
        maxHp(dimension, x, y)
    }

    fun generateTile(dimension: AbstractDimension, type: String, x: Int, y: Int) {
        setTileType(dimension, type, x, y)
        maxHp(dimension, x, y)
    }

    fun setTileType(dimension: AbstractDimension, type: AbstractTileType?, x: Int, y: Int, notify: Boolean = true) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setTileType(x, y, type, false)
        if (notify) {
            updateNeighbors(dimension, x, y)
        }
    }

    fun setTileTypeSilent(dimension: AbstractDimension, type: AbstractTileType?, x: Int, y: Int) =
        setTileType(dimension, type, x, y, false)

    fun setTileType(dimension: AbstractDimension, type: String, x: Int, y: Int, notify: Boolean = true) {
        setTileType(dimension, getRegisteredTileType(type), x, y, notify)
    }

    fun maxHp(dimension: AbstractDimension, x: Int, y: Int) {
        val type = getTileType(dimension, x, y) ?: return
        val master = getMasterPoint(dimension, x, y)
        dimension.mapSystem.setTileHp(master.x, master.y, type.maxHp)
    }

    fun deleteTile(dimension: AbstractDimension, x: Int, y: Int) {
        markDirtyFootprint(dimension, x, y)
        deactivateTile(dimension, x, y)
    }

    fun fillTileRegion(dimension: AbstractDimension, type: AbstractTileType, startX: Int, startY: Int, endX: Int, endY: Int) {
        for (x in startX..endX)
            for (y in startY..endY)
                setTileType(dimension, type, x, y)
    }

    fun clearAllTiles(dimension: AbstractDimension) {
        for (x in 0 until dimension.mapSystem.width)
            for (y in 0 until dimension.mapSystem.height)
                deleteTile(dimension, x, y)
    }

    fun moveTile(dimension: AbstractDimension, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (!isInsideMap(dimension, fromX, fromY) || !isInsideMap(dimension, toX, toY)) return
        
        val type = getTileType(dimension, fromX, fromY) ?: return
        if (type.width > 1 || type.height > 1) {
            val master = getMasterPoint(dimension, fromX, fromY)
            swapArea(dimension, master.x, master.y, toX, toY, type.width, type.height)
            return
        }

        markDirtyFootprint(dimension, fromX, fromY)
        markDirtyFootprint(dimension, toX, toY)
        dimension.mapSystem.swapTiles(fromX, fromY, toX, toY)
    }

    fun swapArea(dimension: AbstractDimension, x1: Int, y1: Int, x2: Int, y2: Int, w: Int, h: Int) {
        markDirtyFootprint(dimension, x1, y1)
        markDirtyFootprint(dimension, x2, y2)
        dimension.mapSystem.swapArea(x1, y1, x2, y2, w, h)
    }

    fun copyTile(dimension: AbstractDimension, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (!isInsideMap(dimension, fromX, fromY) || !isInsideMap(dimension, toX, toY)) return
        val type = getTileType(dimension, fromX, fromY) ?: return
        val hp = getTileHp(dimension, fromX, fromY)
        
        // Use high-level methods for consistency
        deactivateTile(dimension, toX, toY, "replaced_by_copy")
        
        setTileType(dimension, type, toX, toY)
        setTileHp(dimension, toX, toY, hp)
        
        // Multi-tile support
        if (type.width > 1 || type.height > 1) {
            for (dx in 0 until type.width) {
                for (dy in 0 until type.height) {
                    if (dx == 0 && dy == 0) continue
                    val nx = toX + dx
                    val ny = toY + dy
                    if (isInsideMap(dimension, nx, ny)) {
                        deactivateTile(dimension, nx, ny, "multitile_reconstruction")
                        dimension.mapSystem.setTileType(nx, ny, MultiTileDummyType(type.tag + "_dummy", -dx p -dy), false)
                        dimension.mapSystem.setTileHp(nx, ny, hp)
                    }
                }
            }
        }
        
        type.onPositionChanged(fromX, fromY, toX, toY, dimension)
    }

    fun copyArea(dimension: AbstractDimension, x1: Int, y1: Int, x2: Int, y2: Int, w: Int, h: Int) {
        markDirtyFootprint(dimension, x1, y1)
        markDirtyFootprint(dimension, x2, y2)
        dimension.mapSystem.copyArea(x1, y1, x2, y2, w, h)
    }

    fun swapTiles(dimension: AbstractDimension, x1: Int, y1: Int, x2: Int, y2: Int) {
        if (!isInsideMap(dimension, x1, y1) || !isInsideMap(dimension, x2, y2)) return
        markDirtyFootprint(dimension, x1, y1)
        markDirtyFootprint(dimension, x2, y2)
        dimension.mapSystem.swapTiles(x1, y1, x2, y2)
    }

    fun getRegisteredTileType(tag: String): AbstractTileType =
        objectRegistration.tiles[tag] ?: error("Tile $tag not found")

    // --------------------------------------------------------
    // WALLS
    // --------------------------------------------------------

    fun wallIsActive(dimension: AbstractDimension, x: Int, y: Int): Boolean =
        dimension.mapSystem.containsWall(x, y)

    fun getWallType(dimension: AbstractDimension, x: Int, y: Int): AbstractWallType? =
        dimension.mapSystem.getWallType(x, y)

    fun getWallHp(dimension: AbstractDimension, x: Int, y: Int): Int =
        dimension.mapSystem.getWallHp(x, y)

    fun setWallHp(dimension: AbstractDimension, x: Int, y: Int, hp: Int) {
        if (isInsideMap(dimension, x, y)) {
            markDirtyFootprint(dimension, x, y)
            dimension.mapSystem.setWallHp(x, y, hp)
        }
    }

    fun damageWall(dimension: AbstractDimension, x: Int, y: Int, damage: Int, notify: Boolean = true) {
        if (!isInsideMap(dimension, x, y)) return
        val type = getWallType(dimension, x, y) ?: return
        
        type.damage(x, y, damage, dimension, dimension.mapController)
        markDirtyFootprint(dimension, x, y)
        
        val ms = dimension.mapSystem
        ms.setWallHp(x, y, ms.getWallHp(x, y) - damage)
        
        if (ms.getWallHp(x, y) <= 0) {
            deactivateWall(dimension, x, y, "absolute_damage", notify)
        }
    }

    fun killWall(dimension: AbstractDimension, x: Int, y: Int, notify: Boolean = true) {
        val type = getWallType(dimension, x, y) ?: return
        mineWall(dimension, x, y, MineData(
            value = type.maxHp + 1000,
            power = 1000000,
            sourceId = null,
            instrument = null,
            item = null
        ), notify)
    }

    fun mineWall(dimension: AbstractDimension, x: Int, y: Int, mineData: MineData, notify: Boolean = true) {
        if (!wallIsActive(dimension, x, y)) return
        val type = getWallType(dimension, x, y) ?: return
        if (mineData.power < type.blockStrength) return
        
        type.damage(x, y, mineData.value, dimension, dimension.mapController)
        markDirtyFootprint(dimension, x, y)
        
        val ms = dimension.mapSystem
        ms.setWallHp(x, y, ms.getWallHp(x, y) - mineData.value)
        if (ms.getWallHp(x, y) <= 0) {
            type.onMined(x, y, mineData, dimension, dimension.mapController)
            deactivateWall(dimension, x, y, mineData, notify)
        }
    }

    fun repairWall(dimension: AbstractDimension, x: Int, y: Int, amount: Int) {
        val type = getWallType(dimension, x, y) ?: return
        val newHp = (getWallHp(dimension, x, y) + amount).coerceAtMost(type.maxHp)
        setWallHp(dimension, x, y, newHp)
    }

    fun controlPlaceWall(dimension: AbstractDimension, type: AbstractWallType, x: Int, y: Int, item: Item, consumed: Boolean = true): Boolean {
        if (item.count < 1 && consumed) return false
        if (wallIsActive(dimension, x, y)) return false
        if (!isInsideMap(dimension, x, y)) return false
        if (!canPlaceWall(dimension, type, x, y)) return false
        
        dimension.mapSystem.setWallType(x, y, type)
        maxHpWall(dimension, x, y)
        type.place(x, y, item, dimension.mapController)
        markDirtyFootprint(dimension, x, y)
        if (consumed) item.count--
        return true
    }

    fun generateWall(dimension: AbstractDimension, type: AbstractWallType?, x: Int, y: Int) {
        setWallType(dimension, type, x, y)
        maxHpWall(dimension, x, y)
    }

    fun generateWall(dimension: AbstractDimension, type: String, x: Int, y: Int) {
        setWallType(dimension, type, x, y)
        maxHpWall(dimension, x, y)
    }

    fun setWallType(dimension: AbstractDimension, type: AbstractWallType?, x: Int, y: Int, notify: Boolean = true) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setWallType(x, y, type, notify)
    }

    fun setWallTypeSilent(dimension: AbstractDimension, type: AbstractWallType?, x: Int, y: Int) =
        setWallType(dimension, type, x, y, false)

    fun setWallType(dimension: AbstractDimension, type: String, x: Int, y: Int, notify: Boolean = true) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setWallType(x, y, getRegisteredWallType(type), notify)
    }

    fun maxHpWall(dimension: AbstractDimension, x: Int, y: Int) {
        val type = getWallType(dimension, x, y) ?: return
        dimension.mapSystem.setWallHp(x, y, type.maxHp)
    }

    fun deactivateWall(dimension: AbstractDimension, x: Int, y: Int, reason: Any? = null, notify: Boolean = true) {
        if (!isInsideMap(dimension, x, y)) return
        dimension.mapSystem.deactivateWall(x, y, reason, false)
        if (notify) {
            dimension.mapSystem.updateNeighbors(x, y)
        }
    }

    fun deleteWall(dimension: AbstractDimension, x: Int, y: Int, notify: Boolean = true) {
        markDirtyFootprint(dimension, x, y)
        deactivateWall(dimension, x, y, null, notify)
    }

    fun removeWallSilent(dimension: AbstractDimension, x: Int, y: Int) =
        deleteWall(dimension, x, y, false)

    fun fillWallRegion(dimension: AbstractDimension, type: AbstractWallType, startX: Int, startY: Int, endX: Int, endY: Int) {
        for (x in startX..endX)
            for (y in startY..endY)
                setWallType(dimension, type, x, y)
    }

    fun clearAllWalls(dimension: AbstractDimension) {
        for (x in 0 until dimension.mapSystem.width)
            for (y in 0 until dimension.mapSystem.height)
                deleteWall(dimension, x, y)
    }

    fun moveWall(dimension: AbstractDimension, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (!isInsideMap(dimension, fromX, fromY) || !isInsideMap(dimension, toX, toY)) return
        markDirtyFootprint(dimension, fromX, fromY)
        markDirtyFootprint(dimension, toX, toY)
        dimension.mapSystem.deactivateWall(toX, toY, "moved_here")
        dimension.mapSystem.swapWalls(fromX, fromY, toX, toY)
    }

    fun copyWall(dimension: AbstractDimension, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (!isInsideMap(dimension, fromX, fromY) || !isInsideMap(dimension, toX, toY)) return
        val type = getWallType(dimension, fromX, fromY) ?: return
        val hp = getWallHp(dimension, fromX, fromY)
        
        markDirtyFootprint(dimension, fromX, fromY)
        markDirtyFootprint(dimension, toX, toY)
        
        deleteWall(dimension, toX, toY, false)
        setWallType(dimension, type, toX, toY)
        setWallHp(dimension, toX, toY, hp)
        
        type.onPositionChanged(fromX, fromY, toX, toY, dimension)
    }

    fun swapWalls(dimension: AbstractDimension, x1: Int, y1: Int, x2: Int, y2: Int) {
        if (!isInsideMap(dimension, x1, y1) || !isInsideMap(dimension, x2, y2)) return
        markDirtyFootprint(dimension, x1, y1)
        markDirtyFootprint(dimension, x2, y2)
        dimension.mapSystem.swapWalls(x1, y1, x2, y2)
    }

    fun getRegisteredWallType(tag: String): AbstractWallType =
        objectRegistration.walls[tag]!!

    // --------------------------------------------------------
    // SHARED
    // --------------------------------------------------------

    fun getBlockPos(point: LPoint): Vec2 =
        point.x v point.y

    fun getBlockPos(x: Int, y: Int): Vec2 =
        x v y

    fun getBlockPos(dimension: AbstractDimension, point: LPoint): Vec2 =
        getBlockPos(point)

    fun getBlockPos(dimension: AbstractDimension, x: Int, y: Int): Vec2 =
        getBlockPos(x, y)

    fun generateMap(dimension: AbstractDimension) {
        dimension.generateMap()
    }

    fun getBlockSize(): Vec2 =
        1f v 1f

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {
        return LPoint(
            Math.floor(pos.x + 0.5).toInt(),
            Math.floor(pos.y + 0.5).toInt()
        )
    }

    fun checkStability(dimension: AbstractDimension, type: AbstractTileType, x: Int, y: Int): Boolean {
        if (type.placeType == TilePlaceType.FREE) return true

        if (type.placeType == TilePlaceType.FULL_ON_BLOCKS) {
            val masterPoint = LPoint(x, y)
            for (dx in 0 until type.width) {
                val cx = x + dx
                val cy = y - 1
                if (!tileIsActive(dimension, cx, cy)) return false
                if (getMasterPoint(dimension, cx, cy) == masterPoint) return false
            }
            return true
        }

        var satisfied = false
        val masterPoint = LPoint(x, y)
        
        for (dx in 0 until type.width) {
            for (dy in 0 until type.height) {
                val nx = x + dx
                val ny = y + dy

                val matches = when (type.placeType) {
                    TilePlaceType.FREE -> true
                    TilePlaceType.ON_TILE -> {
                        val checkX = nx
                        val checkY = ny - 1
                        tileIsActive(dimension, checkX, checkY) && getMasterPoint(dimension, checkX, checkY) != masterPoint
                    }
                    TilePlaceType.NEAR_TILE -> {
                        hasNearTile(dimension, nx, ny) // This might need refinement but usually NEAR means horizontal
                    }
                    TilePlaceType.NEAR_WALL -> hasNearWall(dimension, nx, ny)
                    TilePlaceType.NEAR_TILE_OR_ON_WALL -> {
                        (hasNearTile(dimension, nx, ny) && !isSameStructure(dimension, nx, ny, nx + 1, ny) && !isSameStructure(dimension, nx, ny, nx - 1, ny)) || 
                        wallIsActive(dimension, nx, ny)
                    }
                    TilePlaceType.CUSTOM -> {
                        type.canPlace(nx, ny, dimension, dimension.mapController)
                    }
                    else -> false
                }
                if (matches) {
                    satisfied = true
                    break
                }
            }
            if (satisfied) break
        }

        return satisfied
    }

    fun canPlaceTile(dimension: AbstractDimension, type: AbstractTileType, x: Int, y: Int): Boolean {
        // Enforce area-wide freedom
        for (dx in 0 until type.width) {
            for (dy in 0 until type.height) {
                val nx = x + dx
                val ny = y + dy
                if (!isInsideMap(dimension, nx, ny)) return false

                val existingTile = getTileType(dimension, nx, ny)
                if (existingTile != null && !existingTile.canBeReplaced) return false
            }
        }

        // Check adjacency requirements across the whole footprint
        return checkStability(dimension, type, x, y)
    }

    fun canPlaceWall(dimension: AbstractDimension, type: AbstractWallType, x: Int, y: Int): Boolean {

        if (!isInsideMap(dimension, x, y)) return false
        if (wallIsActive(dimension, x, y)) return false

        return when (type.placeType) {

            WallPlaceType.FREE -> true

            WallPlaceType.ON_TILE ->
                tileIsActive(dimension, x, y)

            WallPlaceType.NEAR_WALL_OR_TILE ->
                hasNearTile(dimension, x, y) || hasNearWall(dimension, x, y)

            WallPlaceType.CUSTOM -> {
                type.canPlace(x, y, dimension, dimension.mapController)
            }
        }
    }

    private fun hasNearTile(dimension: AbstractDimension, x: Int, y: Int): Boolean {
        val master = getMasterPoint(dimension, x, y)
        fun check(nx: Int, ny: Int) = tileIsActive(dimension, nx, ny) && getMasterPoint(dimension, nx, ny) != master

        return check(x + 1, y) || check(x - 1, y) || check(x, y + 1) || check(x, y - 1)
    }

    fun isSameStructure(dimension: AbstractDimension, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return getMasterPoint(dimension, x1, y1) == getMasterPoint(dimension, x2, y2)
    }

    private fun hasNearWall(dimension: AbstractDimension, x: Int, y: Int): Boolean {
        return wallIsActive(dimension, x + 1, y) ||
                wallIsActive(dimension, x - 1, y) ||
                wallIsActive(dimension, x, y + 1) ||
                wallIsActive(dimension, x, y - 1)
    }
}