package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameContent.EntitiesList
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class PlayerEntityType() : AbstractEntityType() {
    override val tag: String = EntitiesList.player
    override val baseHp: Int = 120
    override val baseSize: Vec2 = 1.6 v 2.8
    override val tags = arrayOf(EntityTags.entity, EntityTags.player)


    override fun createEntity(gameCycle: GameCycle) : Entity {
        return PlayerEntity(this, gameCycle)
    }
}