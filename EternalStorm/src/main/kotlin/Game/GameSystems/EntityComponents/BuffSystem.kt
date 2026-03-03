package la.vok.Game.GameSystems.EntityComponents


import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.LavokLibrary.Vectors.v

class BuffSystem(val entity: Entity) {
    var maxHpMultiplier = 1.0f
    var damageMultiplier = 1.0f
    var speedMultiplier = 1.0f
    var jumpPowerMultiplier = 1.0f
    var diggingSpeedMultiplier = 1.0f
    var rangedAttackSpeedMultiplier = 1.0f
    var meleeAttackSpeedMultiplier = 1.0f
    var placingSpeedMultiplier = 1.0f

    var knockbackResistance = 0.0f
    var regenerationBonus = 0.0f
    var gravityModifier = 1.0f

    fun reset() {
        maxHpMultiplier = 1.0f
        damageMultiplier = 1.0f
        speedMultiplier = 1.0f
        jumpPowerMultiplier = 1.0f
        diggingSpeedMultiplier = 1.0f
        rangedAttackSpeedMultiplier = 1.0f
        meleeAttackSpeedMultiplier = 1.0f
        placingSpeedMultiplier = 1.0f
        knockbackResistance = 0.0f
        regenerationBonus = 0.0f
        gravityModifier = 1.0f
    }
}