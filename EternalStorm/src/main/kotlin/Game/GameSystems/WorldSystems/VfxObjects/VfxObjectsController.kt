package la.vok.Game.GameSystems.WorldSystems.VfxObjects

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension


class VfxObjectsController(var dimension: AbstractDimension) : Controller {
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