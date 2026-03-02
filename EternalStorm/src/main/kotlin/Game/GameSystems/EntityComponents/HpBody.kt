package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity

class HpBody(entity: Entity) : EntityComponent(entity) {
    var hp: Int = 0
    var maxHp: Int = 0

    fun fullHp() {
        this.hp = maxHp
    }

    fun percentageOfHp() : Float {
        return hp/maxHp.toFloat()
    }
}