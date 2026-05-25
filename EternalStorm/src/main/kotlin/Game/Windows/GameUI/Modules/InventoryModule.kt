package la.vok.Game.Windows.GameUI.Modules

import la.vok.Core.CoreContent.Windows.Modules.IUiModule
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.Windows.InventoryCell
import la.vok.Game.Windows.InventoryCellType
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameController.PlayerControl
import la.vok.LLibs.AnimationType
import la.vok.LLibs.FloatAnimation
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import processing.event.MouseEvent

class InventoryModule(
    val playerControl: PlayerControl
) : IUiModule {
    override val id: String = "inventory"
    override var isEnabled: Boolean = true

    // =====================================================================
    // НАСТРОЙКИ (перенесены из InventoryPanelController)
    // =====================================================================

    val cellSize = 55.1 v 55.1
    val cellSpacing = 55f
    val inventoryMargin = 15f
    val inventoryAnimSpeed = 3f
    val hotbarWaveOverlap = 0.9f

    private val inventoryAnim = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))
    private val hotbarAnim    = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))
    private val hotbarAnimY   = FloatAnimation(0f, 1f, AnimationType.EaseIn(6f))

    private fun hotbarClosedPos(i: Int) = ((i - 4.5f) * cellSpacing) v 0f
    private val hotbarClosedAlign = 0 v -1

    private fun hotbarOpenPos(window: AbstractWindow, i: Int): Vec2 {
        val x = -window.logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + i * cellSpacing
        val y = window.logicalSize.y / 2f - inventoryMargin - cellSize.y / 2f - 4 * cellSpacing - 10f
        return x v y
    }
    private val hotbarOpenAlign = Vec2.ZERO

    private fun inventoryOpenPos(window: AbstractWindow, idx: Int): Vec2 {
        val col = idx % 10
        val row = idx / 10
        val x = -window.logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + col * cellSpacing
        val y = window.logicalSize.y / 2f - inventoryMargin - cellSize.y / 2f - row * cellSpacing
        return x v y
    }
    private val inventoryOpenAlign = Vec2.ZERO

    private fun inventoryClosedPos(window: AbstractWindow): Vec2 {
        val x = -window.logicalSize.x / 2f + inventoryMargin - cellSize.x * 3f
        val y = -window.logicalSize.y / 2f + inventoryMargin - cellSize.y * 3f
        return x v y
    }
    private val inventoryClosedAlign = Vec2.ZERO

    // =====================================================================

    val hotbarCells = ArrayList<Pair<InventoryCell, Vec2>>()
    val inventoryCells = ArrayList<Pair<InventoryCell, Vec2>>()
    fun allCells() = hotbarCells + inventoryCells

    // --- Анимация ---
    var animating = false
    var animDir = 0
    var animProgress = 0f

    // --- Состояние инвентаря ---
    var heldItem: Item? = null
    var dragMousePos: Vec2 = Vec2.ZERO

    // --- Helpers ---
    private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t
    private fun lerpVec(a: Vec2, b: Vec2, t: Float) = lerp(a.x, b.x, t) v lerp(a.y, b.y, t)
    private fun lerpVec(a: Vec2, b: Vec2, t: Vec2) = lerp(a.x, b.x, t.x) v lerp(a.y, b.y, t.y)
    private fun remapProgress(t: Float, i: Int, total: Int, overlap: Float): Float {
        val step = (1f - overlap) / total
        val start = i * step
        val end = start + overlap + step
        return ((t - start) / (end - start)).coerceIn(0f, 1f)
    }

    override fun onAttach(window: AbstractWindow) {
        build(window)
    }

    fun build(window: AbstractWindow) {
        AppState.logger.info("InventoryModule build cells")
        
        // Remove old cells if any
        hotbarCells.forEach { window.windowElements.remove(it.first) }
        inventoryCells.forEach { window.windowElements.remove(it.first) }
        
        hotbarCells.clear()
        inventoryCells.clear()

        val player = playerControl.getPlayerEntity() ?: return

        for (i in 0..9) {
            val cell = createCell(window, hotbarClosedPos(i), hotbarClosedAlign, i, InventoryCellType.HOTBAR, player)
            hotbarCells += cell to hotbarClosedPos(i)
            window.windowElements += cell
        }

        for (idx in 0..39) {
            val cell = createCell(window, inventoryClosedPos(window), inventoryClosedAlign, 10 + idx, InventoryCellType.INVENTORY, player)
            inventoryCells += cell to inventoryOpenPos(window, idx)
            window.windowElements += cell
        }

        updateCellPositions(window)
    }

    private fun createCell(
        window: AbstractWindow,
        pos: Vec2,
        align: Vec2,
        slotIndex: Int,
        type: InventoryCellType,
        player: PlayerEntity
    ) = InventoryCell(
        window, pos, cellSize, align,
        slot = player.inventory?.itemContainer?.getSlot(slotIndex),
        cellType = type,
        leftClick = { playerControl.chooseSlot(slotIndex) },
        update = {
            val cell = this as InventoryCell
            val isInvOpen = playerControl.isInventoryOpen
            val item = cell.slot?.item
            
            if (!isInvOpen) {
                cell.tooltip = null
            } else if (cell.tooltip == null) {
                cell.tooltip = item?.generateTooltip()
            }
        }
    )

    fun updateCellPositions(window: AbstractWindow) {
        hotbarCells.forEachIndexed { i, (cell, _) ->
            val cellT  = hotbarAnim.evaluate(remapProgress(animProgress, i, 10, hotbarWaveOverlap))
            val cellTY = hotbarAnimY.evaluate(remapProgress(animProgress, i, 10, hotbarWaveOverlap))
            cell.align    = lerpVec(hotbarClosedAlign, hotbarOpenAlign, cellT v cellTY)
            cell.position = lerpVec(hotbarClosedPos(i), hotbarOpenPos(window, i), cellT v cellTY)
            cell.markDirty()
        }

        val invT = inventoryAnim.evaluate(animProgress)
        inventoryCells.forEachIndexed { i, (cell, openPos) ->
            cell.align    = inventoryOpenAlign
            cell.position = lerpVec(inventoryClosedPos(window), openPos, invT)
            cell.isVisible = animProgress > 0f
            cell.markDirty()
        }
    }

    // --- Логика зажатия ПКМ ---
    private enum class TransferMode { TAKE, GIVE, NONE }
    private var rightButtonHeld = false
    private var lastRightHeldCell: InventoryCell? = null
    private var rightHoldTimer = 0f
    private var rightHoldSpeed = 10f // Тиков между переносами
    private var transferCount = 0
    private var currentMode = TransferMode.NONE

    override fun physicUpdate(window: AbstractWindow) {
        if (rightButtonHeld && lastRightHeldCell != null) {
            rightHoldTimer++
            // Ускорили в 5 раз темп роста (было / 5f)
            val currentSpeed = maxOf(1f, rightHoldSpeed - (transferCount * 1.0f))
            if (rightHoldTimer >= currentSpeed) {
                rightHoldTimer = 0f
                transferCount++
                processRightClick(lastRightHeldCell!!)
            }
        }
    }

    override fun leftPressed(window: AbstractWindow, position: Vec2) {
        if (!playerControl.isInventoryOpen) return
        val cell = allCells().map { it.first }.firstOrNull { it.isVisible && it.inside(position) }
        if (cell != null) {
            handleLeftClick(cell)
        }
    }

    override fun rightPressed(window: AbstractWindow, position: Vec2) {
        if (!playerControl.isInventoryOpen) return
        val cell = allCells().map { it.first }.firstOrNull { it.isVisible && it.inside(position) }
        if (cell != null) {
            val slot = cell.slot
            currentMode = if (heldItem == null && slot?.item != null) TransferMode.TAKE else TransferMode.GIVE

            rightButtonHeld = true
            lastRightHeldCell = cell
            rightHoldTimer = 0f
            transferCount = 0
            processRightClick(cell)
        }
    }

    override fun rightReleased(window: AbstractWindow, position: Vec2) {
        handleRightRelease()
    }

    override fun mouseWheel(window: AbstractWindow, position: Vec2, event: MouseEvent) {
        if (!playerControl.isInventoryOpen) return
        val cell = allCells().map { it.first }.firstOrNull { it.isVisible && it.inside(position) }
        if (cell != null) {
            handleMouseWheel(cell, event)
        }
    }

    private fun handleLeftClick(cell: InventoryCell) {
        val slot = cell.slot ?: return
        val itemInSlot = slot.item
        val handItem = heldItem

        when {
            // В руках пусто -> Берем всё из слота
            handItem == null -> {
                heldItem = slot.item
                slot.item = null
            }
            // В руках предмет того же типа -> Стакаем
            itemInSlot != null && handItem.canStackable(itemInSlot) -> {
                val toAdd = minOf(handItem.count, itemInSlot.leftToStack())
                itemInSlot.count += toAdd
                handItem.count -= toAdd
                if (handItem.count <= 0) heldItem = null
            }
            // В руках предмет, а слот пустой -> Кладем всё
            itemInSlot == null -> {
                slot.item = handItem
                heldItem = null
            }
            // Предметы разные -> Свапаем
            else -> {
                heldItem = itemInSlot
                slot.item = handItem
            }
        }
    }

    private fun handleRightClick(cell: InventoryCell) {
        // Метод перенесен в override rightPressed
    }

    private fun handleRightRelease() {
        rightButtonHeld = false
        lastRightHeldCell = null
        currentMode = TransferMode.NONE
    }

    private fun processRightClick(cell: InventoryCell) {
        val slot = cell.slot ?: return
        val itemInSlot = slot.item
        val handItem = heldItem

        when (currentMode) {
            TransferMode.TAKE -> {
                if (itemInSlot == null) return
                if (handItem == null) {
                    heldItem = itemInSlot.copy().apply { count = 1 }
                    itemInSlot.count--
                } else if (handItem.canStackable(itemInSlot)) {
                    if (handItem.leftToStack() > 0) {
                        handItem.count++
                        itemInSlot.count--
                    }
                }
                if (itemInSlot.count <= 0) slot.item = null
            }
            TransferMode.GIVE -> {
                if (handItem == null) return
                if (itemInSlot == null) {
                    slot.item = handItem.copy().apply { count = 1 }
                    handItem.count--
                } else if (handItem.canStackable(itemInSlot)) {
                    if (itemInSlot.leftToStack() > 0) {
                        itemInSlot.count++
                        handItem.count--
                    }
                }
                if (handItem.count <= 0) heldItem = null
            }
            else -> {}
        }
    }

    private fun handleMouseWheel(cell: InventoryCell, event: MouseEvent) {
        val slot = cell.slot ?: return
        val itemInSlot = slot.item
        val handItem = heldItem
        val count = event.count // -1 up, 1 down

        // Колесико вверх -> Берем 5% от стака (минимум 1)
        if (count < 0) {
            if (itemInSlot == null) return
            // Замедлили в 5 раз (было /4, стало /20)
            val toTake = maxOf(1, itemInSlot.count / 20)
            
            val taken = when {
                handItem == null -> {
                    heldItem = itemInSlot.copy().apply { this.count = toTake }
                    toTake
                }
                handItem.canStackable(itemInSlot) -> {
                    val actual = minOf(toTake, handItem.leftToStack())
                    handItem.count += actual
                    actual
                }
                else -> 0
            }

            itemInSlot.count -= taken
            if (itemInSlot.count <= 0) slot.item = null
        } 
        // Колесико вниз -> Кладем 5% от удерживаемого (минимум 1)
        else {
            val hand = handItem ?: return
            
            // Если слот пустой или содержит тот же тип
            if (itemInSlot == null || hand.canStackable(itemInSlot)) {
                val toPut = maxOf(1, hand.count / 20)
                val actualSource = if (itemInSlot == null) hand.itemType.maxInStack else itemInSlot.leftToStack()
                val actual = minOf(toPut, actualSource)
                
                if (actual > 0) {
                    if (itemInSlot == null) {
                        slot.item = hand.copy().apply { this.count = actual }
                    } else {
                        itemInSlot.count += actual
                    }
                    hand.count -= actual
                    if (hand.count <= 0) heldItem = null
                }
            }
        }
    }

    override fun update(window: AbstractWindow) {
        dragMousePos = window.coreController.mouseInput.logicalPosition

        if (!animating) return
        val dt = FrameLimiter.logicDeltaTime
        animProgress = (animProgress + dt * inventoryAnimSpeed * animDir).coerceIn(0f, 1f)
        updateCellPositions(window)
        if (animProgress <= 0f || animProgress >= 1f) animating = false
    }

    fun toggleInventory() {
        if (animating) return
        playerControl.toggleInventory()
        animDir = if (playerControl.isInventoryOpen) 1 else -1
        animating = true
        
        // Если закрываем инвентарь и в руках что-то есть — выбрасываем (или возвращаем в инвентарь)
        if (animDir == -1 && heldItem != null) {
            val player = playerControl.getPlayerEntity() ?: return
            player.inventory?.itemContainer?.addItem(heldItem!!)
            heldItem = null
        }
    }

    override fun postDraw(window: AbstractWindow, lg: LGraphics) {
        val item = heldItem ?: return
        item.cellDragRender(lg, dragMousePos, cellSize, null)
    }

    // Удаляем старые методы Drag-and-drop
    fun startDrag(cell: InventoryCell, position: Vec2) {}
    fun updateDrag(position: Vec2) {}
    fun endDrag(position: Vec2, onDrop: (item: Item) -> Unit) {}
}
