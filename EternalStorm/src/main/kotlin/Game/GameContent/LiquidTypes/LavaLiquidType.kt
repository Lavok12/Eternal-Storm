package la.vok.Game.GameContent.LiquidTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor

class LavaLiquidType : AbstractLiquidType() {
    override val id: Byte = LiquidList.LAVA_ID
    override val tag: String = LiquidList.lava
    override val color: LColor = LColor(255f, 80f, 0f, 240f)
    override val viscosity: Int = 4
    override val density: Int = 20

    override fun renderBackground(
        lg: LGraphics,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        amount: Int,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        val heightNormalized = amount / 255f
        val drawY = y + (heightNormalized - 1f) * 0.5f * h
        lg.setImage(gameController.coreController.spriteLoader.getValue("lava.png"),x, drawY, w, h * heightNormalized)
    }

    override fun renderForeground(
        lg: LGraphics,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        amount: Int,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        lg.setTint(255f, 208f)
        val heightNormalized = amount / 255f
        val drawY = y + (heightNormalized - 1f) * 0.5f * h
        lg.setImage(gameController.coreController.spriteLoader.getValue("lava.png"),x, drawY, w, h * heightNormalized)
        lg.noTint()
    }
}
