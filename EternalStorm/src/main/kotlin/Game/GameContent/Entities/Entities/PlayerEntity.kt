package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.HandItems.Items.AxeHandItem
import la.vok.Game.GameContent.HandItems.Items.SpearHandItem
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.EntityComponents.PlayerControlComponent
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
class PlayerEntity(entityType: AbstractEntityType, gameController: GameController) : Entity(entityType, gameController) {
    override var renderEntity: RenderObjectInterface<RenderLayers.Main> = BaseRenderEntity(getEntityRenderContainer())
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
    override fun createCustomHitboxes() {
        val new = addHitbox("down trigger", HitboxTypes.ONLY_TRIGGER)
        new.size = 1.4 v 0.1f
        new.delta.y = -1.5f
    }
}