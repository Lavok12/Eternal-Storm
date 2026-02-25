package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.Entities.TestEntity
import la.vok.State.AppState

class TestEntityType() : AbstractEntityType() {
    override val tag: String = AppState.addNamespace("Test")
    override val hp: Int = 0

    override fun createEntity(gameController: GameController) : Entity {
        return TestEntity(this, gameController)
    }
}