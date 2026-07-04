package la.vok.Game.GameContent.LiquidTypes

import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Core.GameControllers.GameController

import la.vok.Game.GameContent.CustomBuffTypes.BuffType

abstract class AbstractLiquidType {
    abstract val id: Byte
    abstract val tag: String
    abstract val color: LColor
    abstract val viscosity: Int // 1 = fast, 5 = slow (skipped update cycles)
    abstract val density: Int   // Heavier liquids (higher density) sink below lighter ones
    open val viscosityForEntities: Float = 0.0f
    open val touchBuff: BuffType? = null

    /**
     * @param amount Liquid amount from 0 to 255
     * @param x World X coordinate (center of the block)
     * @param y World Y coordinate (center of the block)
     */
    open fun renderBackground(
        lg: LGraphics,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        amount: Int,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        lg.fill(color)
        val heightNormalized = amount / 255f
        val drawY = y + (heightNormalized - 1f) * 0.5f * h
        lg.setBlock(x, drawY, w, h * heightNormalized)
    }

    open fun renderForeground(
        lg: LGraphics,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        amount: Int,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        lg.fill(color.r, color.g, color.b, color.a/1.4f)
        val heightNormalized = amount / 255f
        val drawY = y + (heightNormalized - 1f) * 0.5f * h
        lg.setBlock(x, drawY, w, h * heightNormalized)
    }
}
