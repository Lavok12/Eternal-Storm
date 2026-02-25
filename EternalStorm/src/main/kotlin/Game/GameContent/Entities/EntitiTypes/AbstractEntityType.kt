package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Tiles.Tiles.Tile

abstract class AbstractEntityType {
    open val tag: String = ""
    open val hp: Int = 0

    open fun createEntity(gameController: GameController) : Entity {
        return Entity(this, gameController)
    }
}