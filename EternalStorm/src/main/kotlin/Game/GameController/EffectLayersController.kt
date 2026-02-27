package la.vok.Core.GameControllers

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.Layers.EffectLayer

class EffectLayersController(val gameController: GameController) : Controller {
    val coreController: CoreController
        get() {return gameController.coreController}

    init {
        create()
    }

    val bgUpdateImages = arrayOf<EffectLayer>()

    override fun logicalTick() {
        for (i in bgUpdateImages) {
            i.tick()
        }
    }
}