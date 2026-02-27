package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.HandItems.Items.AxeHandItem
import la.vok.Game.GameContent.HandItems.Items.SpearHandItem
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.EntityComponents.PlayerControlComponent
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
class PlayerEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = BaseRenderEntity(getRenderLayer())
    var playerControlComponent = PlayerControlComponent(this)

    var handItemComponent = HandItemComponent(this, 0.8 v 0f)

    override fun spawn() {
        super.spawn()
        handItemComponent.setHandItem(AxeHandItem(handItemComponent))
    }
    override fun physicUpdate() {
        handItemComponent.physicUpdate()
        super.physicUpdate()
    }

    override fun renderUpdate() {
        handItemComponent.renderUpdate()
        super.renderUpdate()
    }
}