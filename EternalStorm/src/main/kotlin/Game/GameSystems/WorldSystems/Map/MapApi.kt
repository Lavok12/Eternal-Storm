package la.vok.Game.GameContent.Map

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class MapApi(var mapController: MapController) {
    val gameCycle: GameCycle = mapController.gameCycle
    val gameController: GameController get() = gameCycle.gameController
    val objectRegistration: ObjectRegistration get() = gameController.coreController.objectRegistration

    fun isInsideMap(x: Int, y: Int): Boolean = mapController.mapSystem.isInside(x, y)

    fun tileIsActive(x: Int, y: Int): Boolean = mapController.mapSystem.containsTile(x, y)

    fun getTileContext(x: Int, y: Int): TileContext? = mapController.mapSystem.getTile(x, y)

    fun getTileType(x: Int, y: Int): AbstractTileType? = mapController.mapSystem.getTileType(x, y)

    fun getTileHp(x: Int, y: Int): Int = mapController.mapSystem.getTileHp(x, y)

    fun setTileHp(x: Int, y: Int, hp: Int) {
        if (isInsideMap(x, y)) mapController.mapSystem.setTileHp(x, y, hp)
    }

    fun damageTile(x: Int, y: Int, damage: Int) {
        if (!isInsideMap(x, y)) return
        val currentHp = getTileHp(x, y)
        val newHp = currentHp - damage
        if (newHp <= 0) {
            deleteTile(x, y)
        } else {
            setTileHp(x, y, newHp)
        }
    }

    fun repairTile(x: Int, y: Int, amount: Int) {
        val type = getTileType(x, y) ?: return
        val newHp = (getTileHp(x, y) + amount).coerceAtMost(type.maxHp)
        setTileHp(x, y, newHp)
    }

    fun placeTile(type: AbstractTileType, x: Int, y: Int) {
        if (isInsideMap(x, y)) {
            mapController.mapSystem.setTileType(x, y, type)
            mapController.mapSystem.setTileHp(x, y, type.maxHp)
        }
    }

    fun placeTile(tag: String, x: Int, y: Int) = placeTile(getRegisteredTileType(tag), x, y)

    fun setTile(context: TileContext) = mapController.mapSystem.setTile(context)

    fun setTileType(x: Int, y: Int, abstractTileType: AbstractTileType?) = mapController.mapSystem.setTileType(x, y, abstractTileType)

    fun deleteTile(x: Int, y: Int) = mapController.mapSystem.deactivateTile(x, y)

    fun fillRegion(type: AbstractTileType, startX: Int, startY: Int, endX: Int, endY: Int) {
        for (x in startX..endX) {
            for (y in startY..endY) {
                placeTile(type, x, y)
            }
        }
    }

    fun clearAll() {
        for (x in 0 until mapController.mapSystem.width) {
            for (y in 0 until mapController.mapSystem.height) {
                deleteTile(x, y)
            }
        }
    }

    fun getRegisteredTileType(tag: String): AbstractTileType = objectRegistration.tiles[tag]!!

    fun getTilePos(point: LPoint): Vec2 = point.x v point.y

    fun getTileSize(): Vec2 = 1f v 1f

    @Suppress("NOTHING_TO_INLINE")
    inline fun getPointFromPos(pos: Vec2): LPoint {
        return LPoint(Math.floor(pos.x + 0.5).toInt(), Math.floor(pos.y + 0.5).toInt())
    }
}