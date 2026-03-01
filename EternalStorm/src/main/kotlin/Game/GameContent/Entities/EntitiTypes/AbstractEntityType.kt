package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.EmptyEntity
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.Entities.ItemEntity
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

abstract class AbstractEntityType {
    object EmptyEntityType : AbstractEntityType() {
        override val baseHp = 0
        override val baseSize = 0 v 0
        override val tags = arrayOf(EntityTags.entity)
        override fun createEntity(gameCycle: GameCycle) = EmptyEntity(gameCycle)
        override val imgPreview = ""
    }

    object ItemEntityType : AbstractEntityType() {
        override val baseHp = 0
        override val baseSize = 1 v 1
        override val tags = arrayOf(EntityTags.item)
        override fun createEntity(gameCycle: GameCycle) = ItemEntity(gameCycle)
        override val imgPreview = ""
    }

    open val tag: String = ""
    open val baseHp: Int = 0
    open val baseSize: Vec2 = 1 v 1
    open val tags = arrayOf(EntityTags.entity)
    open val imgPreview = ""
    open val drop: DropEntry = NothingDrop

    open fun createEntity(gameCycle: GameCycle) : Entity {
        return Entity(this, gameCycle)
    }
}