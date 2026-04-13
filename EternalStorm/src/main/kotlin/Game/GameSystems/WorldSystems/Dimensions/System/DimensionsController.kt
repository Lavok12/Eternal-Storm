package la.vok.Game.GameSystems.WorldSystems.Dimensions.System

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class DimensionsController(var gameCycle: GameCycle) : Controller {
    val dimensions = mutableMapOf<String, AbstractDimension>()
    val dimensionsApi = DimensionsApi(this)

    override fun physicTick() {
        dimensions.values.forEach { it.physicTick() }
    }
    
    override fun logicalTick() {
        dimensions.values.forEach { it.logicalTick() }
    }
    
    override fun renderTick() {
        dimensions.values.forEach { it.renderTick() }
    }
}
