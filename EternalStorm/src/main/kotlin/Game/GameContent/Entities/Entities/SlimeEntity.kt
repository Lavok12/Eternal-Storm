package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.Ai.AbstractAI
import la.vok.Game.GameContent.Entities.Ai.SlimeAI
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.SlimeRenderEntity
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter

@Suppress("UNCHECKED_CAST")
class SlimeEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = SlimeRenderEntity(getRenderLayer())
    override var ai: AbstractAI? = SlimeAI(this, gameCycle)
    override var bodyDamage = 10
    override var bodyKnockBack = 0.3f

    init {
        hasCollisionDetector = true
    }
    override fun physicUpdate() {
        super.physicUpdate()
    }

    override fun createCustomHitboxes() {
        super.createCustomHitboxes()
        collisionDetector?.tagFilter = TagFilter.HasAny(listOf(EntityTags.player))
        collisionDetector?.onContactStart = { it ->
            bodyDamage(it)
        }
    }
}
