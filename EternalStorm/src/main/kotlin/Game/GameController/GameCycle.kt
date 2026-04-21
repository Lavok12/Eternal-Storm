package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Crafts.CraftApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameSystems.WorldSystems.Particles.ParticlesApi
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsApi
import la.vok.Game.GameSystems.WorldSystems.Dimensions.DimensionsController
import la.vok.State.AppState

class GameCycle(var gameController: GameController) : Controller {
    val entityApi: EntityApi = EntityApi(this)
    val mapApi: MapApi = MapApi(this)
    val particlesApi: ParticlesApi = ParticlesApi(this)
    val vfxObjectsApi: VfxObjectsApi = VfxObjectsApi(this)
    var itemsApi = ItemsApi(this)
    var craftApi = CraftApi(this)

    var dimensionsController = DimensionsController(this)
    val dimensionsApi get() = dimensionsController.dimensionsApi
    
    val updateController: UpdateController = UpdateController(this)

    val batchApi: la.vok.Game.GameSystems.RenderSystems.BatchApi = la.vok.Game.GameSystems.RenderSystems.BatchApi(this)

    var collisionSystem = CollisionSystem(this)

    init {
        create()
    }

    override fun logicalTick() {
        collisionSystem.logicalTick()
        dimensionsController.logicalTick()
        updateController.logicalTick()
    }

    override fun physicTick() {
        collisionSystem.physicTick()
        dimensionsController.physicTick()
        updateController.physicTick()
    }
    
    override fun renderTick() {
        dimensionsController.renderTick()
        updateController.renderTick()
    }
}