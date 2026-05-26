package la.vok.Game.GameSystems.EntityComponents.Buffs

import la.vok.Game.GameContent.CustomBuffTypes.BuffType

object BuffRegistry {
    private val registry = mutableMapOf<String, BuffType>()

    fun register(buff: BuffType) {
        registry[buff.tag] = buff
    }

    fun get(tag: String): BuffType? = registry[tag]
    
    fun getAll(): Collection<BuffType> = registry.values
}
