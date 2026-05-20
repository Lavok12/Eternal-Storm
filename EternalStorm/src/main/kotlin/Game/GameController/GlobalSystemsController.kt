package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameController.GlobalSystems.AbstractGlobalSystem
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi

class GlobalSystemsController(var gameCycle: GameCycle) : Controller {
    val entityApi: EntityApi get() = gameCycle.entityApi
    val mapApi: MapApi get() = gameCycle.mapApi
    val itemsApi: ItemsApi get() = gameCycle.itemsApi

    var systems = mutableListOf<AbstractGlobalSystem>()

    init {
        create()
    }

    fun add(system: AbstractGlobalSystem) {
        systems += system
    }

    override fun logicalTick() {
        systems.forEach { it.logicalTick() }
        gameCycle.dimensionsController.dimensions.values.forEach { dimension ->
            systems.forEach { system ->
                system.logicalTickDimension(dimension)
            }
        }
    }

    override fun physicTick() {
        systems.forEach { it.physicTick() }
        gameCycle.dimensionsController.dimensions.values.forEach { dimension ->
            systems.forEach { system ->
                system.physicTickDimension(dimension)
            }
        }
    }

    override fun renderTick() {
        systems.forEach { it.renderTick() }
        gameCycle.dimensionsController.dimensions.values.forEach { dimension ->
            systems.forEach { system ->
                system.renderTickDimension(dimension)
            }
        }
    }

    fun clearSystems() {
        systems.forEach { it.destroy() }
        systems.clear()
    }
}