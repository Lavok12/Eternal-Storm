package la.vok.Game.Windows.GameUI.Modules.GameInterfaces

import la.vok.Core.CoreContent.Windows.Modules.IUiModule
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Game.GameContent.TileData.ChestTileData
import la.vok.Game.GameController.PlayerControl
import la.vok.Game.Windows.InventoryCell
import la.vok.Game.Windows.InventoryCellType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Game.Windows.GameUI.Modules.InventoryModule
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.LavokLibrary.Gradient.GradientInfo
import la.vok.LavokLibrary.Gradient.ShadowFrameInfo
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.LPoint

class ChestUiInterface(
    val playerControl: PlayerControl,
    val chestData: ChestTileData
) : IUiModule {
    override val id: String = "chest_interface"
    override var isEnabled: Boolean = true

    private val cellSize = 50.0 v 50.0
    private val cellSpacing = 55f
    private val cells = ArrayList<InventoryCell>()
    private var chestBg: WindowElement? = null
    private var isInitialized = false
    private var animProgress = 0f

    override fun update(window: AbstractWindow) {
        if (!isInitialized) {
            initialize(window)
        }
        
        val inventoryModule = (window as? WGamePanel)?.moduleManager?.getModule<InventoryModule>("inventory")
        animProgress = inventoryModule?.animProgress ?: 1f
        
        // Закрываем интерфейс только если инвентарь полностью закрыт и не анимируется
        if (animProgress <= 0f && inventoryModule?.animating == false) {
            (window as? WGamePanel)?.moduleManager?.closeBlockInterface()
            return
        }

        val startX = 0f
        val startY = window.logicalSize.y / 2f - 350f

        cells.forEachIndexed { i, cell ->
            val col = i % 5
            val row = i / 5
            
            val targetX = startX + col * cellSpacing
            val targetY = startY - row * cellSpacing
            
            cell.position = targetX v targetY
            cell.isVisible = true
            cell.markDirty()
        }

        chestBg?.let { bg ->
            bg.position = startX + (2 * cellSpacing) v startY - (1.5f * cellSpacing)
            bg.isVisible = true
            bg.markDirty()
        }
    }

    private fun initialize(window: AbstractWindow) {
        cells.clear()

        // --- Добавление фона ---
        val bgWidth = 5.6f * cellSpacing
        val bgHeight = 4.6f * cellSpacing
        
        chestBg = WindowElement(
            window, Vec2.ZERO, bgWidth v bgHeight,
            render = { lg ->
                val alpha = animProgress * 255f
                val pos = positionWithCache
                
                // Темный градиент
                val bgTop = LColor(45f, 55f, 70f, 150f)
                val bgBot = LColor(8f, 12f, 18f, 110f)
                
                lg.setTint(255f, alpha)
                lg.setImage(
                    GradientInfo(
                        bgTop, bgBot,
                        LPoint(0, 0), LPoint(0, 100), LPoint(1, 100)
                    ).generate(),
                    pos, size
                )
                lg.noTint()
                
                // Внутренняя тень
                lg.setTint(255f, alpha)
                lg.setImage(
                    ShadowFrameInfo(
                        size.toLPoint() / 2, 
                        intensity = 0.45f,
                        spread = 20
                    ).generate(),
                    pos, size
                )
                lg.noTint()
            }
        ).apply { isVisible = false }
        window.windowElements.add(chestBg!!)

        for (i in 0 until chestData.itemContainer.size) {
            val cell = InventoryCell(
                window, Vec2.ZERO, cellSize, Vec2.ZERO,
                slot = chestData.itemContainer.getSlot(i),
                cellType = InventoryCellType.INVENTORY,
                leftClick = { pos: Vec2 -> handleLeftClick(window, this as InventoryCell) },
                rightClick = { pos: Vec2 -> handleRightClick(window, this as InventoryCell) }
            )
            cells.add(cell)
            window.windowElements.add(cell)
        }
        isInitialized = true
    }

    private fun handleLeftClick(window: AbstractWindow, cell: InventoryCell) {
        val inventoryModule = (window as? WGamePanel)?.moduleManager?.getModule<InventoryModule>("inventory") ?: return
        val slot = cell.slot ?: return
        val itemInSlot = slot.item
        val handItem = inventoryModule.heldItem

        when {
            handItem == null -> {
                inventoryModule.heldItem = slot.item
                slot.item = null
            }
            itemInSlot != null && handItem.canStackable(itemInSlot) -> {
                val toAdd = minOf(handItem.count, itemInSlot.leftToStack())
                itemInSlot.count += toAdd
                handItem.count -= toAdd
                if (handItem.count <= 0) inventoryModule.heldItem = null
            }
            itemInSlot == null -> {
                slot.item = handItem
                inventoryModule.heldItem = null
            }
            else -> {
                inventoryModule.heldItem = itemInSlot
                slot.item = handItem
            }
        }
    }

    private fun handleRightClick(window: AbstractWindow, cell: InventoryCell) {
        val inventoryModule = (window as? WGamePanel)?.moduleManager?.getModule<InventoryModule>("inventory") ?: return
        val slot = cell.slot ?: return
        val itemInSlot = slot.item
        val handItem = inventoryModule.heldItem

        if (handItem == null) {
            if (itemInSlot != null) {
                // Берем один предмет
                inventoryModule.heldItem = itemInSlot.copy().apply { count = 1 }
                itemInSlot.count--
                if (itemInSlot.count <= 0) slot.item = null
            }
        } else {
            if (itemInSlot == null) {
                // Кладем один предмет
                slot.item = handItem.copy().apply { count = 1 }
                handItem.count--
                if (handItem.count <= 0) inventoryModule.heldItem = null
            } else if (handItem.canStackable(itemInSlot)) {
                // Добавляем один предмет
                if (itemInSlot.leftToStack() > 0) {
                    itemInSlot.count++
                    handItem.count--
                    if (handItem.count <= 0) inventoryModule.heldItem = null
                }
            }
        }
    }

    override fun onDetach(window: AbstractWindow) {
        cells.forEach { window.windowElements.remove(it) }
        chestBg?.let { window.windowElements.remove(it) }
    }

    override fun draw(window: AbstractWindow, lg: LGraphics) {
        // Можно отрисовать фон
    }
}
