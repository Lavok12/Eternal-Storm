package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.Game.GameSystems.Entities.EntityController

class GameCycle(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = entityController.entityApi
    val mapApi: MapApi get() = mapController.mapApi

    var mapController = MapController(this)
    var entityController = EntityController(this)
    var collisionSystem = CollisionSystem(this)

    init {
        create()
    }

    override fun logicalTick() {
        entityApi.getActiveEntities().forEach { it.logicalUpdate() }

        collisionSystem.logicalTick()
        mapController.logicalTick()
        entityController.logicalTick()
    }

    override fun physicTick() {
        collisionSystem.physicTick()
        mapController.physicTick()
        entityController.physicTick()

        entityApi.getActiveEntities().forEach {
            if (!it.isDead) {
                it.physicUpdate()
            }
        }
    }
    override fun renderTick() {
        mapController.renderTick()
        entityController.renderTick()

        entityApi.getActiveEntities().forEach {
            if (!it.isDead) {
                it.renderUpdate()
            }
        }
    }
}