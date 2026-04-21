package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.State.AppState

class SmallGrassTileType : AbstractTileType() {
    override val tag: String = TilesList.small_grass
    override var collisionType = CollisionType.NONE
    override val texture: String = AppState.res("small_grass.png")
    override val placeType = TilePlaceType.ON_TILE
    override val canBeReplaced = true
    override val renderConfig = TileRenderConfig(AOShadow = false)
    
    override val blockStrength: Int = 0
    override val maxHp: Int = 1
}
