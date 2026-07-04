package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameContent.ContentList.StatTags
import la.vok.Game.GameSystems.EntityComponents.Buffs.BuffRegistry
import la.vok.Game.GameContent.ContentList.BuffTags
import la.vok.LavokLibrary.Vectors.Vec2

class OxygenComponent(entity: Entity) : EntityComponent(entity) {
    var rawMaxOxygen = 300f
    
    val maxOxygen: Float
        get() = rawMaxOxygen * entity.buffController.getStat(StatTags.MAX_OXYGEN, 1f)
        
    var currentOxygen = 300f
    
    var drownDamageInterval = 60
    var drownDamage = 10
    var canBreatheUnderwater = false

    override fun onPhysicUpdate() {
        val maxO2 = maxOxygen
        // Ensure current oxygen doesn't exceed maximum when stats change
        if (currentOxygen > maxO2) {
            currentOxygen = maxO2
        }

        val detector = entity.getComponent<LiquidDetectorComponent>()
        val isHeadSubmerged = detector?.isHeadInLiquid ?: false
        
        if (isHeadSubmerged && !canBreatheUnderwater) {
            val drainRate = entity.buffController.getStat(StatTags.OXYGEN_DRAIN, 1f)
            if (currentOxygen > 0) {
                currentOxygen = (currentOxygen - drainRate).coerceAtLeast(0f)
            }
            
            if (currentOxygen <= 0f) {
                val drowningBuff = BuffRegistry.get(BuffTags.DROWNING)
                if (drowningBuff != null) {
                    val hasBuff = entity.buffController.getActiveBuffs().any { it.type.tag == BuffTags.DROWNING }
                    if (!hasBuff) {
                        entity.buffController.addBuff(drowningBuff)
                    }
                }
            } else {
                entity.buffController.removeBuff(BuffTags.DROWNING)
            }
        } else {
            // Restore oxygen
            val regenRate = entity.buffController.getStat(StatTags.OXYGEN_REGEN, 3f)
            if (currentOxygen < maxO2) {
                currentOxygen = (currentOxygen + regenRate).coerceAtMost(maxO2)
            }
            entity.buffController.removeBuff(BuffTags.DROWNING)
        }
    }
}
