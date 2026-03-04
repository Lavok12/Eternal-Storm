package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Game.GameContent.Entities.Entities.Special.ProjectileEntity
import la.vok.Game.GameContent.Entities.Entities.Special.EmptyEntity
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.Entities.Special.ItemEntity
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

abstract class AbstractEntityType {
    object EmptyEntityType : AbstractEntityType() {
        override val baseHp = 0
        override val baseSize = Vec2.ZERO
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

    object ProjectileEntityType : AbstractEntityType() {
        override val tag: String = EntitiesList.projectile
        override val baseHp: Int = 1
        override val baseSize: Vec2 = 1f v 1f
        override val tags = arrayOf(EntityTags.projectile)

        override fun createEntity(gameCycle: GameCycle) : Entity {
            return ProjectileEntity(gameCycle, 0, -1L)
        }
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