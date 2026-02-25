package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.EntitiesList
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class GameLoader(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = gameController.entityController.entityApi
    val mapApi: MapApi get() = gameController.mapController.mapApi
    init {
        create()
    }
    override fun logicalTick() {
        superTick()
    }
    fun load() {
        AppState.logger.info("Load Game")
        gameController.mapController.createMap()
        entityApi.addInSystemWithId(-1, entityApi.getRegisteredEntity(EntitiesList.player), gameController.mainCamera.pos.copy())
        gameController.playerId = -1
    }
}