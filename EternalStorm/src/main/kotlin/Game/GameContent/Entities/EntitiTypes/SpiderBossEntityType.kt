package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Game.GameContent.Entities.Entities.SpiderBossEntity
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class SpiderBossEntityType() : AbstractEntityType() {
    override val tag: String = EntitiesList.spider_boss
    override val baseHp: Int = 2500
    override val baseSize: Vec2 = 5 v 5
    override val tags = arrayOf(EntityTags.entity, EntityTags.enemy, EntityTags.boss)
    override val imgPreview = ""

    override fun createEntity(gameCycle: GameCycle) : Entity {
        return SpiderBossEntity(this, gameCycle)
    }
}
