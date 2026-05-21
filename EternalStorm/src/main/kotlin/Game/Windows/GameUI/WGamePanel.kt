package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Input.KeyCode
import la.vok.Core.CoreContent.Windows.Modules.WindowModuleManager
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameController.PlayerControl
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.Game.Windows.GameUI.Modules.CraftingModule
import la.vok.Game.Windows.GameUI.Modules.InventoryModule
import la.vok.Game.Windows.GameUI.Modules.StatsModule
import la.vok.State.AppState

class WGamePanel(windowsManager: WindowsManager, var gameController: GameController) : WStandartPanel(windowsManager) {
    val playerControl: PlayerControl get() = gameController.playerControl

    override var padding: Vec2 = 10 v 10
    override var tags: Array<String> = arrayOf("game")

    val moduleManager = WindowModuleManager(this)

    init {
        moduleManager.addModule(InventoryModule(playerControl))
        moduleManager.addModule(CraftingModule(playerControl, gameController.gameCycle))
        moduleManager.addModule(StatsModule(playerControl))
    }

    private val inventory get() = moduleManager.getModule<InventoryModule>("inventory")!!
    private val crafts get() = moduleManager.getModule<CraftingModule>("crafting")!!

// --- Рендер ---

    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        lg.bg(0f)
        gameController.render(lg)
        moduleManager.draw(lg)
    }

    override fun postDraw(mainRender: MainRender) {
        super.postDraw(mainRender)
        moduleManager.postDraw(lg)
    }

    // --- Обновление ---

    override fun update() {
        super.update()
        moduleManager.update()
    }

    // --- Утилиты ---

    fun insideUxElement(position: Vec2) = windowElements.any { it.isVisible && it.inside(position) }

    // --- Ввод ---

    override fun leftPressed(position: Vec2) {
        super.leftPressed(position)
        if (insideUxElement(position)) {
             val cell = inventory.allCells().map { it.first }.firstOrNull { it.inside(position) }
             if (cell != null) { inventory.startDrag(cell, position) }
             return
        }
        playerControl.leftPressed(position)
        playerControl.interact(position, BlockInteractionType.LEFT)
    }

    override fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        super.leftUpdate(position, oldPosition)
        inventory.updateDrag(position)
        if (inventory.draggedCell == null) playerControl.leftUpdate(position, oldPosition)
    }

    override fun leftReleased(position: Vec2) {
        super.leftReleased(position)
        if (inventory.draggedCell != null) {
            inventory.endDrag(position) { item ->
                val player = playerControl.getPlayerEntity() ?: return@endDrag
                val currentDim = player.dimension
                
                gameController.gameCycle.itemsApi.spawnItemEntity(
                    currentDim,
                    item.copy().apply { count = item.count },
                    player.position,
                    randomVelocity = true
                )

                // Sync hand item if dropped
                if (player.handItemComponent.currentHandItem?.item === item) {
                    player.handItemComponent.clearHandItem()
                }
            }
            return
        }
        playerControl.leftReleased(position)
    }

    override fun keyPressed(key: Int) {
        when (key) {
            KeyCode.TAB -> {
                inventory.toggleInventory()
                if (playerControl.isInventoryOpen) {
                    crafts.show(this)
                } else {
                    crafts.hide(this)
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
            KeyCode.F1 -> {
                if (gameController.coreController.windowsManager.containsTag("dev")) return
                gameController.wDevPanel = WDevPanel(coreController.windowsManager, gameController).apply {
                    windowSize = AppState.lg.frameSize
                }
                coreController.windowsManager.addWindow(gameController.wDevPanel!!)
            }
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
        playerControl.interact(position, BlockInteractionType.RIGHT)
    }

    override fun centerPressed(position: Vec2) {
        super.centerPressed(position)
        if (insideUxElement(position)) return
        playerControl.interact(position, BlockInteractionType.MIDDLE)
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
