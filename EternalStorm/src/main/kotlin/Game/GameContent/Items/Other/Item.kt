package la.vok.Game.GameContent.Items.Other

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Special.ItemEntity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.List.WallHandItem
import la.vok.Game.GameContent.HandItems.Weapons.TileHandItem
import la.vok.Game.Windows.InventoryCell
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Core.CoreControllers.Parts.Tooltip
import la.vok.Core.CoreControllers.Parts.TooltipLine
import la.vok.LavokLibrary.Gradient.GradientInfo
import la.vok.LavokLibrary.Gradient.ShadowFrameInfo
import la.vok.LavokLibrary.Gradient.ShadowType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

@Suppress("UNCHECKED_CAST")
open class Item(var itemType: AbstractItemType, var gameCycle: GameCycle) {
    fun generateTooltip(): Tooltip {
        val lines = getTooltipLines()
        return Tooltip(text = "").apply {
            extraRender = { lg, pos, _ ->
                // Рассчитываем ширину динамически по самой длинной строке
                val contentWidth = lines.maxOf { it.getWidth(lg) }.coerceAtLeast(180f)
                val width = contentWidth + 30f // Отступы по краям
                val totalHeight = lines.sumOf { it.render(lg, Vec2(-1000f, -1000f), width - 20f).toDouble() }.toFloat() + 20f
                
                val screen = Vec2(lg.disW.toFloat(), lg.disH.toFloat())
                
                // Выравнивание по Левому Верхнему углу (Top-Left) относительно pos.
                // В LGraphics +Y это ВВЕРХ.
                val center = pos + (width / 2f v -totalHeight / 2f)
                
                // Проверка границ
                if (center.x + width / 2f > screen.x / 2f) center.x -= (width + 40f)
                if (center.y - totalHeight / 2f < -screen.y / 2f) center.y += (totalHeight + 10f)
                if (center.y + totalHeight / 2f > screen.y / 2f) center.y -= (totalHeight + 10f)

                // Отрисовка фона
                lg.fill(20f, 25f, 30f, 245f)
                lg.stroke(255f, 80f)
                lg.strokeWeight(1f)
                lg.setImage(GradientInfo(LColor(20f, 25f, 30f)*1.6f, LColor(20f, 25f, 30f)*0.8f, LPoint(0, 0), LPoint(0, 100),
                    LPoint(1, 100)).generate(),center, width v totalHeight)
                lg.setImage(ShadowFrameInfo((width v totalHeight).toLPoint()/2, intensity = 0.2f, spread = 15).generate(),center, width v totalHeight)
                lg.setImage(ShadowFrameInfo((width v totalHeight).toLPoint()/2, intensity = 0.3f, spread = 5, type = ShadowType.OUTER).generate(),center, (width v totalHeight) + (10 v 10))
                lg.noStroke()

                // Текст рисуем от верхнего левого угла нашего бокса
                val topLeft = center + (-width / 2f v totalHeight / 2f)
                var drawY = topLeft.y - 10f
                lines.forEach { 
                    // render возвращает высоту строки, которую мы вычитаем для следующей строки
                    drawY -= it.render(lg, Vec2(topLeft.x + 10f, drawY), width - 20f)
                }
            }
        }
    }

    open fun getTooltipLines(): List<TooltipLine> {
        val list = mutableListOf<TooltipLine>()
        val fullTag = itemType.tag
        val tag = fullTag.replace(':', '.') // Используем . вместо : для корректного поиска
        
        // Название (цвет зависит от редкости)
        val nameKey = "items.$tag.name"
        val name = try { AppState.getLanguageValue(nameKey) } catch (e: Exception) { fullTag }
        list.add(TooltipLine.Title(name, color = itemType.rarity.color))
        
        // Редкость
        list.add(TooltipLine.Stat("Редкость:", itemType.rarity.label, color = itemType.rarity.color))
        list.add(TooltipLine.Separator)
        
        // Описание
        val descKey = "items.$tag.description"
        val desc = try { AppState.getLanguageValue(descKey) } catch (e: Exception) { null }
        if (desc != null) {
            list.add(TooltipLine.Description(desc))
        }

        // Тип (опционально)
        list.add(TooltipLine.Separator)
        
        return list
    }
    val consumed: Boolean = true
    val gameController: GameController get() = gameCycle.gameController
    val coreController: CoreController get() = gameController.coreController

    var isChoose = false
    var count = 1;

    open fun baseRender(lg: LGraphics, pos: Vec2, size: Vec2) {
        if (itemType.texture != "") {
            when (itemType.usingVariants) {
                is UsingVariants.PlaceTile -> {
                    lg.setImage(coreController.spriteLoader.getValue(itemType.texture), pos, size*0.7f, itemType.renderConfig.flipX)
                }
                is UsingVariants.PlaceWall -> {
                    lg.setImage(coreController.spriteLoader.getValue(itemType.texture), pos, size*0.9f, itemType.renderConfig.flipX)
                }
                else -> {
                    lg.setImage(coreController.spriteLoader.getValue(itemType.texture), pos, size, itemType.renderConfig.flipX)
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
        val renderSize = size * itemType.renderConfig.worldSize
        val visual = stackVisualCount()

        for (i in 0 until visual) {
            val offset = stackOffset(visual-i-1, renderSize)
            var renderPos = pos + itemType.renderConfig.worldDelta * size - offset
            baseRender(lg, renderPos, renderSize)
        }
    }

    open fun cellRender(lg: LGraphics, pos: Vec2, size: Vec2, cell: InventoryCell? = null) {
        val renderPos = pos + itemType.renderConfig.slotDelta * (size * itemType.renderConfig.sizeInSlot)
        val renderSize = size * itemType.renderConfig.sizeInSlot
        baseRender(lg, renderPos, renderSize)
        renderCount(lg, pos, size)
    }

    open fun cellDragRender(lg: LGraphics, pos: Vec2, size: Vec2, cell: InventoryCell? = null) {
        val renderPos = pos + itemType.renderConfig.slotDelta * (size * itemType.renderConfig.sizeInSlot)
        val renderSize = size * itemType.renderConfig.sizeInSlot * 1.1f
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
        if (itemType.usingVariants is UsingVariants.PlaceWall) {
            return WallHandItem(this, component, gameCycle.mapApi.getRegisteredWallType((itemType.usingVariants as UsingVariants.PlaceWall).wallTag))
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