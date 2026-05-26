package la.vok.Game.GameSystems.EntityComponents.Buffs

data class Modifier(
    val statId: String,
    val value: Float,
    val type: ModifierType
)
