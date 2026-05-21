package la.vok.Game.GameSystems.WorldSystems.Liquid

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class LiquidController(val dimension: AbstractDimension) : Controller {
    val liquidSystem = LiquidSystem(dimension)

    init {
        create()
    }

    override fun physicTick() {
        liquidSystem.update()
    }
}
