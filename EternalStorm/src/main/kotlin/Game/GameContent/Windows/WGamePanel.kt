package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Camera.StandartCamera
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.Vectors.v

class WGamePanel(windowsManager: WindowsManager, var gameController: GameController) : WStandartPanel(windowsManager) {
    var mainCamera = StandartCamera(50 v 102, 25f)

    override var tags: Array<String>
        get() = arrayOf("game")
        set(value) {}
    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        lg.bg(0f)
        gameController.renderTick(lg, mainCamera)
    }

    override fun physicUpdate() {
        super.physicUpdate()
    }
}