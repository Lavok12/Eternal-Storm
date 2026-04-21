package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

abstract class AbstractTileData(
    val x: Int,
    val y: Int,
    val dimension: AbstractDimension,
) {
    open fun onRemoved() {}
    open fun onDestroyed() {}
}
