package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Game.GameContent.Entities.Entities.BossEntity
import la.vok.Game.GameContent.Entities.Entities.SlimeEntity
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.EntitiesList
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.dropTable
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class BossEntityType() : AbstractEntityType() {
    override val tag: String = EntitiesList.boss
    override val baseHp: Int = 600
    override val baseSize: Vec2 = 3 v 3
    override val tags = arrayOf(EntityTags.entity, EntityTags.enemy, EntityTags.boss)
    override val imgPreview = "xHead.png"

    override fun createEntity(gameCycle: GameCycle) : Entity {
        return BossEntity(this, gameCycle)
    }
}