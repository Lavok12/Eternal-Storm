package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Crafts.CraftType
import la.vok.Game.Windows.CraftCell
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameController.PlayerControl
import la.vok.LLibs.AnimationType
import la.vok.LLibs.FloatAnimation
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class CraftPanelController(
    val window: AbstractWindow,
    val playerControl: PlayerControl,
    val gameCycle: GameCycle
) {
    // =====================================================================
    // НАСТРОЙКИ
    // =====================================================================

    val cellSize = 55.1 v 55.1
    val cellSpacing = 55f
    val craftsPerRow = 10
    val inventoryMargin = 15f
    val animSpeed = 3f
    val updateInterval = 60
    val waveOverlap = 0.6f

    private val craftAnim = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))

    // =====================================================================

    private val craftCells = ArrayList<Pair<CraftCell, Vec2>>()
    var animating = false
    var animDir = 0
    var animProgress = 0f
    private var ticksSinceUpdate = 0

    private val closedPos: Vec2
        get() = -1000f v -2000f

    private fun sortedByPercent(): List<Pair<CraftType, Float>> {
        val snap = gameCycle.craftApi.snapshot(*getContainers().toTypedArray())
        val sorted = gameCycle.craftApi.allCrafts
            .map { craft -> craft to gameCycle.craftApi.getCompletionPercent(craft, snap) }
            .sortedByDescending { (_, percent) -> -percent }

        val result = ArrayList<Pair<CraftType, Float>>(sorted.size)
        var i = 0
        while (i < sorted.size) {
            val rowEnd = minOf(i + craftsPerRow, sorted.size)
            val row = sorted.subList(i, rowEnd).sortedBy { (_, percent) -> -percent }
            result.addAll(row)
            i += craftsPerRow
        }
        return result
    }

    private fun openPos(idx: Int): Vec2 {
        val col = idx % craftsPerRow
        val row = idx / craftsPerRow
        val x = -window.logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + col * cellSpacing
        val y = -window.logicalSize.y / 2f + inventoryMargin + cellSize.y / 2f + row * cellSpacing
        return x v y
    }

    private fun remapProgress(t: Float, row: Int, col: Int, totalRows: Int, totalCols: Int): Float {
        val totalSteps = totalRows + totalCols - 1
        val diag = (totalRows - 1 - row) + col
        val step = (1f - waveOverlap) / totalSteps.coerceAtLeast(1)
        val start = diag * step
        val end = start + waveOverlap + step
        return ((t - start) / (end - start)).coerceIn(0f, 1f)
    }

    private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t
    private fun lerpVec(a: Vec2, b: Vec2, t: Float) = lerp(a.x, b.x, t) v lerp(a.y, b.y, t)

    private fun getContainers() = listOfNotNull(
        playerControl.getPlayerEntity()?.inventory?.itemContainer
    )

    private fun doCraft(craft: CraftType, windowElements: ArrayList<WindowElement>) {
        val container = getContainers().firstOrNull() ?: return
        val entity = playerControl.getPlayerEntity() ?: return
        val success = gameCycle.craftApi.craft(container, craft, entity, 1)
        if (success) {
            refresh(windowElements)
            updatePositions()
        }
    }

    // =====================================================================
    // ПОСТРОЕНИЕ
    // =====================================================================

    fun build(windowElements: ArrayList<WindowElement>) {
        clear(windowElements)
        val sorted = sortedByPercent()

        sorted.forEachIndexed { idx, (craft, percent) ->
            val cell = CraftCell(
                window = window,
                position = closedPos,
                size = cellSize,
                align = Vec2.ZERO,
                craft = craft,
                completionPercent = percent,
                leftClick = { doCraft(craft, windowElements) }
            )
            cell.isVisible = false
            craftCells += cell to openPos(idx)
            windowElements += cell
        }

        updatePositions()
    }

    fun refresh(windowElements: ArrayList<WindowElement>) {
        val sorted = sortedByPercent()

        while (craftCells.size < sorted.size) {
            val idx = craftCells.size
            val (craft, percent) = sorted[idx]
            val cell = CraftCell(
                window = window,
                position = if (animProgress > 0f) openPos(idx) else closedPos,
                size = cellSize,
                align = Vec2.ZERO,
                craft = craft,
                completionPercent = percent,
                leftClick = { doCraft(craft, windowElements) }
            )
            cell.isVisible = animProgress > 0f
            craftCells += cell to openPos(idx)
            windowElements += cell
        }

        while (craftCells.size > sorted.size) {
            val (cell, _) = craftCells.removeLast()
            windowElements.remove(cell)
        }

        craftCells.forEachIndexed { idx, (cell, _) ->
            val (craft, percent) = sorted.getOrNull(idx) ?: return@forEachIndexed
            cell.craft = craft
            cell.completionPercent = percent
        }
    }

    fun clear(windowElements: ArrayList<WindowElement>) {
        craftCells.forEach { (cell, _) -> windowElements.remove(cell) }
        craftCells.clear()
    }

    // =====================================================================
    // АНИМАЦИЯ
    // =====================================================================

    private fun updatePositions() {
        val totalRows = ((craftCells.size - 1) / craftsPerRow + 1).coerceAtLeast(1)

        craftCells.forEachIndexed { idx, (cell, open) ->
            val row = idx / craftsPerRow
            val col = idx % craftsPerRow
            val rowT = craftAnim.evaluate(remapProgress(animProgress, row, col, totalRows, craftsPerRow))
            cell.position = lerpVec(closedPos, open, rowT)
            cell.isVisible = animProgress > 0f
            cell.markDirty()
        }
    }

    fun update(windowElements: ArrayList<WindowElement>) {
        if (animating) {
            val dt = FrameLimiter.logicDeltaTime
            animProgress = (animProgress + dt * animSpeed * animDir).coerceIn(0f, 1f)
            updatePositions()
            if (animProgress <= 0f || animProgress >= 1f) animating = false
        }

        if (animProgress > 0f) {
            ticksSinceUpdate++
            if (ticksSinceUpdate >= updateInterval) {
                ticksSinceUpdate = 0
                refresh(windowElements)
                updatePositions()
            }
        }

        if (animProgress <= 0f && animDir == -1 && !animating) {
            onInventoryClosed(windowElements)
        }
    }

    fun isMouseOverAnyCraft(position: Vec2): Boolean =
        craftCells.any { (cell, _) -> cell.isVisible && cell.inside(position) }

    fun show(windowElements: ArrayList<WindowElement>) {
        if (craftCells.isEmpty()) build(windowElements)
        animDir = 1
        animating = true
    }

    fun hide(windowElements: ArrayList<WindowElement>) {
        animDir = -1
        animating = true
    }

    fun onInventoryClosed(windowElements: ArrayList<WindowElement>) {
        clear(windowElements)
        animProgress = 0f
        animDir = 0
        animating = false
    }
}