package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.LavokLibrary.LGraphics.LGraphics

interface IBlockData {
    var x: Int
    var y: Int
    var disabled: Boolean

    val wantsPreLogical: Boolean get() = false
    val wantsMainLogical: Boolean get() = false
    val wantsPostLogical: Boolean get() = false

    val wantsPreSecond: Boolean get() = false
    val wantsMainSecond: Boolean get() = false
    val wantsPostSecond: Boolean get() = false

    val wantsPreMinute: Boolean get() = false
    val wantsMainMinute: Boolean get() = false
    val wantsPostMinute: Boolean get() = false

    val wantsPrePhysics: Boolean get() = false
    val wantsMainPhysics: Boolean get() = false
    val wantsPostPhysics: Boolean get() = false

    val wantsPreRender: Boolean get() = false
    val wantsMainRender: Boolean get() = false
    val wantsPostRender: Boolean get() = false

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

    fun onRemoved(reason: Any? = null) {}
    fun onDestroyed(reason: Any? = null) {}
    fun onRelocated(fromX: Int, fromY: Int, toX: Int, toY: Int) {}
    fun onPositionChanged(oldX: Int, oldY: Int, newX: Int, newY: Int) {}
}
