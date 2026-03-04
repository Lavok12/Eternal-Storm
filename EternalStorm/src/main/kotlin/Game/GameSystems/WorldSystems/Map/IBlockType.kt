package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.NothingDrop

interface IBlockType {
    val tag: String
    val blockStrength: Int
    val maxHp: Int
    val texture: String
    val drop: DropEntry
}