package la.vok.Core.CoreControllers.Interfaces

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.State.AppState

interface SceneController : Controller {
    fun activate(coreController: CoreController) {
        initWindows()
    }

    fun deactivate(coreController: CoreController) {
        coreController.windowsManager.destroy()
    }
    fun initWindows()

    fun start()
}