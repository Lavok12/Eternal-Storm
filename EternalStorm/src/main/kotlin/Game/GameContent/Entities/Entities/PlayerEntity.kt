package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.Ai.AbstractAI
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.MobInventory
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.EntityComponents.PickUpComponent
import la.vok.Game.GameContent.Entities.Ai.PlayerAI
import la.vok.Game.GameSystems.EntityComponents.EquipmentModule
import la.vok.Game.GameSystems.EntityComponents.LiquidDetectorComponent
import la.vok.Game.GameSystems.EntityComponents.OxygenComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
class PlayerEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = BaseRenderEntity(getRenderLayer())
    override var ai: AbstractAI? = PlayerAI(this, gameCycle)

    var handItemComponent = HandItemComponent(this, 0.8 v 0f)
    var pickUpComponent: PickUpComponent? = null
    var equipmentModule = EquipmentModule(this)

    override var inventory: MobInventory? = MobInventory(this, 50)

    init {
        addComponent(LiquidDetectorComponent(this))
        addComponent(OxygenComponent(this))
    }

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
        gameCycle.playerApi.registerPlayer(systemId, this)
        
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.axe))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.sin_gun))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.pickaxe))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.grass_block, 10))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.stone_block, 1230))
        inventory?.itemContainer?.addItem(gameCycle.itemsApi.getRegisteredItem(ItemsList.dirt_block, 22334))

        handItemComponent.setItem(inventory?.itemContainer?.getItem(0))
    }

    override fun physicUpdate() {
        pickUpComponent?.physicUpdate()
        handItemComponent.physicUpdate()
        equipmentModule.physicUpdate()
        super.physicUpdate()
    }

    override fun logicalUpdate() {
        equipmentModule.logicalUpdate()
        super.logicalUpdate()
    }

    override fun renderUpdate() {
        handItemComponent.renderUpdate()
        equipmentModule.renderUpdate()
        super.renderUpdate()
    }

    override fun show() {
        super.show()
        handItemComponent.currentHandItem?.show()
    }
    override fun hide() {
        super.hide()
        handItemComponent.currentHandItem?.hide()
    }

    override fun damage(damage: DamageData) {
        super.damage(damage)
        invulnerabilityTicks = 15
    }
}