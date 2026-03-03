package la.vok.Game.GameSystems.WorldSystems.Particles

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameController.GameCycle

class ParticleController(var gameCycle: GameCycle) : Controller {
    var particleSystem = ParticleSystem(this)
    var particlesApi = ParticlesApi(this)

    init {
        create()
    }

    override fun physicTick() {
        particleSystem.physicUpdate()
    }

    fun render() {

    }
}