package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.SoftCamera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.ClientContent.Windows.WDevPanel
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameController.GameLoader
import la.vok.Game.GameController.PlayerControl
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class GameController(var coreController: CoreController) : SceneController {
    var mainCamera = SoftCamera(750 v 803, 25f, 0.995f)

    var effectLayersController = EffectLayersController(this)
    var gameLoader = GameLoader(this)
    var gameRender = GameRender(this)
    var gameCycle = GameCycle(this)

    val playerControl = PlayerControl(this)
    val playerDimension get() = playerControl.getPlayerEntity()?.dimension

    private val systems = mutableListOf<Controller>()

    init {
        systems.add(playerControl)
        systems.add(gameCycle)
        systems.add(effectLayersController)
        systems.add(gameRender)
        create()
    }

    var wGamePanel: WGamePanel? = null
    var wDevPanel: WDevPanel? = null

    var playerId = -1L

    override fun deactivate(coreController: CoreController) {
        super.deactivate(coreController)
        wGamePanel = null
    }

    override fun initWindows() {
        coreController.windowsManager.destroy()

        wGamePanel = WGamePanel(coreController.windowsManager, this).apply {
            windowSize = AppState.lg.frameSize
        }

        coreController.windowsManager.addWindow(wGamePanel!!)

    }
    override fun logicalTick() {
        systems.forEach { it.logicalTick() }
        mainCamera.updateCamera()
    }

    override fun physicTick() {
        systems.forEach { it.physicTick() }
    }

    override fun renderTick() {
        systems.forEach { it.renderTick() }
    }
    fun render(lg: LGraphics) {
        lg.noStroke()
        gameRender.render(lg, mainCamera)
    }


    override fun start() {
        gameLoader.initWorldSystems()
        gameLoader.load()
    }
}