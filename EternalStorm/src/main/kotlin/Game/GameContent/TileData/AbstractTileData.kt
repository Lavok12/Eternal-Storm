package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockData

abstract class AbstractTileData(
    override var x: Int,
    override var y: Int,
    var dimension: la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension,
    override var disabled: Boolean = false
) : IBlockData {
    val mapApi: MapApi get() = dimension.gameCycle.mapApi
    val gameCycle: GameCycle get() = dimension.gameCycle

    override val wantsPreLogical: Boolean = false
    override val wantsMainLogical: Boolean = false
    override val wantsPostLogical: Boolean = false

    override val wantsPreSecond: Boolean = false
    override val wantsMainSecond: Boolean = false
    override val wantsPostSecond: Boolean = false

    override val wantsPreMinute: Boolean = false
    override val wantsMainMinute: Boolean = false
    override val wantsPostMinute: Boolean = false

    override val wantsPrePhysics: Boolean = false
    override val wantsMainPhysics: Boolean = false
    override val wantsPostPhysics: Boolean = false

    override val wantsPreRender: Boolean = false
    override val wantsMainRender: Boolean = false
    override val wantsPostRender: Boolean = false

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

    override fun onRemoved(reason: Any?) {}
    override fun onDestroyed(reason: Any?) {}
    override fun onRelocated(fromX: Int, fromY: Int, toX: Int, toY: Int) {}
    override fun onPositionChanged(oldX: Int, oldY: Int, newX: Int, newY: Int) {}
}
