package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class ChestTileData(x: Int, y: Int, dimension: AbstractDimension) : AbstractTileData(x, y, dimension) {
    val itemContainer = ItemContainer(dimension.gameCycle, 20) // 20 слотов для сундука

    override fun onDestroyed(reason: Any?) {
        dropItems()
    }

    fun dropItems() {
        for (i in 0 until itemContainer.size) {
            val item = itemContainer.getItem(i)
            if (item != null) {
                dimension.gameCycle.itemsApi.spawnItemEntity(dimension, item, (x v y) + Vec2(0.5f), true)
            }
        }
        itemContainer.clear()
    }
}
