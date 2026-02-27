package la.vok.Game.GameSystems.WorldSystems.VfxObjects

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.VfxObjects.AbstractVfxObject
import la.vok.Game.GameContent.VfxObjects.DamageVfxObject
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class VfxObjectsApi(var vfxObjectsController: VfxObjectsController) {
    val gameCycle: GameCycle = vfxObjectsController.gameCycle
    val gameController: GameController get() = gameCycle.gameController

    fun showVfx(vfx: AbstractVfxObject) {
        vfx.show()
    }

    fun hideVfx(vfx: AbstractVfxObject) {
        vfx.hide()
    }

    fun addInSystem(vfx: AbstractVfxObject) {
        vfxObjectsController.vfxObjectsSystem.add(vfx.apply {
            vfxObjectsSystem = vfxObjectsController.vfxObjectsSystem
            create() })

    }

    fun addInSystem(vfx: AbstractVfxObject, pos: Vec2, size: Vec2 = 1 v 1, speed: Vec2 = 0 v 0) {
        vfx.position = pos.copy()
        vfx.size = size.copy()
        vfx.vfxObjectsSystem = vfxObjectsController.vfxObjectsSystem
        vfx.speed = speed
        vfx.create()
        vfxObjectsController.vfxObjectsSystem.add(vfx)
    }

    fun spawnDamageValue(pos: Vec2, damage: Int) {
        val vfxd = DamageVfxObject(gameCycle, damage)
        vfxd.vfxObjectsSystem = vfxObjectsController.vfxObjectsSystem
        addInSystem(vfxd, pos, 1 v 1, 0 v 0.02f)
    }

    fun spawn(pos: Vec2, size: Vec2 = 1 v 1, vfxFactory: () -> AbstractVfxObject): AbstractVfxObject {
        val vfx = vfxFactory()
        addInSystem(vfx, pos, size)
        return vfx
    }

    fun deleteInSystem(vfx: AbstractVfxObject) {
        vfxObjectsController.vfxObjectsSystem.delete(vfx)
    }

    fun killInSystem(vfx: AbstractVfxObject) {
        vfxObjectsController.vfxObjectsSystem.kill(vfx)
    }

    fun getActiveVfx(): HashSet<AbstractVfxObject> {
        return vfxObjectsController.vfxObjectsSystem.vfxObjects
    }
}