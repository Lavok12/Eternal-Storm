package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameController.GlobalSystems.AbstractSimulationSystem
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi

/**
 * Manages global world simulation systems like ecosystem, logic grids, and weather.
 * Orchestrates updates across all active dimensions.
 */
class WorldSimulationManager(var gameCycle: GameCycle) : Controller {
    val entityApi: EntityApi get() = gameCycle.entityApi
    val mapApi: MapApi get() = gameCycle.mapApi
    val itemsApi: ItemsApi get() = gameCycle.itemsApi

    val systems = mutableListOf<AbstractSimulationSystem>()

    init {
        create()
    }

    fun add(system: AbstractSimulationSystem) {
        systems += system
        system.create()
    }

    override fun logicalTick() {
        systems.forEach { it.logicalTick() }
        
        gameCycle.dimensionsController.dimensions.values.forEach { dimension ->
            systems.forEach { system ->
                if (system.shouldSimulate(dimension)) {
                    system.logicalTickDimension(dimension)
                }
            }
        }
    }

    override fun physicTick() {
        systems.forEach { it.physicTick() }
        
        gameCycle.dimensionsController.dimensions.values.forEach { dimension ->
            systems.forEach { system ->
                if (system.shouldSimulate(dimension)) {
                    system.physicTickDimension(dimension)
                }
            }
        }
    }

    override fun renderTick() {
        systems.forEach { it.renderTick() }
        
        gameCycle.dimensionsController.dimensions.values.forEach { dimension ->
            systems.forEach { system ->
                if (system.shouldSimulate(dimension)) {
                    system.renderTickDimension(dimension)
                }
            }
        }
    }

    fun clearSystems() {
        systems.forEach { it.destroy() }
        systems.clear()
    }
}
