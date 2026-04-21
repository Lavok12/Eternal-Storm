package la.vok.Game.GameContent.Dimensions.Dimensions

import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityController
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameSystems.WorldSystems.Particles.ParticleController
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsController
import la.vok.LavokLibrary.Vectors.LColor

abstract class AbstractDimension(var gameCycle: GameCycle) {
    abstract val dimensionTag: String
    abstract val width: Int
    abstract val height: Int

    abstract var skyColor: LColor

    val mapController by lazy { MapController(this) }
    val mapSystem get() = mapController.mapSystem

    val entityController by lazy { EntityController(this) }
    val entitySystem get() = entityController.entitySystem

    val particleController by lazy { ParticleController(this) }
    val particleSystem get() = particleController.particleSystem

    val vfxObjectsController by lazy { VfxObjectsController(this) }
    val vfxObjectsSystem get() = vfxObjectsController.vfxObjectsSystem

    abstract fun generateMap()

    open fun physicTick() {
        mapController.physicTick()
        entityController.physicTick()
        particleController.physicTick()
        vfxObjectsController.physicTick()
    }

    open fun logicalTick() {
        mapController.logicalTick()
        entityController.logicalTick()
        vfxObjectsController.logicalTick()
    }

    open fun renderTick() {
        mapController.renderTick()
        entityController.renderTick()
        vfxObjectsController.renderTick()
    }
}