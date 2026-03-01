package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Input.KeyCode
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.FrameLimiter
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Windows.InventoryCell
import la.vok.Game.GameContent.Windows.InventoryCellType
import la.vok.Game.GameController.PlayerControl
import la.vok.LLibs.AnimationType
import la.vok.LLibs.FloatAnimation
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import kotlin.math.sin

class WGamePanel(windowsManager: WindowsManager, var gameController: GameController) : WStandartPanel(windowsManager) {
    val playerControl: PlayerControl get() = gameController.playerControl

    private var inventoryAnimating = false
    private var inventoryAnimDir = 0
    private var inventoryAnimProgress = 0f

    // =====================================================================
    // НАСТРОЙКИ — менять здесь
    // =====================================================================

    private val cellSize = 55.1 v 55.1
    private val cellSpacing = 55f
    private val inventoryMargin = 15f
    private val inventoryAnimSpeed = 3f
    private val hotbarWaveOverlap = 0.9f       // насколько ячейки хотбара перекрываются (0..1)
    private val inventoryAnim = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))
    private val hotbarAnim = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))
    private val hotbarAnimY = FloatAnimation(0f, 1f, AnimationType.EaseIn(6f))


    // Хотбар закрыт — позиция по центру снизу (выравнивание align.y = -1)
    private fun hotbarClosedPos(i: Int) = ((i - 4.5f) * cellSpacing) v 0f
    private val hotbarClosedAlign = 0 v -1

    // Хотбар открыт — позиция снизу слева (выравнивание align.y = 0)
    private fun hotbarOpenPos(i: Int): Vec2 {
        val x = -logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + i * cellSpacing
        val y = logicalSize.y / 2f - inventoryMargin - cellSize.y / 2f - 4 * cellSpacing - 10f
        return x v y
    }
    private val hotbarOpenAlign = 0 v 0

    // Инвентарь открыт — сетка сверху слева
    private fun inventoryOpenPos(idx: Int): Vec2 {
        val col = idx % 10
        val row = idx / 10
        val x = -logicalSize.x / 2f + inventoryMargin + cellSize.x / 2f + col * cellSpacing
        val y = logicalSize.y / 2f - inventoryMargin - cellSize.y / 2f - row * cellSpacing
        return x v y
    }
    private val inventoryOpenAlign = 0 v 0

    // Инвентарь закрыт — стартовая точка (левый нижний угол)
    private val inventoryClosedPos: Vec2
        get() {
            val x = -logicalSize.x / 2f + inventoryMargin - cellSize.x * 3f
            val y = -logicalSize.y / 2f + inventoryMargin - cellSize.y * 3f
            return x v y
        }
    private val inventoryClosedAlign = 0 v 0

    // =====================================================================

    override var padding: Vec2 get() = 10 v 10; set(value) {}
    override var tags: Array<String> get() = arrayOf("game"); set(value) {}

    private val hotbarCells = ArrayList<Pair<InventoryCell, Vec2>>()
    private val inventoryCells = ArrayList<Pair<InventoryCell, Vec2>>()
    private fun allCells() = hotbarCells + inventoryCells

    private var draggedCell: InventoryCell? = null
    private var dragMousePos: Vec2 = Vec2.ZERO

    private fun remapProgress(t: Float, i: Int, total: Int, overlap: Float): Float {
        val step = (1f - overlap) / total
        val start = i * step
        val end = start + overlap + step
        return ((t - start) / (end - start)).coerceIn(0f, 1f)
    }

    private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t
    private fun lerpVec(a: Vec2, b: Vec2, t: Float) = lerp(a.x, b.x, t) v lerp(a.y, b.y, t)
    private fun lerpVec(a: Vec2, b: Vec2, t: Vec2) = lerp(a.x, b.x, t.x) v lerp(a.y, b.y, t.y)

    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        lg.bg(0f)
        gameController.renderTick(lg)
    }

    override fun postDraw(mainRender: MainRender) {
        super.postDraw(mainRender)
        val dragged = draggedCell ?: return
        val item = dragged.slot?.item ?: return
        item.cellDragRender(lg, dragMousePos, dragged.size, dragged)
    }

    fun buildInventoryButtons() {
        AppState.logger.info("WGamePanel rebuild buttons")
        windowElements.clear()
        hotbarCells.clear()
        inventoryCells.clear()

        val player = gameController.gameCycle.entityApi.getById(playerControl.playerId) ?: return

        for (i in 0..9) {
            val cell = InventoryCell(
                this, hotbarClosedPos(i), cellSize,
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
                    this, inventoryClosedPos, cellSize,
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

    private fun updateCellPositions() {
        val globalT = inventoryAnimProgress

        hotbarCells.forEachIndexed { i, (cell, _) ->
            val cellT = hotbarAnim.evaluate(remapProgress(globalT, i, 10, hotbarWaveOverlap))
            val cellTY = hotbarAnimY.evaluate(remapProgress(globalT, i, 10, hotbarWaveOverlap))

            val closedPos = hotbarClosedPos(i)
            val openPos = hotbarOpenPos(i)
            cell.align = lerpVec(hotbarClosedAlign, hotbarOpenAlign, cellT v cellTY)
            cell.position = lerpVec(closedPos, openPos, cellT v cellTY)
            cell.markDirty()
        }

        val invT = inventoryAnim.evaluate(globalT)
        inventoryCells.forEachIndexed { i, (cell, openPos) ->
            cell.align = inventoryOpenAlign
            cell.position = lerpVec(inventoryClosedPos, openPos, invT)
            cell.isVisible = globalT > 0f
            cell.markDirty()
        }
    }

    override fun update() {
        super.update()
        if (inventoryAnimating) {
            val dt = FrameLimiter.logicDeltaTime
            inventoryAnimProgress = (inventoryAnimProgress + dt * inventoryAnimSpeed * inventoryAnimDir)
                .coerceIn(0f, 1f)
            updateCellPositions()
            if (inventoryAnimProgress <= 0f || inventoryAnimProgress >= 1f) inventoryAnimating = false
        }
    }

    private fun startDrag(cell: InventoryCell, position: Vec2) {
        if (cell.slot?.item == null) return
        draggedCell = cell
        dragMousePos = position
    }

    private fun endDrag(position: Vec2) {
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
                val player = gameController.gameCycle.entityApi.getById(playerControl.playerId) ?: return
                gameController.gameCycle.itemsApi.spawnItemEntity(
                    item.copy().apply { count = item.count }, player.position, randomVelocity = true
                )
                slot.item = null
            }
        }
    }

    fun insideUxElement(position: Vec2) = windowElements.any { it.inside(position) }

    // Хелпер: Pair<Int,Int> → Vec2
    private fun Pair<Int, Int>.toVec2() = first.toFloat() v second.toFloat()

    override fun leftPressed(position: Vec2) {
        super.leftPressed(position)
        val cell = allCells().map { it.first }.firstOrNull { it.inside(position) }
        if (cell != null) { startDrag(cell, position); return }
        playerControl.leftPressed(position)
    }

    override fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        super.leftUpdate(position, oldPosition)
        dragMousePos = position
        if (draggedCell != null) {
            allCells().forEach { (cell, _) ->
                cell.isDragTarget = cell !== draggedCell && cell.inside(position)
            }
        }
    }

    override fun leftReleased(position: Vec2) {
        super.leftReleased(position)
        if (draggedCell != null) { endDrag(position); return }
        playerControl.leftReleased(position)
    }

    override fun keyPressed(key: Int) {
        when (key) {
            KeyCode.TAB -> {
                if (!inventoryAnimating) {
                    playerControl.toggleInventory()
                    inventoryAnimDir = if (playerControl.isInventoryOpen) 1 else -1
                    inventoryAnimating = true
                }
            }
            KeyCode.NUM_1 -> playerControl.chooseSlot(0)
            KeyCode.NUM_2 -> playerControl.chooseSlot(1)
            KeyCode.NUM_3 -> playerControl.chooseSlot(2)
            KeyCode.NUM_4 -> playerControl.chooseSlot(3)
            KeyCode.NUM_5 -> playerControl.chooseSlot(4)
            KeyCode.NUM_6 -> playerControl.chooseSlot(5)
            KeyCode.NUM_7 -> playerControl.chooseSlot(6)
            KeyCode.NUM_8 -> playerControl.chooseSlot(7)
            KeyCode.NUM_9 -> playerControl.chooseSlot(8)
            KeyCode.NUM_0 -> playerControl.chooseSlot(9)
        }
    }

    override fun keyUpdate(key: Int, heldTime: Float) {
        when (key) {
            KeyCode.A -> playerControl.tapA()
            KeyCode.D -> playerControl.tapD()
            KeyCode.SPACE -> playerControl.tapSpace()
        }
    }

    override fun rightPressed(position: Vec2) {
        super.rightPressed(position)
        if (insideUxElement(position)) return
        playerControl.rightPressed(position)
    }

    override fun rightUpdate(position: Vec2, oldPosition: Vec2) {
        super.rightUpdate(position, oldPosition)
        playerControl.rightUpdate(position, oldPosition)
    }

    override fun rightReleased(position: Vec2) {
        super.rightReleased(position)
        playerControl.rightReleased(position)
    }
}