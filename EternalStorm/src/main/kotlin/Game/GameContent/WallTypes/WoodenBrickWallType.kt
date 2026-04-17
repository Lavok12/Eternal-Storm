package la.vok.Game.GameContent.WallTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics

class WoodenBrickWallType() : AbstractWallType() {
    override val tag: String = WallList.wooden_brick_wall
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = "wooden_bricks.png"
    override val drop: DropEntry = SingleDrop(ItemsList.wooden_brick_wall)
    override val tags = setOf(BlockTags.WOOD)

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
