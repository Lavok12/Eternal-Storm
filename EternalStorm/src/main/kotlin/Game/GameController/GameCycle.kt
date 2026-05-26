package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.RenderSystems.BatchApi
import la.vok.Game.GameSystems.WorldSystems.Crafts.CraftApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameSystems.WorldSystems.Particles.ParticlesApi
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsApi
import la.vok.Game.GameSystems.WorldSystems.Dimensions.DimensionsController
import la.vok.Game.GameSystems.WorldSystems.Liquid.LiquidApi
import la.vok.Game.GameSystems.WorldSystems.Players.PlayerApi
import la.vok.Game.GameSystems.WorldSystems.UI.UiApi
import la.vok.Game.GameSystems.WorldSystems.Buffs.BuffApi

class GameCycle(val gameController: GameController) : Controller {
    val gameContext = GameContext(gameController, this)
    var logicalTicks: Long = 0L
    var physicTicks: Long = 0L
    var renderTicks: Long = 0L

    val entityApi = EntityApi(this)
    val mapApi = MapApi(this)
    val particlesApi = ParticlesApi(this)
    val vfxObjectsApi = VfxObjectsApi(this)
    val itemsApi = ItemsApi(this)
    val craftApi = CraftApi(this)
    val playerApi = PlayerApi(this)
    val batchApi = BatchApi(this)
    val liquidApi = LiquidApi(this)
    val uiApi = UiApi(gameController)
    val buffApi = BuffApi(this)

    val dimensionsController = DimensionsController(this)
    val worldSimulationManager = WorldSimulationManager(this)
    val updateController = UpdateController(this)

    val collisionSystem = CollisionSystem(this)

    val dimensionsApi get() = dimensionsController.dimensionsApi

    private val systems = mutableListOf<Controller>()

    init {
        gameContext.apply {
            entityApi = this@GameCycle.entityApi
            mapApi = this@GameCycle.mapApi
            particlesApi = this@GameCycle.particlesApi
            vfxObjectsApi = this@GameCycle.vfxObjectsApi
            itemsApi = this@GameCycle.itemsApi
            craftApi = this@GameCycle.craftApi
            dimensionsApi = this@GameCycle.dimensionsApi
            playerApi = this@GameCycle.playerApi
            liquidApi = this@GameCycle.liquidApi
            uiApi = this@GameCycle.uiApi
            buffApi = this@GameCycle.buffApi
        }

        systems.add(worldSimulationManager)
        systems.add(collisionSystem)
        systems.add(dimensionsController)
        systems.add(updateController)

        create()
    }

    override fun logicalTick() {
        logicalTicks++
        systems.forEach { it.logicalTick() }
    }

    override fun physicTick() {
        physicTicks++
        systems.forEach { it.physicTick() }
    }

    override fun renderTick() {
        renderTicks++
        systems.forEach { it.renderTick() }
    }
}