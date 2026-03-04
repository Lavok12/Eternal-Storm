package la.vok.Game.GameSystems.EntityComponents


import la.vok.Game.GameContent.Entities.Entities.Special.Entity

class BuffController(val entity: Entity) {
    var maxHpMultiplier = 1.0f
    var damageMultiplier = 1.0f
    var speedMultiplier = 1.0f
    var jumpPowerMultiplier = 1.0f
    var diggingTileSpeedMultiplier = 1.0f
    var diggingWallSpeedMultiplier = 1.0f
    var rangedAttackSpeedMultiplier = 1.0f
    var meleeAttackSpeedMultiplier = 1.0f
    var placingBlockSpeedMultiplier = 1.0f
    var placingWallSpeedMultiplier = 1.0f

    var weaponSize = 1.0f

    var knockbackResistance = 0.0f
    var regenerationBonus = 0.0f
    var gravityModifier = 1.0f

    fun reset() {
        maxHpMultiplier = 1.0f
        damageMultiplier = 1.0f
        speedMultiplier = 1.0f
        jumpPowerMultiplier = 1.0f
        diggingTileSpeedMultiplier = 1.0f
        diggingWallSpeedMultiplier = 1.0f
        rangedAttackSpeedMultiplier = 1.0f
        meleeAttackSpeedMultiplier = 1.0f
        placingBlockSpeedMultiplier = 1.0f
        placingWallSpeedMultiplier = 1.0f
        knockbackResistance = 0.0f
        regenerationBonus = 0.0f
        gravityModifier = 1.0f
        weaponSize = 1.0f
    }
}