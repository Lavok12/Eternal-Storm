package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.LavokLibrary.LGraphics.LGraphics

interface IBlockType {
    val tag: String
    val blockStrength: Int
    val maxHp: Int
    val texture: String
    val drop: DropEntry
    val tags: Set<String>
    fun isFullBlock(): Boolean = false

    fun onInteract(type: BlockInteractionType, context: BlockInteractionContext): Boolean

    fun render(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    )

    fun renderBlockEntity(
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        render(0, 0, lg, positionX, positionY, sizeX, sizeY, dimension, gameController)
    }
}