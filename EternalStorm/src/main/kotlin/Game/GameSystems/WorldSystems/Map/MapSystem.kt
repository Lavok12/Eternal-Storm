package la.vok.Game.GameContent.Map

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameSystems.WorldSystems.Map.MineData

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

    fun getTileContext(x: Int, y: Int): TileContext? {
        if (!isInside(x, y)) return null
        val idx = getIndex(x, y)
        return TileContext(x, y, tilesHp[idx], tiles[idx])
    }

    fun setTile(tileContext: TileContext) {
        val x = tileContext.positionX
        val y = tileContext.positionY
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        tiles[idx] = tileContext.tileType
        tilesHp[idx] = tileContext.hp
    }

    fun setTileType(x: Int, y: Int, abstractTileType: AbstractTileType?) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        tiles[idx] = abstractTileType
    }

    fun setTileHp(x: Int, y: Int, hp: Int) {
        if (!isInside(x, y)) return
        tilesHp[getIndex(x, y)] = hp
    }

    fun deactivateTile(x: Int, y: Int, reason: Any? = null) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return
        val context = TileContext(x, y, tilesHp[idx], tileType)
        tileType.onRemoved(x, y, context, reason)
        tiles[idx] = null
        tilesHp[idx] = 0
    }

    fun containsTile(x: Int, y: Int): Boolean {
        return isInside(x, y) && tiles[getIndex(x, y)] != null
    }

    fun callPlace(x: Int, y: Int, item: Item) {
        getTileType(x, y)?.place(x, y, item)
    }

    fun maxHp(x: Int, y: Int) {
        if (!containsTile(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return
        tilesHp[idx] = tileType.maxHp
    }

    fun damageTile(
        x: Int,
        y: Int,
        damage: Int,
    ) {
        if (!containsTile(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return

        val contextBefore = TileContext(x, y, tilesHp[idx], tileType)
        tileType.damage(x, y, damage, contextBefore)

        tilesHp[idx] -= damage

        if (tilesHp[idx] <= 0) {
            val contextBreak = TileContext(x, y, tilesHp[idx], tileType)
            tileType.onRemoved(x, y, contextBreak, "absolute_damage")
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }

    fun mineTile(
        x: Int,
        y: Int,
        mineData: MineData
    ) {
        if (!containsTile(x, y)) return
        val idx = getIndex(x, y)
        val tileType = tiles[idx] ?: return

        if (mineData.power < tileType.blockStrength) return

        val contextBefore = TileContext(x, y, tilesHp[idx], tileType)
        tileType.damage(x, y, mineData.power, contextBefore)

        tilesHp[idx] -= mineData.power

        if (tilesHp[idx] <= 0) {
            val contextBreak = TileContext(x, y, tilesHp[idx], tileType)
            tileType.onMined(x, y, mineData, contextBreak)
            tileType.onRemoved(x, y, contextBreak, mineData)
            tiles[idx] = null
            tilesHp[idx] = 0
        }
    }
}