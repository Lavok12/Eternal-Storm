package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.SoftCamera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.Game.ClientContent.Windows.WDevPanel
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameController.GameLoader
import la.vok.Game.GameController.PlayerControl
import la.vok.Game.GameSystems.EffectLayers.AOTiles
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.Menu.MenuContent.Windows.WMenuPanel
import la.vok.Menu.MenuController.MenuController
import la.vok.State.AppState

class GameController(var coreController: CoreController) : SceneController {
    var mainCamera = SoftCamera(50 v 73, 25f, 0.995f)

    var effectLayersController = EffectLayersController(this)
    var gameLoader = GameLoader(this)
    var gameRender = GameRender(this)
    var gameCycle = GameCycle(this)

    val playerControl = PlayerControl(this)
    val playerDimension get() = playerControl.getPlayerEntity()?.dimension

    init {
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
        playerControl.logicalTick()
        gameCycle.logicalTick()
        effectLayersController.logicalTick()
        gameRender.logicalTick()

        mainCamera.updateCamera()
    }

    override fun physicTick() {
        playerControl.physicTick()
        gameCycle.physicTick()
        effectLayersController.physicTick()
        gameRender.physicTick()
    }

    override fun renderTick() {
        gameCycle.renderTick()
        gameRender.renderTick()
    }
    fun render(lg: LGraphics) {
        lg.noStroke()
        gameRender.render(lg, mainCamera)
    }


    override fun start() {
        gameLoader.load()
    }
}