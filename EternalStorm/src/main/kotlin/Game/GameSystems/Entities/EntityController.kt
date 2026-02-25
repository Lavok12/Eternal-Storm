package la.vok.Game.GameSystems.Entities

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameSystems.Map.MapGenerator

class EntityController(var gameController: GameController) : Controller {
    init {
        create()
    }
    var entityApi: EntityApi = EntityApi(this)
    var entitySystem = EntitySystem(this)

    override fun logicalTick() {
        superTick()
    }
}