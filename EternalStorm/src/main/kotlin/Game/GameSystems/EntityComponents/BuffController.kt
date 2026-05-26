package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.EntityComponents.Buffs.BuffInstance
import la.vok.Game.GameContent.CustomBuffTypes.BuffType
import la.vok.Game.GameSystems.EntityComponents.Buffs.ModifierType

class BuffController(val entity: Entity) : IEntityComponent {
    private val activeBuffs = mutableMapOf<String, BuffInstance>()
    
    override fun onPhysicUpdate() {
        physicTick()
    }
    
    // Caches for stats
    private val cachedMultipliers = mutableMapOf<String, Float>()
    private val cachedAdditions = mutableMapOf<String, Float>()
    private val activeFlags = mutableSetOf<String>()

    /**
     * Called every physics tick to update buff durations.
     */
    fun physicTick() {
        if (activeBuffs.isEmpty()) return
        
        val iterator = activeBuffs.values.iterator()
        var changed = false
        
        while (iterator.hasNext()) {
            val instance = iterator.next()
            
            // 1. onTick hook
            instance.type.onTick?.invoke(entity)
            
            instance.tick()
            if (instance.isExpired()) {
                // 2. onRemove hook (expired = true)
                instance.type.onRemove?.invoke(entity, true)
                iterator.remove()
                changed = true
            }
        }
        
        if (changed) {
            recalculate()
        }
    }

    /**
     * Adds a buff or refreshes its duration if already present.
     */
    fun addBuff(type: BuffType, customTicks: Int? = null) {
        // Tag-based check ensures no duplicates (refreshes duration)
        activeBuffs[type.tag] = BuffInstance(type, customTicks)
        
        // onApply hook
        type.onApply?.invoke(entity)
        
        recalculate()
    }

    /**
     * Forcefully removes a buff by tag.
     */
    fun removeBuff(tag: String) {
        val removed = activeBuffs.remove(tag)
        if (removed != null) {
            // onRemove hook (expired = false)
            removed.type.onRemove?.invoke(entity, false)
            recalculate()
        }
    }

    /**
     * Clears all active buffs.
     */
    fun clearBuffs() {
        activeBuffs.values.forEach { it.type.onRemove?.invoke(entity, false) }
        activeBuffs.clear()
        recalculate()
    }

    /**
     * Recalculates all stat multipliers and additions from scratch.
     */
    private fun recalculate() {
        cachedMultipliers.clear()
        cachedAdditions.clear()
        activeFlags.clear()
        
        activeBuffs.values.forEach { instance ->
            instance.type.modifiers.forEach { modifier ->
                when (modifier.type) {
                    ModifierType.ADD -> {
                        cachedAdditions[modifier.statId] = (cachedAdditions[modifier.statId] ?: 0f) + modifier.value
                    }
                    ModifierType.MULTIPLY -> {
                        cachedMultipliers[modifier.statId] = (cachedMultipliers[modifier.statId] ?: 1f) * modifier.value
                    }
                    ModifierType.FLAG -> {
                        if (modifier.value > 0.5f) {
                            activeFlags.add(modifier.statId)
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the final value of a stat (base * multiplier + addition).
     */
    fun getStat(statId: String, baseValue: Float = 1f): Float {
        val mult = cachedMultipliers[statId] ?: 1f
        val add = cachedAdditions[statId] ?: 0f
        return baseValue * mult + add
    }

    /**
     * Checks if a specific boolean flag is active.
     */
    fun hasFlag(flagId: String): Boolean = activeFlags.contains(flagId)

    fun getActiveBuffs(): Collection<BuffInstance> = activeBuffs.values
}