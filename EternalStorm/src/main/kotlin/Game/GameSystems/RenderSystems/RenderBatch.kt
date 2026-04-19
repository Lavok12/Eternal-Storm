package la.vok.Game.GameSystems.RenderSystems

import la.vok.LavokLibrary.LGraphics.LGraphics

class RenderBatch(var chunkX: Int, var chunkY: Int) {
    var isDirty: Boolean = true
    var lg: LGraphics? = null
    var lastUsedTime: Long = System.currentTimeMillis()

    fun clear() {
        lg = null
    }
}
