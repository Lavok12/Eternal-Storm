package la.vok.Game.GameContent.WallData

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockData

abstract class AbstractWallData(
    override var x: Int,
    override var y: Int,
    var dimension: la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension,
    override var disabled: Boolean = false
) : IBlockData
