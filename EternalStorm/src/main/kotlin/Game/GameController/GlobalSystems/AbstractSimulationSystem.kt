package la.vok.Game.GameController.GlobalSystems

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameController.WorldSimulationManager

abstract class AbstractSimulationSystem(var simulationManager: WorldSimulationManager) {
    open fun create() {}

    /**
     * Called every logic tick for each dimension.
     */
    open fun logicalTickDimension(dimension: AbstractDimension) {}

    /**
     * Called once per logic tick globally.
     */
    open fun logicalTick() {}

    open fun physicTickDimension(dimension: AbstractDimension) {}
    open fun physicTick() {}

    open fun renderTickDimension(dimension: AbstractDimension) {}
    open fun renderTick() {}

    /**
     * Filter to determine if this system should run for a specific dimension.
     * By default, runs for all dimensions.
     */
    open fun shouldSimulate(dimension: AbstractDimension): Boolean = true

    open fun destroy() {}
}
