package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Game.GameContent.Entities.Entities.SpiderLegEntity
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class SpiderLegEntityType() : AbstractEntityType() {
    override val tag: String = EntitiesList.spider_leg
    override val baseHp: Int = 150
    override val baseSize: Vec2 = 0.5f v 0.5f
    override val tags = arrayOf(EntityTags.entity, EntityTags.enemy)
    override val imgPreview = ""

    override fun createEntity(gameCycle: GameCycle) : Entity {
        return SpiderLegEntity(this, gameCycle)
    }
}
