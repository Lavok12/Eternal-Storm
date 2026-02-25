package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.Entities.EntityApi

class GameCycle(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = gameController.entityController.entityApi
    val mapApi: MapApi get() = gameController.mapController.mapApi

    init {
        create()
    }

    override fun logicalTick() {
        superTick()
    }

    fun physicUpdate() {
        entityApi.getActiveEntities().forEach { it.physicUpdate() }
    }
    fun logicalUpdate() {
        entityApi.getActiveEntities().forEach { it.logicalUpdate() }
    }
    fun renderUpdate() {
        entityApi.getActiveEntities().forEach { it.renderUpdate() }
    }
}