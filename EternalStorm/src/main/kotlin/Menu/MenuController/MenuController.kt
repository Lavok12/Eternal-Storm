package la.vok.Menu.MenuController

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.Menu.MenuContent.BGPlanetBase
import la.vok.Menu.MenuContent.BGPlanetFinal
import la.vok.Menu.MenuContent.Windows.WMenuPanel
import la.vok.State.AppState


class MenuController(var coreController: CoreController) : SceneController {
    init {
        create()
    }

    val planetBase = BGPlanetBase(LPoint(2000, 1000), 1f)
    val planetFinal = BGPlanetFinal(LPoint(2000, 2000), 1f, planetBase)


    override fun initWindows() {
        coreController.windowsManager.destroy()

        val wMenuPanel = WMenuPanel(coreController.windowsManager, this).apply {
            windowSize = AppState.lg.frameSize
        }

        coreController.windowsManager.addWindow(wMenuPanel)
    }
    var frame = -1
    override fun logicalTick() {
        frame++
        if (frame % 6 == 0) {
            planetBase.draw = true
            planetFinal.draw = true
        }
        planetBase.tick()
        planetFinal.tick()
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}