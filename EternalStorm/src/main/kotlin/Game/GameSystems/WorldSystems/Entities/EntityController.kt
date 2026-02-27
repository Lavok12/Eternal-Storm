package la.vok.Game.GameSystems.WorldSystems.Entities

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameController.GameCycle

class EntityController(var gameCycle: GameCycle) : Controller {
    init {
        create()
    }
    var entityApi: EntityApi = EntityApi(this)
    var entitySystem = EntitySystem(this)


    override fun logicalTick() {
        entitySystem.entities.forEach { it.logicalUpdate() }
        entitySystem.flushBuffers()
    }
}