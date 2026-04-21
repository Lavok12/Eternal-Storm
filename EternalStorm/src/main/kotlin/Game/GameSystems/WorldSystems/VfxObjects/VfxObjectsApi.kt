package la.vok.Game.GameSystems.WorldSystems.VfxObjects

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.VfxObjects.AbstractVfxObject
import la.vok.Game.GameContent.VfxObjects.DamageVfxObject
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class VfxObjectsApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController

    fun showVfx(dimension: AbstractDimension, vfx: AbstractVfxObject) {
        vfx.show()
    }

    fun hideVfx(dimension: AbstractDimension, vfx: AbstractVfxObject) {
        vfx.hide()
    }

    fun addInSystem(dimension: AbstractDimension, vfx: AbstractVfxObject) {
        dimension.vfxObjectsSystem.add(vfx.apply {
            this.dimension = dimension
            this.vfxObjectsSystem = dimension.vfxObjectsSystem
            create() })

    }

    fun addInSystem(dimension: AbstractDimension, vfx: AbstractVfxObject, pos: Vec2, size: Vec2 = 1 v 1, speed: Vec2 = Vec2.ZERO) {
        vfx.position = pos.copy()
        vfx.size = size.copy()
        vfx.dimension = dimension
        vfx.vfxObjectsSystem = dimension.vfxObjectsSystem
        vfx.speed = speed
        vfx.create()
        dimension.vfxObjectsSystem.add(vfx)
    }

    fun spawnDamageValue(dimension: AbstractDimension, pos: Vec2, damage: Int) {
        val vfxd = DamageVfxObject(gameCycle, damage)
        vfxd.vfxObjectsSystem = dimension.vfxObjectsSystem
        addInSystem(dimension, vfxd, pos, 1 v 1, 0 v 0.02f)
    }

    fun spawn(dimension: AbstractDimension, pos: Vec2, size: Vec2 = 1 v 1, vfxFactory: () -> AbstractVfxObject): AbstractVfxObject {
        val vfx = vfxFactory()
        addInSystem(dimension, vfx, pos, size)
        return vfx
    }

    fun deleteInSystem(dimension: AbstractDimension, vfx: AbstractVfxObject) {
        dimension.vfxObjectsSystem.delete(vfx)
    }

    fun killInSystem(dimension: AbstractDimension, vfx: AbstractVfxObject) {
        dimension.vfxObjectsSystem.kill(vfx)
    }

    fun getActiveVfx(dimension: AbstractDimension): HashSet<AbstractVfxObject> {
        return dimension.vfxObjectsSystem.vfxObjects
    }
}