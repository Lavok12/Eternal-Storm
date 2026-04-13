package la.vok.Game.GameSystems.WorldSystems.VfxObjects

import la.vok.Game.GameContent.VfxObjects.AbstractVfxObject
import la.vok.State.AppState

class VfxObjectsSystem(var vfxObjectsController: VfxObjectsController) {
    var vfxObjects = HashSet<AbstractVfxObject>()

    private val addBuffer = ArrayDeque<AbstractVfxObject>()
    private val deleteBuffer = ArrayDeque<AbstractVfxObject>()
    private val killBuffer = ArrayDeque<AbstractVfxObject>()

    fun add(vfx: AbstractVfxObject) {
        addBuffer.add(vfx)
    }

    fun delete(vfx: AbstractVfxObject) {
        deleteBuffer.add(vfx)
    }

    fun kill(vfx: AbstractVfxObject) {
        killBuffer.add(vfx)
    }

    fun flushBuffers() {
        while (addBuffer.isNotEmpty()) {
            val vfx = addBuffer.removeFirst()
            vfxObjects.add(vfx)
            vfxObjectsController.dimension.gameCycle.vfxObjectsApi.showVfx(vfxObjectsController.dimension, vfx)
        }

        while (deleteBuffer.isNotEmpty()) {
            val vfx = deleteBuffer.removeFirst()
            vfxObjectsController.dimension.gameCycle.vfxObjectsApi.hideVfx(vfxObjectsController.dimension, vfx)
            vfxObjects.remove(vfx)
        }

        while (killBuffer.isNotEmpty()) {
            val vfx = killBuffer.removeFirst()
            vfxObjectsController.dimension.gameCycle.vfxObjectsApi.hideVfx(vfxObjectsController.dimension, vfx)
            vfxObjects.remove(vfx)
            vfx.kill()
        }
    }
}