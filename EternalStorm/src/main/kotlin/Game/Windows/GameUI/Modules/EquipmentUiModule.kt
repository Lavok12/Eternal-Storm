package la.vok.Game.Windows.GameUI.Modules

import la.vok.Core.CoreContent.Windows.Modules.IUiModule
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.Windows.InventoryCell
import la.vok.Game.Windows.InventoryCellType
import la.vok.Game.GameController.PlayerControl
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Game.GameSystems.WorldSystems.Equipment.EquipmentSlot
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.LavokLibrary.Gradient.GradientInfo
import la.vok.LavokLibrary.Gradient.ShadowFrameInfo
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LLibs.AnimationType

class EquipmentUiModule(
    val playerControl: PlayerControl
) : IUiModule {
    override val id: String = "equipment"
    override var isEnabled: Boolean = true

    private val cellSize = 48.0 v 48.0
    private val cellSpacing = 50f
    private val margin = 15f

    private val cells = ArrayList<InventoryCell>()
    private var equipmentBg: WindowElement? = null
    private var isInitialized = false
    var animProgress = 0f

    // Конфигурация: Слот -> (Сетка X, Сетка Y, Множитель размера)
    private val slotConfigs = listOf(
        Triple(EquipmentSlot.HEAD, 0 v -0.5f, 1.0f),
        Triple(EquipmentSlot.NECK, 0 v 0.7f, 1.0f),
        Triple(EquipmentSlot.CHEST, 0 v 2.0f, 1.25f), // Больше
        Triple(EquipmentSlot.LEFT_HAND, -1.3f v 2.0f, 1.0f), // По бокам от Chest
        Triple(EquipmentSlot.RIGHT_HAND, 1.3f v 2.0f, 1.0f), // По бокам от Chest
        Triple(EquipmentSlot.PANTS, 0 v 3.3f, 1.2f), // Больше
        Triple(EquipmentSlot.BOOTS, 0 v 4.6f, 1.0f) // Под Pants
    )

    private fun getClosedPos(window: AbstractWindow): Vec2 {
        return -250f v (window.logicalSize.y / 2f + 200f)
    }

    private fun getOpenPos(window: AbstractWindow, gridPos: Vec2): Vec2 {
        val invBgRightX = -window.logicalSize.x / 2f + margin + (10 * cellSpacing) + margin
        val startX = invBgRightX + 180f
        val startY = window.logicalSize.y / 2f - margin - 70f
        return (startX + gridPos.x * cellSpacing) v (startY - gridPos.y * cellSpacing)
    }

    override fun onAttach(window: AbstractWindow) {}

    private fun initialize(window: AbstractWindow) {
        val player = playerControl.getPlayerEntity() ?: return
        val equipment = player.equipmentModule

        cells.clear()
        
        // --- Добавление фона ---
        val bgWidth = 4.2f * cellSpacing
        val bgHeight = 6.8f * cellSpacing
        
        equipmentBg = WindowElement(
            window, Vec2.ZERO, bgWidth v bgHeight,
            render = { lg ->
                val alpha = animProgress * 255f
                val pos = positionWithCache
                
                // Темный градиент (темнее чем в InventoryModule)
                val bgTop = LColor(40f, 50f, 65f, 140f)
                val bgBot = LColor(5f, 8f, 12f, 100f)
                
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
                        intensity = 0.4f,
                        spread = 25
                    ).generate(),
                    pos, size
                )
                lg.noTint()
            }
        ).apply { isVisible = false }
        window.windowElements.add(equipmentBg!!)

        slotConfigs.forEach { (slotId, gridPos, sizeMult) ->
            val cell = InventoryCell(
                window, getClosedPos(window), cellSize * sizeMult, Vec2.ZERO,
                slot = equipment.getSlot(slotId),
                cellType = InventoryCellType.INVENTORY,
                leftClick = { handleLeftClick(slotId) }
            )
            cells.add(cell)
            window.windowElements.add(cell)
        }
        isInitialized = true
    }

    private fun handleLeftClick(slotId: String) {
        val player = playerControl.getPlayerEntity() ?: return
        val heldItem = playerControl.heldItem
        val slot = player.equipmentModule.getSlot(slotId) ?: return
        val equippedItem = slot.item

        when {
            heldItem == null -> {
                playerControl.heldItem = equippedItem
                slot.item = null
            }
            equippedItem == null -> {
                if (heldItem.count > 0) {
                   slot.item = heldItem
                   playerControl.heldItem = null
                }
            }
            else -> {
                playerControl.heldItem = equippedItem
                slot.item = heldItem
            }
        }
    }

    private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t
    private fun lerpVec(a: Vec2, b: Vec2, t: Float) = lerp(a.x, b.x, t) v lerp(a.y, b.y, t)

    override fun update(window: AbstractWindow) {
        if (!isInitialized) {
            initialize(window)
            if (!isInitialized) return
        }

        val inventoryModule = (window as? WGamePanel)?.moduleManager?.getModule<InventoryModule>("inventory") ?: return
        animProgress = inventoryModule.animProgress
        
        cells.forEachIndexed { i, cell ->
            val gridPos = slotConfigs[i].second
            val openPos = getOpenPos(window, gridPos)
            val closedPos = getClosedPos(window)
            
            cell.position = lerpVec(closedPos, openPos, animProgress)
            cell.isVisible = animProgress > 0f
            cell.markDirty()
        }

        // Обновляем позицию фона
        equipmentBg?.let { bg ->
            val openBgPos = getOpenPos(window, 0 v 2.05f) // Центр сетки слотов
            val closedBgPos = getClosedPos(window)
            bg.position = lerpVec(closedBgPos, openBgPos, animProgress)
            bg.isVisible = animProgress > 0f
            bg.markDirty()
        }
    }

    override fun draw(window: AbstractWindow, lg: LGraphics) {}
}
