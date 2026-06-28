package la.vok.Game.GameContent.WallTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState

class DirtWallType() : AbstractWallType() {
    override val tag: String = WallList.dirt_wall
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = AppState.res("dirtTexture.jpg")
    override val drop: DropEntry = SingleDrop(ItemsList.dirt_wall)

    override fun getPollutionStrength(
        targetBlock: la.vok.Game.GameSystems.WorldSystems.Map.IBlockType,
        dimension: AbstractDimension,
        x: Int, y: Int
    ): Float {
        if (targetBlock.tag == this.tag) return 0f
        return 1f
    }

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
        lg.setTint(170f)
        super.render(pointX, pointY, lg, positionX, positionY, sizeX, sizeY, dimension, gameController)
        lg.noTint()
    }
}