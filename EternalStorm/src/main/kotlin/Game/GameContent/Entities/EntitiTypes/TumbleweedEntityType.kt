package la.vok.Game.GameContent.Entities.EntitiTypes

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.Entities.TumbleweedEntity
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.dropTable
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class TumbleweedEntityType : AbstractEntityType() {
    override val tag: String = EntitiesList.tumbleweed
    override val baseHp: Int = 1
    override val baseSize: Vec2 = 2.0 v 2.0
    override val tags = arrayOf(EntityTags.entity, EntityTags.enemy)
    override val imgPreview = "tumbleweed.png"
    override var drop: DropEntry = dropTable {
        item(ItemsList.sand_block, 1, 3)
        item(ItemsList.sandstone_block, 1, 3)
    }

    override fun createEntity(gameCycle: GameCycle): Entity {
        return TumbleweedEntity(this, gameCycle)
    }
}
