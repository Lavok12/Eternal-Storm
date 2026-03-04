package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.Tiles.System.AbstractWallType

class WallContext(
    var positionX: Int = 0,
    var positionY: Int = 0,
    var hp: Int = 0,
    var wallType: AbstractWallType? = null,
)