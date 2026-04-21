package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockData

abstract class AbstractTileData(
    override var x: Int,
    override var y: Int,
    var dimension: AbstractDimension
) : IBlockData {
    override val wantsLogicalUpdate: Boolean = false
    override val wantsSecondUpdate: Boolean = false
    override val wantsMinuteUpdate: Boolean = false
    override val wantsPhysicsUpdate: Boolean = false
    override val wantsRenderUpdate: Boolean = false

    override fun preLogicalTick() {}
    override fun logicalTick() {}
    override fun postLogicalTick() {}

    override fun preSecondUpdate() {}
    override fun secondUpdate() {}
    override fun postSecondUpdate() {}

    override fun preMinuteUpdate() {}
    override fun minuteUpdate() {}
    override fun postMinuteUpdate() {}

    override fun physicsUpdate() {}
    override fun prePhysicsUpdate() {}
    override fun postPhysicsUpdate() {}

    override fun renderUpdate() {}
    override fun preRenderUpdate() {}
    override fun postRenderUpdate() {}

    override fun onRemoved() {}
    override fun onDestroyed() {}
    override fun onPositionChanged(oldX: Int, oldY: Int, newX: Int, newY: Int) {}
}
