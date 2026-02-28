package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.HandItems.Items.AxeHandItem
import la.vok.Game.GameContent.HandItems.Items.SpearHandItem
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.MobInventory
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.EntityComponents.PickUpComponent
import la.vok.Game.GameSystems.EntityComponents.PlayerControlComponent
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
class PlayerEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = BaseRenderEntity(getRenderLayer())
    var playerControlComponent = PlayerControlComponent(this)

    var handItemComponent = HandItemComponent(this, 0.8 v 0f)
    var pickUpComponent: PickUpComponent? = null

    override var inventory: MobInventory? = MobInventory(this, 10)

    override fun changeFacing(newFacing: Int) {
        if (handItemComponent.isFacingBlocked()) return
        facing = newFacing
    }

    override fun createCustomHitboxes() {
        val attractTrigger = addHitbox("attract trigger", HitboxTypes.ONLY_TRIGGER, null).also {
            it.size = 7 v 7
            it.ignoreCollision = true
        }
        val pickUpTrigger = addHitbox("pickup trigger", HitboxTypes.ONLY_TRIGGER, null).also {
            it.size = size.copy()
            it.ignoreCollision = true
        }
        pickUpComponent = PickUpComponent(this, pickUpTrigger, attractTrigger, inventory!!.itemContainer, gravityPower = 2f)
    }

    override fun spawn() {
        super.spawn()
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.axe))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.spear))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.pickaxe))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.grass_block, 10))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.stone_block, 1230))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.dirt_block, 22334))

        handItemComponent.setItem(inventory?.itemContainer?.getItem(0))
    }

    override fun physicUpdate() {
        pickUpComponent?.physicUpdate()
        handItemComponent.physicUpdate()
        super.physicUpdate()
    }

    override fun renderUpdate() {
        handItemComponent.renderUpdate()
        super.renderUpdate()
    }
}