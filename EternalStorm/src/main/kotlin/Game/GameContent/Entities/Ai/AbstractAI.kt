package la.vok.Game.GameContent.Entities.Ai

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData

abstract class AbstractAI(var entity: Entity, var gameCycle: GameCycle) {
    open fun spawn() {}
    open fun physicUpdate() {}
    open fun die() {}
    open fun damage(damage: DamageData) {}
}