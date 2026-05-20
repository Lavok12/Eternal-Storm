package la.vok.Game.GameController.GlobalSystems

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameController.GlobalSystemsController

abstract class AbstractGlobalSystem(var globalSystemsController: GlobalSystemsController) {
    fun create() {}

    fun logicalTickDimension(dimension: AbstractDimension) {}
    fun logicalTick() {}
    fun physicTickDimension(dimension: AbstractDimension) {}
    fun physicTick() {}
    fun renderTickDimension(dimension: AbstractDimension) {}
    fun renderTick() {}

    fun destroy() {}
}