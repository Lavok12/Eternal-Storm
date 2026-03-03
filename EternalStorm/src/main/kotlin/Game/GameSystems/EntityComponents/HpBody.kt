package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity

class HpBody(entity: Entity) : EntityComponent(entity) {
    var hp: Int = 0
    var rawMaxHp: Int = 0
    val maxHp: Int get() = (rawMaxHp * entity.buffSystem.maxHpMultiplier).toInt()

    fun fullHp() {
        this.hp = maxHp
    }

    fun percentageOfHp() : Float {
        return hp/maxHp.toFloat()
    }
}