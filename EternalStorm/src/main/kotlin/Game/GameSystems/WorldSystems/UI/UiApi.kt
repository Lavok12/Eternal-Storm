package la.vok.Game.GameSystems.WorldSystems.UI

import la.vok.Core.CoreContent.Windows.Modules.IUiModule
import la.vok.Core.GameControllers.GameController

class UiApi(val gameController: GameController) {
    fun addModule(module: IUiModule) {
        gameController.wGamePanel?.moduleManager?.addModule(module)
    }

    fun removeModule(id: String) {
        gameController.wGamePanel?.moduleManager?.removeModule(id)
    }

    fun <T : IUiModule> getModule(id: String): T? {
        return gameController.wGamePanel?.moduleManager?.getModule(id)
    }
}
