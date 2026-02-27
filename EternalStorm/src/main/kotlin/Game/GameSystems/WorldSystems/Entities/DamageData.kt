package la.vok.Game.GameSystems.WorldSystems.Entities

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.LavokLibrary.Vectors.Vec2

data class DamageData(
    var value: Int,
    var force: Vec2,
    var sourceId: Long = 0,
    var weapon: HandItem
)
