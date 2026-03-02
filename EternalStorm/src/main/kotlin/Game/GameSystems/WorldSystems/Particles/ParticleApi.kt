package la.vok.Game.GameSystems.WorldSystems.Particles

import Core.CoreControllers.ObjectRegistration
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameController.GameCycle

class ParticleApi(var particleController: ParticleController) {
    val gameCycle: GameCycle = particleController.gameCycle
    val gameController: GameController get() = gameCycle.gameController

}