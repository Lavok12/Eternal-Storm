package la.vok.Game.GameContent.Map

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
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

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.Vectors.p

class MapApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

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

    fun damageTile(dimension: AbstractDimension, x: Int, y: Int, damage: Int) {
        if (!isInsideMap(dimension, x, y)) return
        markDirtyFootprint(dimension, x, y)
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            dimension.mapSystem.damageTile(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y, damage)
        } else {
            dimension.mapSystem.damageTile(x, y, damage)
        }
    }

    fun mineTile(dimension: AbstractDimension, x: Int, y: Int, mineData: MineData) {
        if (!tileIsActive(dimension, x, y)) return
        val tileType = getTileType(dimension, x, y) ?: return
        if (mineData.power < tileType.blockStrength) return
        
        markDirtyFootprint(dimension, x, y)
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            dimension.mapSystem.mineTile(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y, mineData)
        } else {
            dimension.mapSystem.mineTile(x, y, mineData)
        }
    }

    fun repairTile(dimension: AbstractDimension, x: Int, y: Int, amount: Int) {
        val type = getTileType(dimension, x, y) ?: return
        val newHp = (getTileHp(dimension, x, y) + amount).coerceAtMost(type.maxHp)
        setTileHp(dimension, x, y, newHp)
    }

    fun controlPlaceTile(dimension: AbstractDimension, type: AbstractTileType, x: Int, y: Int, item: Item, consumed: Boolean = true): Boolean {
        val px = x + type.placeOffset.x
        val py = y + type.placeOffset.y

        if (item.count < 1 && consumed) return false
        if (!isInsideMap(dimension, px, py)) return false
        if (!canPlaceTile(dimension, type, px, py)) return false
        
        // Place Master
        dimension.mapSystem.setTileType(px, py, type)
        dimension.mapSystem.setTileHp(px, py, type.maxHp)
        dimension.mapSystem.callPlace(px, py, item)
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

    fun handleInteraction(dimension: AbstractDimension, x: Int, y: Int, type: BlockInteractionType, interactor: Entity?): Boolean {
        if (!isInsideMap(dimension, x, y)) return false
        
        // getTileType already handles dummy redirection
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

    fun deactivateTile(dimension: AbstractDimension, x: Int, y: Int, reason: Any? = null) {
        if (!isInsideMap(dimension, x, y)) return
        markDirtyFootprint(dimension, x, y)
        val rawTile = dimension.mapSystem.getTileType(x, y)
        if (rawTile != null && rawTile.isDummy) {
            dimension.mapSystem.deactivateTile(x + rawTile.masterOffset.x, y + rawTile.masterOffset.y, reason)
        } else {
            dimension.mapSystem.deactivateTile(x, y, reason)
        }
    }

    fun generateTile(dimension: AbstractDimension, type: AbstractTileType?, x: Int, y: Int) {
        setTileType(dimension, type, x, y)
        maxHp(dimension, x, y)
    }

    fun generateTile(dimension: AbstractDimension, type: String, x: Int, y: Int) {
        setTileType(dimension, type, x, y)
        maxHp(dimension, x, y)
    }

    fun setTileType(dimension: AbstractDimension, type: AbstractTileType?, x: Int, y: Int) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setTileType(x, y, type)
    }

    fun setTileType(dimension: AbstractDimension, type: String, x: Int, y: Int) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setTileType(x, y, getRegisteredTileType(type))
    }

    fun maxHp(dimension: AbstractDimension, x: Int, y: Int) =
        dimension.mapSystem.maxHp(x, y)

    fun deleteTile(dimension: AbstractDimension, x: Int, y: Int) =
        dimension.mapSystem.deactivateTile(x, y)

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

    fun damageWall(dimension: AbstractDimension, x: Int, y: Int, damage: Int) {
        if (!isInsideMap(dimension, x, y)) return
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.damageWall(x, y, damage)
    }

    fun mineWall(dimension: AbstractDimension, x: Int, y: Int, mineData: MineData) {
        if (!wallIsActive(dimension, x, y)) return
        val wallType = getWallType(dimension, x, y) ?: return
        if (mineData.power < wallType.blockStrength) return
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.mineWall(x, y, mineData)
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
        dimension.mapSystem.setWallHp(x, y, type.maxHp)
        dimension.mapSystem.callPlaceWall(x, y, item)
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

    fun setWallType(dimension: AbstractDimension, type: AbstractWallType?, x: Int, y: Int) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setWallType(x, y, type)
    }

    fun setWallType(dimension: AbstractDimension, type: String, x: Int, y: Int) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.setWallType(x, y, getRegisteredWallType(type))
    }

    fun maxHpWall(dimension: AbstractDimension, x: Int, y: Int) =
        dimension.mapSystem.maxHpWall(x, y)

    fun deleteWall(dimension: AbstractDimension, x: Int, y: Int) {
        markDirtyFootprint(dimension, x, y)
        dimension.mapSystem.deactivateWall(x, y)
    }

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

    fun canPlaceTile(dimension: AbstractDimension, type: AbstractTileType, x: Int, y: Int): Boolean {
        // Enforce area-wide freedom
        for (dx in 0 until type.width) {
            for (dy in 0 until type.height) {
                val nx = x + dx
                val ny = y + dy
                if (!isInsideMap(dimension, nx, ny)) return false
                if (tileIsActive(dimension, nx, ny)) return false
            }
        }

        // Check adjacency requirements across the whole footprint
        if (type.placeType == TilePlaceType.FREE) return true

        var satisfied = false
        for (dx in 0 until type.width) {
            for (dy in 0 until type.height) {
                val nx = x + dx
                val ny = y + dy

                val matches = when (type.placeType) {
                    TilePlaceType.FREE -> true
                    TilePlaceType.ON_TILE -> tileIsActive(dimension, nx, ny)
                    TilePlaceType.NEAR_TILE -> hasNearTile(dimension, nx, ny)
                    TilePlaceType.NEAR_WALL -> hasNearWall(dimension, nx, ny)
                    TilePlaceType.NEAR_TILE_OR_ON_WALL -> hasNearTile(dimension, nx, ny) || wallIsActive(dimension, nx, ny)
                    TilePlaceType.CUSTOM -> {
                        type.canPlace(nx, ny, dimension, dimension.mapController)
                    }
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
        return tileIsActive(dimension, x + 1, y) ||
                tileIsActive(dimension, x - 1, y) ||
                tileIsActive(dimension, x, y + 1) ||
                tileIsActive(dimension, x, y - 1)
    }

    private fun hasNearWall(dimension: AbstractDimension, x: Int, y: Int): Boolean {
        return wallIsActive(dimension, x + 1, y) ||
                wallIsActive(dimension, x - 1, y) ||
                wallIsActive(dimension, x, y + 1) ||
                wallIsActive(dimension, x, y - 1)
    }
}