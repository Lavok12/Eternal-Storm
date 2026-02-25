package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.SoftCamera
import la.vok.Core.CoreContent.Camera.StandartCamera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameController.GameLoader
import la.vok.Game.GameController.PlayerControl
import la.vok.Game.GameSystems.Entities.EntityController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class GameController(var coreController: CoreController) : SceneController {
    var mainCamera = SoftCamera(50 v 73, 25f, 0.995f)

    var effectLayersController = EffectLayersController(this)
    var gameLoader = GameLoader(this)
    var gameRender = GameRender(this)
    var gameCycle = GameCycle(this)

    var mapController = MapController(this)
    var entityController = EntityController(this)
    val playerControl = PlayerControl(this)

    init {
        create()
    }

    var wGamePanel: WGamePanel? = null
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
        super.superTick()
        mapController.logicalTick()
        entityController.logicalTick()
        effectLayersController.logicalTick()
        playerControl.logicalTick()
        gameRender.logicalTick()
        logicalUpdate()


        mainCamera.updateCamera()
    }

    override fun physicTick() {
        mapController.physicTick()
        entityController.physicTick()
        effectLayersController.physicTick()
        playerControl.physicTick()
        gameRender.physicTick()

        physicUpdate()
    }
    fun renderTick(lg: LGraphics) {
        renderUpdate()
        lg.noStroke()
        gameRender.render(lg, mainCamera)
    }

    fun physicUpdate() {
        gameCycle.physicUpdate()
    }
    fun logicalUpdate() {
        gameCycle.logicalUpdate()
    }
    fun renderUpdate() {
        gameCycle.renderUpdate()
    }

    override fun start() {
        gameLoader.load()
    }
}