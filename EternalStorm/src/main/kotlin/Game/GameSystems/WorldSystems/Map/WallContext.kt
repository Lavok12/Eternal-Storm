package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class WallContext(
    var positionX: Int = 0,
    var positionY: Int = 0,
    var hp: Int = 0,
    var wallType: AbstractWallType? = null,
    var dimension: AbstractDimension? = null
)