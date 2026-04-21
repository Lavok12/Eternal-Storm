package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.State.AppState
import kotlin.random.Random

class DistributeTileData(x: Int, y: Int, dimension: AbstractDimension) : AbstractTileData(x, y, dimension) {
    override val wantsSecondUpdate: Boolean = true
    override fun secondUpdate() {
        val randomDirection = Random.nextInt()%4

        when (randomDirection) {
            0 -> {
                if (mapApi.getTileType(dimension, x+1, y) == null) return
                mapApi.copyTile(dimension, x, y, x+1, y)
            }
            1 -> {
                if (mapApi.getTileType(dimension, x, y+1) == null) return
                mapApi.copyTile(dimension, x, y, x, y+1)
            }
            2 -> {
                if (mapApi.getTileType(dimension, x-1, y) == null) return
                mapApi.copyTile(dimension, x, y, x-1, y)
            }
            3 -> {
                if (mapApi.getTileType(dimension, x, y-1) == null) return
                mapApi.copyTile(dimension, x, y, x, y-1)
            }
        }
    }
}