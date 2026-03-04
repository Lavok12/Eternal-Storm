package la.vok.Game.GameContent.Map

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.WallContext
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class MapApi(var mapController: MapController) {

    val gameCycle: GameCycle = mapController.gameCycle
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    // --------------------------------------------------------
    // TILES
    // --------------------------------------------------------

    fun isInsideMap(x: Int, y: Int): Boolean =
        mapController.mapSystem.isInside(x, y)

    fun tileIsActive(x: Int, y: Int): Boolean =
        mapController.mapSystem.containsTile(x, y)

    fun getTileContext(x: Int, y: Int): TileContext? =
        mapController.mapSystem.getTileContext(x, y)

    fun getTileType(x: Int, y: Int): AbstractTileType? =
        mapController.mapSystem.getTileType(x, y)

    fun getTileHp(x: Int, y: Int): Int =
        mapController.mapSystem.getTileHp(x, y)

    fun setTileHp(x: Int, y: Int, hp: Int) {
        if (isInsideMap(x, y)) {
            mapController.mapSystem.setTileHp(x, y, hp)
        }
    }

    fun damageTile(x: Int, y: Int, damage: Int) {
        if (!isInsideMap(x, y)) return
        mapController.mapSystem.damageTile(x, y, damage)
    }

    fun mineTile(x: Int, y: Int, mineData: MineData) {
        if (!tileIsActive(x, y)) return
        val tileType = getTileType(x, y) ?: return
        if (mineData.power < tileType.blockStrength) return
        mapController.mapSystem.mineTile(x, y, mineData)
    }

    fun repairTile(x: Int, y: Int, amount: Int) {
        val type = getTileType(x, y) ?: return
        val newHp = (getTileHp(x, y) + amount).coerceAtMost(type.maxHp)
        setTileHp(x, y, newHp)
    }

    fun controlPlaceTile(type: AbstractTileType, x: Int, y: Int, item: Item, consumed: Boolean = true): Boolean {
        if (item.count < 1 && consumed) return false
        if (tileIsActive(x, y)) return false
        if (!isInsideMap(x, y)) return false
        mapController.mapSystem.setTileType(x, y, type)
        mapController.mapSystem.setTileHp(x, y, type.maxHp)
        mapController.mapSystem.callPlace(x, y, item)
        if (consumed) item.count--
        return true
    }

    fun setTile(context: TileContext) =
        mapController.mapSystem.setTile(context)

    fun generateTile(type: AbstractTileType?, x: Int, y: Int) {
        setTileType(type, x, y)
        maxHp(x, y)
    }

    fun generateTile(type: String, x: Int, y: Int) {
        setTileType(type, x, y)
        maxHp(x, y)
    }

    fun setTileType(type: AbstractTileType?, x: Int, y: Int) =
        mapController.mapSystem.setTileType(x, y, type)

    fun setTileType(type: String, x: Int, y: Int) =
        mapController.mapSystem.setTileType(x, y, getRegisteredTileType(type))

    fun maxHp(x: Int, y: Int) =
        mapController.mapSystem.maxHp(x, y)

    fun deleteTile(x: Int, y: Int) =
        mapController.mapSystem.deactivateTile(x, y)

    fun fillTileRegion(type: AbstractTileType, startX: Int, startY: Int, endX: Int, endY: Int) {
        for (x in startX..endX)
            for (y in startY..endY)
                setTileType(type, x, y)
    }

    fun clearAllTiles() {
        for (x in 0 until mapController.mapSystem.width)
            for (y in 0 until mapController.mapSystem.height)
                deleteTile(x, y)
    }

    fun getRegisteredTileType(tag: String): AbstractTileType =
        objectRegistration.tiles[tag]!!

    // --------------------------------------------------------
    // WALLS
    // --------------------------------------------------------

    fun wallIsActive(x: Int, y: Int): Boolean =
        mapController.mapSystem.containsWall(x, y)

    fun getWallContext(x: Int, y: Int): WallContext? =
        mapController.mapSystem.getWallContext(x, y)

    fun getWallType(x: Int, y: Int): AbstractWallType? =
        mapController.mapSystem.getWallType(x, y)

    fun getWallHp(x: Int, y: Int): Int =
        mapController.mapSystem.getWallHp(x, y)

    fun setWallHp(x: Int, y: Int, hp: Int) {
        if (isInsideMap(x, y))
            mapController.mapSystem.setWallHp(x, y, hp)
    }

    fun damageWall(x: Int, y: Int, damage: Int) {
        if (!isInsideMap(x, y)) return
        mapController.mapSystem.damageWall(x, y, damage)
    }

    fun mineWall(x: Int, y: Int, mineData: MineData) {
        if (!wallIsActive(x, y)) return
        val wallType = getWallType(x, y) ?: return
        if (mineData.power < wallType.blockStrength) return
        mapController.mapSystem.mineWall(x, y, mineData)
    }

    fun repairWall(x: Int, y: Int, amount: Int) {
        val type = getWallType(x, y) ?: return
        val newHp = (getWallHp(x, y) + amount).coerceAtMost(type.maxHp)
        setWallHp(x, y, newHp)
    }

    fun controlPlaceWall(type: AbstractWallType, x: Int, y: Int, item: Item, consumed: Boolean = true): Boolean {
        print(2234342345)
        if (item.count < 1 && consumed) return false
        if (wallIsActive(x, y)) return false
        if (!isInsideMap(x, y)) return false
        mapController.mapSystem.setWallType(x, y, type)
        mapController.mapSystem.setWallHp(x, y, type.maxHp)
        mapController.mapSystem.callPlaceWall(x, y, item)
        if (consumed) item.count--
        return true
    }

    fun setWall(context: WallContext) =
        mapController.mapSystem.setWall(context)

    fun generateWall(type: AbstractWallType?, x: Int, y: Int) {
        setWallType(type, x, y)
        maxHpWall(x, y)
    }

    fun generateWall(type: String, x: Int, y: Int) {
        setWallType(type, x, y)
        maxHpWall(x, y)
    }

    fun setWallType(type: AbstractWallType?, x: Int, y: Int) =
        mapController.mapSystem.setWallType(x, y, type)

    fun setWallType(type: String, x: Int, y: Int) =
        mapController.mapSystem.setWallType(x, y, getRegisteredWallType(type))

    fun maxHpWall(x: Int, y: Int) =
        mapController.mapSystem.maxHpWall(x, y)

    fun deleteWall(x: Int, y: Int) =
        mapController.mapSystem.deactivateWall(x, y)

    fun fillWallRegion(type: AbstractWallType, startX: Int, startY: Int, endX: Int, endY: Int) {
        for (x in startX..endX)
            for (y in startY..endY)
                setWallType(type, x, y)
    }

    fun clearAllWalls() {
        for (x in 0 until mapController.mapSystem.width)
            for (y in 0 until mapController.mapSystem.height)
                deleteWall(x, y)
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

    fun getBlockSize(): Vec2 =
        1f v 1f

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {
        return LPoint(
            Math.floor(pos.x + 0.5).toInt(),
            Math.floor(pos.y + 0.5).toInt()
        )
    }
}