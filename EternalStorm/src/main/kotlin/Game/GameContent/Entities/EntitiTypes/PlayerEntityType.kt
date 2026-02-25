package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameContent.EntityTags
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class PlayerEntityType() : AbstractEntityType() {
    override val tag: String = AppState.addNamespace("Player")
    override val baseHp: Int = 0
    override val baseSize: Vec2 = 1.6 v 2.8
    override var tags = arrayOf(EntityTags.entity, EntityTags.player)


    override fun createEntity(gameController: GameController) : Entity {
        return PlayerEntity(this, gameController)
    }
}