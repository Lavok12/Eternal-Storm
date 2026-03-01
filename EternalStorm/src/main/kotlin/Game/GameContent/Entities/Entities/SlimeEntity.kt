package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.Ai.AbstractAI
import la.vok.Game.GameContent.Entities.Ai.SlimeAI
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Entities.EntityRender.SlimeRenderEntity
import la.vok.Game.GameContent.HandItems.Items.AxeHandItem
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.dropTable
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.EntityComponents.PlayerControlComponent
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
class SlimeEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = SlimeRenderEntity(getRenderLayer())
    override val ai: AbstractAI? = SlimeAI(this, gameCycle)

    override fun physicUpdate() {
        super.physicUpdate()
    }
}
