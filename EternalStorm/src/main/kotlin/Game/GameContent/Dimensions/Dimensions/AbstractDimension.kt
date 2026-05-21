package la.vok.Game.GameContent.Dimensions.Dimensions

import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityController
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameSystems.WorldSystems.Particles.ParticleController
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsController
import la.vok.Game.GameSystems.WorldSystems.Liquid.LiquidController
import la.vok.LavokLibrary.Vectors.LColor

abstract class AbstractDimension(var gameCycle: GameCycle) {
    abstract val dimensionTag: String
    abstract val width: Int
    abstract val height: Int

    abstract var skyColor: LColor

    var mapController: MapController? = null
    var entityController: EntityController? = null
    var particleController: ParticleController? = null
    var vfxObjectsController: VfxObjectsController? = null
    var liquidController: LiquidController? = null

    val mapSystem get() = mapController!!.mapSystem
    val entitySystem get() = entityController!!.entitySystem
    val particleSystem get() = particleController!!.particleSystem
    val vfxObjectsSystem get() = vfxObjectsController!!.vfxObjectsSystem

    abstract fun generateMap()

    open fun initialize() {
        mapController = MapController(this)
        entityController = EntityController(this)
        particleController = ParticleController(this)
        vfxObjectsController = VfxObjectsController(this)
        liquidController = LiquidController(this)
    }

    open fun physicTick() {
        mapController?.physicTick()
        entityController?.physicTick()
        particleController?.physicTick()
        vfxObjectsController?.physicTick()
        liquidController?.physicTick()
    }

    open fun logicalTick() {
        mapController?.logicalTick()
        entityController?.logicalTick()
        vfxObjectsController?.logicalTick()
    }

    open fun renderTick() {
        mapController?.renderTick()
        entityController?.renderTick()
        vfxObjectsController?.renderTick()
    }
}