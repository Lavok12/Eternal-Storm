package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.ContentList.BuffTags
import la.vok.Game.GameContent.ContentList.StatTags

class HpBody(entity: Entity) : EntityComponent(entity) {
    var hp: Int = 0
    var rawMaxHp: Int = 0
    val maxHp: Int get() = (rawMaxHp * entity.buffController.getStat(StatTags.MAX_HP)).toInt()

    override fun onSpawn() {
        fullHp()
    }

    override fun onEvent(event: EntityEvent) {
        if (event is EntityEvent.Damage) {
            // Damage handling logic could be moved here if we want components to be self-sufficient
        }
    }

    fun fullHp() {
        this.hp = maxHp
    }

    fun percentageOfHp() : Float {
        if (maxHp <= 0) return 0f
        return hp / maxHp.toFloat()
    }
}