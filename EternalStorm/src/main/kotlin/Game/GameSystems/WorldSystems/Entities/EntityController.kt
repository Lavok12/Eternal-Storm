package la.vok.Game.GameSystems.WorldSystems.Entities

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class EntityController(var dimension: AbstractDimension) : Controller {
    init {
        create()
    }
    var entitySystem = EntitySystem(this)


    override fun logicalTick() {
        dimension.gameCycle.collisionSystem.rebuildGrid(dimension)
        entitySystem.entities.forEach { it.logicalUpdate() }
        entitySystem.flushBuffers()
    }

    override fun physicTick() {
        entitySystem.entities.toList().forEach {
            if (!it.isDead) it.physicUpdate()
        }
    }

    override fun renderTick() {
        entitySystem.entities.toList().forEach {
            if (!it.isDead) it.renderUpdate()
        }
    }
}