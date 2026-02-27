package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameContent.Entities.Entities.SlimeEntity
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class SlimeEntityType() : AbstractEntityType() {
    override val tag: String = AppState.addNamespace("Slime")
    override val baseHp: Int = 100
    override val baseSize: Vec2 = 1.6 v 1.6
    override var tags = arrayOf(EntityTags.entity, EntityTags.enemy)


    override fun createEntity(gameCycle: GameCycle) : Entity {
        return SlimeEntity(this, gameCycle)
    }
}