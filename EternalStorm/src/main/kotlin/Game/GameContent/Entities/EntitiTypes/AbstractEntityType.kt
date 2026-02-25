package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

abstract class AbstractEntityType {
    open val tag: String = ""
    open val baseHp: Int = 0
    open val baseSize: Vec2 = 1 v 1
    open var tags = arrayOf(EntityTags.entity)

    open fun createEntity(gameController: GameController) : Entity {
        return Entity(this, gameController)
    }
}