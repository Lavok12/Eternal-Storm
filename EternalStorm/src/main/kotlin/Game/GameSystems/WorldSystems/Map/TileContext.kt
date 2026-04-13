package la.vok.Game.GameContent.Tiles.System

import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class TileContext(
    var positionX: Int = 0,
    var positionY: Int = 0,
    var hp: Int = 0,
    var tileType: AbstractTileType? = null,
    var dimension: AbstractDimension? = null
)