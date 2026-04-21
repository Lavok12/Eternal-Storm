package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.LavokLibrary.LGraphics.LGraphics

interface IBlockData {
    var x: Int
    var y: Int

    val wantsLogicalUpdate: Boolean get() = false
    val wantsSecondUpdate: Boolean get() = false
    val wantsMinuteUpdate: Boolean get() = false
    val wantsPhysicsUpdate: Boolean get() = false
    val wantsRenderUpdate: Boolean get() = false

    fun preLogicalTick() {}
    fun logicalTick() {}
    fun postLogicalTick() {}

    fun preSecondUpdate() {}
    fun secondUpdate() {}
    fun postSecondUpdate() {}

    fun preMinuteUpdate() {}
    fun minuteUpdate() {}
    fun postMinuteUpdate() {}

    fun prePhysicsUpdate() {}
    fun physicsUpdate() {}
    fun postPhysicsUpdate() {}

    fun preRenderUpdate() {}
    fun renderUpdate() {}
    fun postRenderUpdate() {}

    fun onRemoved() {}
    fun onDestroyed() {}
    fun onPositionChanged(oldX: Int, oldY: Int, newX: Int, newY: Int) {}
}
