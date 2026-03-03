package la.vok.Game.GameContent.Items.Other

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Special.ItemEntity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.Weapons.TileHandItem
import la.vok.Game.Windows.InventoryCell
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Gradient.ShadowInfo
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

    open fun stackVisualCount(): Int = when {
        count >= 5000 -> 7
        count >= 500 -> 6
        count >= 50 -> 5
        count >= 10  -> 4
        count >= 5   -> 3
        count >= 2   -> 2
        else         -> 1
    }

    open fun stackOffset(index: Int, size: Vec2): Vec2 {
        val step = size * 0.1f
        return (step * (index.toFloat())).invertedY()
    }

    open fun worldRender(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        val renderSize = size * itemType.worldSize
        val visual = stackVisualCount()

        for (i in 0 until visual) {
            val offset = stackOffset(visual-i-1, renderSize)
            var renderPos = pos + itemType.worldRenderDelta * size - offset
            shadowRender(lg, renderPos, renderSize, itemType.shadowPower/(i/2f+0.5f))
            baseRender(lg, renderPos, renderSize)
        }
    }

    open fun shadowRender(lg: LGraphics, pos: Vec2, size: Vec2, power: Float = itemType.shadowPower) {
        lg.setTint(255f, 255f*power)
        lg.setImage(
            ShadowInfo(
                coreController.spriteLoader.getValue(itemType.sprite),
                120 p 120,
                10,
                6,
                true
            ).generate(),
                pos, size * 1.1f)
        lg.noTint()
    }

    open fun cellRender(lg: LGraphics, pos: Vec2, size: Vec2, cell: InventoryCell) {
        val renderPos = pos + itemType.slotRenderDelta * (size * itemType.sizeInSlot)
        val renderSize = size * itemType.sizeInSlot
        shadowRender(lg, renderPos, renderSize, itemType.shadowPower * 0.5f)
        baseRender(lg, renderPos, renderSize)
        renderCount(lg, pos, size)
    }

    open fun cellDragRender(lg: LGraphics, pos: Vec2, size: Vec2, cell: InventoryCell) {
        val renderPos = pos + itemType.slotRenderDelta * (size * itemType.sizeInSlot)
        val renderSize = size * itemType.sizeInSlot * 1.1f
        shadowRender(lg, renderPos, renderSize)
        baseRender(lg, renderPos, renderSize)
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
        if (itemType.maxInStack <= 1) return false
        return (itemType === item.itemType)
    }
    open fun leftToStack() : Int {
        return itemType.maxInStack - count
    }
}