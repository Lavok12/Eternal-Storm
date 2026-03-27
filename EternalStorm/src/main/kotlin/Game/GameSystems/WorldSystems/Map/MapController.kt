package la.vok.Game.GameContent.Map

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Map.MapGeneration.MapGenerator

class MapController(var gameCycle: GameCycle) : Controller {
    init {
        create()
    }
    var mapApi: MapApi = MapApi(this)

    var mapSystem = MapSystem(this)
    var mapGenerator = MapGenerator(this)

    fun createMap() {
        mapGenerator.create(mapSystem)
    }
}