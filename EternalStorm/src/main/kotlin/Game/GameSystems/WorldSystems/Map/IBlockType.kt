package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.LavokLibrary.LGraphics.LGraphics
import processing.core.PImage

interface IBlockType {
    val tag: String
    val blockStrength: Int
    val maxHp: Int
    val texture: String
    val drop: DropEntry
    val tags: Set<String>
    fun isFullBlock(): Boolean = false
    fun canBePolluted(): Boolean = isFullBlock()

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

    fun getPollutionStrength(targetBlock: IBlockType, dimension: AbstractDimension, x: Int, y: Int): Float {
        return 0f
    }

    fun getPollutionTexture(
        dimension: AbstractDimension,
        polluterX: Int, polluterY: Int,
        targetX: Int, targetY: Int,
        gameController: GameController
    ): PImage? {
        return gameController.coreController.spriteLoader.getValue(texture)
    }

    fun renderPollution(
        pointX: Int, pointY: Int,
        lg: LGraphics,
        positionX: Float, positionY: Float,
        sizeX: Float, sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {}

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