package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class FrameTileData(x: Int, y: Int, dimension: AbstractDimension) : AbstractTileData(x, y, dimension) {
    var itemContainer = ItemContainer(dimension.gameCycle, 1)

    override fun onDestroyed() {
        dropItem()
    }

    fun dropItem() {
        if (getItem() != null) {
            dimension.gameCycle.itemsApi.spawnItemEntity(dimension, itemContainer.getItem(0)!!, (x v y) + Vec2(0.5f), true)
            itemContainer.clear()
        }
    }
    fun setItem(item: Item?) {
        dropItem()
        if (item == null) return
        if (item.count < 1) return
        item.count -= 1
        itemContainer.addItem(item.copy().apply { count = 1 })
    }
    fun getItem() : Item? {
        return itemContainer.getItem(0)
    }
}
