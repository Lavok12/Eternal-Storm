package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameSystems.WorldSystems.Crafts.CraftApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityController
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameSystems.WorldSystems.Particles.ParticleController
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsApi
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsController

class GameCycle(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = entityController.entityApi
    val mapApi: MapApi get() = mapController.mapApi
    val vfxObjectsApi: VfxObjectsApi get() = vfxObjectsController.vfxObjectsApi

    var mapController = MapController(this)
    var entityController = EntityController(this)
    var vfxObjectsController = VfxObjectsController(this)
    var particleController = ParticleController(this)

    var itemsApi = ItemsApi(this)
    var craftApi = CraftApi(this)
    
    var collisionSystem = CollisionSystem(this)

    init {
        create()
    }

    override fun logicalTick() {
        entityApi.getActiveEntities().forEach { it.logicalUpdate() }

        collisionSystem.logicalTick()
        mapController.logicalTick()
        entityController.logicalTick()
        vfxObjectsController.logicalTick()
    }

    override fun physicTick() {
        collisionSystem.physicTick()
        mapController.physicTick()
        entityController.physicTick()
        vfxObjectsController.physicTick()
        particleController.physicTick()

        entityApi.getActiveEntities().toList().forEach {
            if (!it.isDead) {
                it.physicUpdate()
            }
        }
    }
    override fun renderTick() {
        mapController.renderTick()
        entityController.renderTick()
        mapController.renderTick()
        vfxObjectsController.renderTick()

        entityApi.getActiveEntities().toList().forEach {
            if (!it.isDead) {
                it.renderUpdate()
            }
        }
    }
}