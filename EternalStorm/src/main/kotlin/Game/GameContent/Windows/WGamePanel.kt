package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.GameControllers.GameController

class WGamePanel(windowsManager: WindowsManager, var gameController: GameController) : WStandartPanel(windowsManager) {
    override var tags: Array<String>
        get() = arrayOf("game")
        set(value) {}
    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        gameController.renderTick(lg)
        lg.bg(0f)
    }
}