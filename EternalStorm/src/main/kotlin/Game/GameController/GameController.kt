package la.vok.Core.GameControllers

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState

class GameController(var coreController: CoreController) : SceneController {
    var effectLayersController = EffectLayersController(this)
    val gameRender = GameRender(this)

    init {
        create()
    }

    fun initWindows() {
        coreController.windowsManager.destroy()

        val wGamePanel = WGamePanel(coreController.windowsManager, this).apply {
            windowSize = AppState.lg.frameSize
        }

        coreController.windowsManager.addWindow(wGamePanel)
    }
    override fun tick() {
        super.superTick()
        effectLayersController.tick()
        gameRender.tick()
    }
    fun renderTick(lg: LGraphics) {}

    override fun activate() {
        initWindows()
    }

    override fun deactivate() {
        coreController.windowsManager.destroy()
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}