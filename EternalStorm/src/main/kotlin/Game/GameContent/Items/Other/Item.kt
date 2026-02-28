package la.vok.Game.GameContent.Items.Other

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.Entities.ItemEntity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.Weapons.TileHandItem
import la.vok.Game.GameContent.Windows.InventoryCell
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class Item(var itemType: AbstractItemType, var gameCycle: GameCycle) {

    val gameController: GameController get() = gameCycle.gameController
    val coreController: CoreController get() = gameController.coreController

    var isChoose = false
    var count = 1;

    open fun baseRender(lg: LGraphics, pos: Vec2, size: Vec2) {
        if (itemType.sprite != "") {
            when (itemType.usingVariants) {
                is UsingVariants.PlaceTile -> {
                    lg.setImage(coreController.spriteLoader.getValue(itemType.sprite), pos, size*0.8f)
                }
                else -> {
                    lg.setImage(coreController.spriteLoader.getValue(itemType.sprite), pos, size)
                }
            }
        }
    }
    open fun renderCount(lg: LGraphics, pos: Vec2, size: Vec2) {
        if (count > 1) {
            lg.fill(255f)
            lg.setTextAlign(0 p 1)
            lg.setText("$count", pos.x, pos.y - size.y * 0.55f, 14f)
        }
    }

    open fun worldRender(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        baseRender(lg, pos + itemType.worldRenderDelta * (size * itemType.worldSize), size * itemType.worldSize)
        renderCount(lg, pos, size)
    }

    open fun cellRender(lg: LGraphics, pos: Vec2, size: Vec2, cell: InventoryCell) {
        baseRender(lg, pos + itemType.slotRenderDelta * (size * itemType.sizeInSlot), size * itemType.sizeInSlot)
        renderCount(lg, pos, size)
    }

    open fun cellPhysicUpdate(itemSlot: ItemSlot) {

    }
    open fun cellRenderUpdate(itemSlot: ItemSlot) {

    }
    open fun cellLogicalUpdate(itemSlot: ItemSlot) {

    }

    open fun logicalUpdate(entity: ItemEntity) {

    }
    open fun physicUpdate(entity: ItemEntity) {

    }
    open fun renderUpdate(entity: ItemEntity) {

    }
    open fun getHandItem(component: HandItemComponent) : HandItem? {
        if (itemType.usingVariants is UsingVariants.PlaceTile) {
            return TileHandItem(this, component, gameCycle.mapApi.getRegisteredTileType((itemType.usingVariants as UsingVariants.PlaceTile).tileTag))
        }
        return null
    }
    open fun spawn() {

    }
    open fun startChoose() {
        isChoose = true
    }
    open fun endChoose() {
        isChoose = false
    }

    fun rawCopy() : Item {
        return gameCycle.itemsApi.getRegisteredItem(itemType)
    }
    open fun copy() : Item {
        return rawCopy()
    }
    open fun canStackable(item: Item) : Boolean {
        if (itemType.maxInStack == 0) return false
        return (itemType === item.itemType)
    }
    open fun leftToStack() : Int {
        return itemType.maxInStack - count
    }
}