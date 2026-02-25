package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.StandartCamera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.Game.ClientContent.Windows.WGamePanel
import Core.CoreControllers.ObjectRegistration
import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameController.GameLoader
import la.vok.Game.GameSystems.Entities.EntityController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class GameController(var coreController: CoreController) : SceneController {
    var effectLayersController = EffectLayersController(this)
    var gameLoader = GameLoader(this)
    val gameRender = GameRender(this)
    var mapController = MapController(this)
    var entityController = EntityController(this)

    init {
        create()
    }

    var wGamePanel: WGamePanel? = null

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
    override fun tick() {
        super.superTick()
        mapController.tick()
        entityController.tick()
        effectLayersController.tick()
        gameRender.tick()
    }

    fun renderTick(lg: LGraphics, camera: Camera) {
        gameRender.render(lg, camera)
    }

    override fun start() {
        gameLoader.load()
    }
}