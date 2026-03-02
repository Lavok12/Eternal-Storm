package la.vok.Game.GameSystems.WorldSystems.Particles

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.LGraphics.LGraphics

class ParticleSystem(var particleController: ParticleController) {
    var list = mutableListOf<Particle>()

    fun addParticle(particle: Particle) {
        list += particle
    }
    fun physicUpdate() {
        for (l in list) {
            l.physicUpdate()
        }
        list.removeAll { it.isDelete }
    }
    fun render(lg: LGraphics, camera: Camera) {
        for (l in list) {
            l.render(lg, camera)
        }
    }
}