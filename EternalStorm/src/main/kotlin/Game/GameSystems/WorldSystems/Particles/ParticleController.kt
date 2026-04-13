package la.vok.Game.GameSystems.WorldSystems.Particles

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class ParticleController(var dimension: AbstractDimension) : Controller {
    var particleSystem = ParticleSystem(this)

    init {
        create()
    }

    override fun physicTick() {
        particleSystem.physicUpdate()
    }

    fun render() {

    }
}