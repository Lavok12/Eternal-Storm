package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.LavokLibrary.Vectors.Vec2

data class MineData(
    var value: Int,
    var power: Int,
    var sourceId: Long? = 0,
    var instrument: HandItem?,
    var item: Item?
)
