package la.vok.Game.GameContent.CustomBuffTypes

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.EntityComponents.Buffs.Modifier

class BuffType(
    val tag: String,
    val maxTicks: Int,
    val modifiers: List<Modifier> = listOf(),

    val onApply: ((Entity) -> Unit)? = null,
    val onTick: ((Entity) -> Unit)? = null,
    val onRemove: ((Entity, Boolean) -> Unit)? = null
) {
    var tickCount: Int = 0
}