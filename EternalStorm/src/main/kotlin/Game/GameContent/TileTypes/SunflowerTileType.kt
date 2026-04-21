package la.vok.Game.GameContent.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class SunflowerTileType : AbstractTileType() {
    override val tag: String = TilesList.sunflower
    override var collisionType = CollisionType.NONE
    override val texture: String = AppState.res("sunflower.png")
    override val width: Int = 1
    override val height: Int = 3
    override val placeType = TilePlaceType.ON_TILE
    override val drop: DropEntry = SingleDrop(ItemsList.sunflower)
    
    override val renderConfig = TileRenderConfig(
        useSquareRender = true,
        sizeMultiplier = 3.45f,
        renderDelta = 0 v -0.25f,
        AOShadow = false
    )

    override fun render(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        renderConfig.flipX = ((pointX * 374761393L xor pointY * 668265263L) shr 16).toInt() % 2 == 0
        super.render(pointX, pointY, lg, positionX, positionY, sizeX, sizeY, dimension, gameController)
        renderConfig.flipX = false
    }
    override val blockStrength = 0
    override val maxHp = 1
}
