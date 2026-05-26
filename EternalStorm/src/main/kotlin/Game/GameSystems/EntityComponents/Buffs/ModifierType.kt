package la.vok.Game.GameSystems.EntityComponents.Buffs

enum class ModifierType {
    /** Adds to the base value (e.g. +10 speed) */
    ADD,
    /** Multiplies the base value (e.g. x1.5 damage) */
    MULTIPLY,
    /** Sets a boolean flag (value > 0.5f is true) */
    FLAG
}
