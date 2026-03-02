package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.Windows.InventoryCell
import la.vok.Game.Windows.InventoryCellType
import la.vok.Game.GameController.PlayerControl
import la.vok.LLibs.AnimationType
import la.vok.LLibs.FloatAnimation
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class InventoryPanelController(
    val window: AbstractWindow,
    val playerControl: PlayerControl
) {
    // =====================================================================
    // НАСТРОЙКИ
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

    private fun hotbarOpenPos(i: Int): Vec2 {
        val x = -window.logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + i * cellSpacing
        val y = window.logicalSize.y / 2f - inventoryMargin - cellSize.y / 2f - 4 * cellSpacing - 10f
        return x v y
    }
    private val hotbarOpenAlign = 0 v 0

    private fun inventoryOpenPos(idx: Int): Vec2 {
        val col = idx % 10
        val row = idx / 10
        val x = -window.logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + col * cellSpacing
        val y = window.logicalSize.y / 2f - inventoryMargin - cellSize.y / 2f - row * cellSpacing
        return x v y
    }
    private val inventoryOpenAlign = 0 v 0

    private val inventoryClosedPos: Vec2
        get() {
            val x = -window.logicalSize.x / 2f + inventoryMargin - cellSize.x * 3f
            val y = -window.logicalSize.y / 2f + inventoryMargin - cellSize.y * 3f
            return x v y
        }
    private val inventoryClosedAlign = 0 v 0

    // =====================================================================

    val hotbarCells = ArrayList<Pair<InventoryCell, Vec2>>()
    val inventoryCells = ArrayList<Pair<InventoryCell, Vec2>>()
    fun allCells() = hotbarCells + inventoryCells

    // --- Анимация ---
    var animating = false
    var animDir = 0
    var animProgress = 0f

    // --- Drag ---
    var draggedCell: InventoryCell? = null
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

    // =====================================================================
    // ПОСТРОЕНИЕ
    // =====================================================================

    fun build(windowElements: ArrayList<WindowElement>) {
        AppState.logger.info("InventoryPanelController build cells")
        windowElements.clear()
        hotbarCells.clear()
        inventoryCells.clear()

        val player = playerControl.getPlayerEntity() ?: return

        for (i in 0..9) {
            val cell = InventoryCell(
                window, hotbarClosedPos(i), cellSize,
                hotbarClosedAlign,
                slot = player.inventory?.itemContainer?.getSlot(i),
                cellType = InventoryCellType.HOTBAR,
                leftClick = { playerControl.chooseSlot(i) }
            )
            hotbarCells += cell to hotbarClosedPos(i)
            windowElements += cell
        }

        for (x in 0..9) {
            for (y in 0..3) {
                val idx = y * 10 + x
                val cell = InventoryCell(
                    window, inventoryClosedPos, cellSize,
                    inventoryClosedAlign,
                    slot = player.inventory?.itemContainer?.getSlot(10 + idx),
                    cellType = InventoryCellType.INVENTORY,
                    leftClick = { playerControl.chooseSlot(10 + idx) }
                )
                inventoryCells += cell to inventoryOpenPos(idx)
                windowElements += cell
            }
        }

        updateCellPositions()
    }

    // =====================================================================
    // АНИМАЦИЯ
    // =====================================================================

    fun updateCellPositions() {
        hotbarCells.forEachIndexed { i, (cell, _) ->
            val cellT  = hotbarAnim.evaluate(remapProgress(animProgress, i, 10, hotbarWaveOverlap))
            val cellTY = hotbarAnimY.evaluate(remapProgress(animProgress, i, 10, hotbarWaveOverlap))
            cell.align    = lerpVec(hotbarClosedAlign, hotbarOpenAlign, cellT v cellTY)
            cell.position = lerpVec(hotbarClosedPos(i), hotbarOpenPos(i), cellT v cellTY)
            cell.markDirty()
        }

        val invT = inventoryAnim.evaluate(animProgress)
        inventoryCells.forEachIndexed { i, (cell, openPos) ->
            cell.align    = inventoryOpenAlign
            cell.position = lerpVec(inventoryClosedPos, openPos, invT)
            cell.isVisible = animProgress > 0f
            cell.markDirty()
        }
    }

    fun update() {
        if (!animating) return
        val dt = FrameLimiter.logicDeltaTime
        animProgress = (animProgress + dt * inventoryAnimSpeed * animDir).coerceIn(0f, 1f)
        updateCellPositions()
        if (animProgress <= 0f || animProgress >= 1f) animating = false
    }

    fun toggleInventory() {
        if (animating) return
        playerControl.toggleInventory()
        animDir = if (playerControl.isInventoryOpen) 1 else -1
        animating = true
    }

    // =====================================================================
    // DRAG & DROP
    // =====================================================================

    fun startDrag(cell: InventoryCell, position: Vec2) {
        if (cell.slot?.item == null) return
        draggedCell = cell
        dragMousePos = position
    }

    fun updateDrag(position: Vec2) {
        dragMousePos = position
        if (draggedCell == null) return
        allCells().forEach { (cell, _) ->
            cell.isDragTarget = cell !== draggedCell && cell.inside(position)
        }
    }

    fun endDrag(position: Vec2, onDrop: (item: Item) -> Unit) {
        val dragged = draggedCell ?: return
        draggedCell = null
        allCells().forEach { (cell, _) -> cell.isDragTarget = false }

        val target = allCells().map { it.first }.firstOrNull { it !== dragged && it.inside(position) }

        when {
            target != null -> {
                val ds = dragged.slot
                val ts = target.slot
                if (ds != null && ts != null) ds.itemContainer.swap(ds.id, ts.id)
            }
            dragged.inside(position) -> {}
            else -> {
                val slot = dragged.slot ?: return
                val item = slot.item ?: return
                onDrop(item)
                slot.item = null
            }
        }
    }

    fun renderDragged(lg: LGraphics) {
        val dragged = draggedCell ?: return
        val item = dragged.slot?.item ?: return
        item.cellDragRender(lg, dragMousePos, dragged.size, dragged)
    }
}