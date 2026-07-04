package la.vok.Game.GameSystems.EntityComponents.Buffs

import la.vok.Game.GameContent.CustomBuffTypes.BuffType

class BuffInstance(val type: BuffType, customTicks: Int? = null) {
    var ticksRemaining: Int = customTicks ?: type.maxTicks
    val maxTicks: Int = customTicks ?: type.maxTicks
    var tickCount: Int = 0
    
    val progress: Float 
        get() = if (maxTicks > 0) ticksRemaining.toFloat() / maxTicks.toFloat() else 0f
        
    fun tick() {
        tickCount++
        type.tickCount = tickCount
        if (ticksRemaining > 0) {
            ticksRemaining--
        }
    }
    
    fun isExpired(): Boolean = maxTicks > 0 && ticksRemaining <= 0
}
