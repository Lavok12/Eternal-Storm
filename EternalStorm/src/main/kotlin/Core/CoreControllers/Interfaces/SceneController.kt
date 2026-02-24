package la.vok.Core.CoreControllers.Interfaces

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.State.AppState

interface SceneController : Controller {
    fun activate()
    fun deactivate()

    fun start()
}