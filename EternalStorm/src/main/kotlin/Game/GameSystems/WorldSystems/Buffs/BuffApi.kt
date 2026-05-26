package la.vok.Game.GameSystems.WorldSystems.Buffs

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.BuffController
import la.vok.Game.GameSystems.EntityComponents.Buffs.BuffRegistry
import la.vok.Game.GameContent.CustomBuffTypes.BuffType

class BuffApi(val gameCycle: GameCycle) {

    /**
     * Adds a buff to an entity using its tag.
     * The buff must be registered in the BuffRegistry.
     */
    fun addBuff(entity: Entity, tag: String) {
        val buffType = BuffRegistry.get(tag) ?: return
        entity.getComponent<BuffController>()?.addBuff(buffType)
    }

    /**
     * Adds a buff to an entity using its tag and a custom duration in ticks.
     */
    fun addBuff(entity: Entity, tag: String, ticks: Int) {
        val buffType = BuffRegistry.get(tag) ?: return
        entity.getComponent<BuffController>()?.addBuff(buffType, ticks)
    }

    /**
     * Directly adds a BuffType to an entity.
     */
    fun addBuff(entity: Entity, buffType: BuffType) {
        entity.getComponent<BuffController>()?.addBuff(buffType)
    }

    /**
     * Removes a buff from an entity by its tag.
     */
    fun removeBuff(entity: Entity, tag: String) {
        entity.getComponent<BuffController>()?.removeBuff(tag)
    }

    /**
     * Clears all buffs from an entity.
     */
    fun clearBuffs(entity: Entity) {
        entity.getComponent<BuffController>()?.clearBuffs()
    }
}
