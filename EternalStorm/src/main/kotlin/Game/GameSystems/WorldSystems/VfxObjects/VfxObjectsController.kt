package la.vok.Game.GameSystems.WorldSystems.VfxObjects

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameController.GameCycle

class VfxObjectsController(var gameCycle: GameCycle) : Controller {
    var vfxObjectsApi: VfxObjectsApi = VfxObjectsApi(this)
    var vfxObjectsSystem = VfxObjectsSystem(this)

    init {
        create()
    }

    override fun logicalTick() {
        vfxObjectsSystem.vfxObjects.forEach {
            it.logicalUpdate()
        }
        vfxObjectsSystem.flushBuffers()
    }

    override fun renderTick() {
        vfxObjectsSystem.vfxObjects.forEach {
            it.renderUpdate()
        }
    }

    override fun physicTick() {
        vfxObjectsSystem.vfxObjects.forEach {
            it.physicUpdate()
        }
    }
}