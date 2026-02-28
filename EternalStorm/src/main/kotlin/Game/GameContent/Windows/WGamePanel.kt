package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Camera.StandartCamera
import la.vok.Core.CoreContent.Input.KeyCode
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.EntitiesList
import la.vok.Game.GameContent.Windows.InventoryCell
import la.vok.Game.GameController.PlayerControl
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class WGamePanel(windowsManager: WindowsManager, var gameController: GameController) : WStandartPanel(windowsManager) {
    val playerControl: PlayerControl get() = gameController.playerControl

    override var padding: Vec2
        get() = 10 v 10
        set(value) {}

    override var tags: Array<String>
        get() = arrayOf("game")
        set(value) {}
    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        lg.bg(0f)
        gameController.renderTick(lg)
    }

    fun buildInventoryButtons() {
        AppState.logger.info("WGamePanel rebuild buttons")
        val player = gameController.gameCycle.entityApi.getById(playerControl.playerId)
        if (player == null) return

        for (i in 0..9) {
            windowElements += InventoryCell(
                this,
                (i-4.5f) * 50 v 0f,
                45 v 45,
                0 v -1,
                slot = player.inventory?.itemContainer?.getSlot(i),
                leftClick = {
                    playerControl.chooseSlot(i)
                }
            )
        }
    }

    override fun start() {
        super.start()
    }

    override fun physicUpdate() {
        super.physicUpdate()
    }

    fun insideUxElement(position: Vec2) : Boolean {
        for (i in windowElements) {
            if (i.inside(position)) return true
        }
        return false
    }

    override fun keyPressed(key: Int) {
        when(key) {
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
        when(key) {
            KeyCode.A -> {playerControl.tapA()}
            KeyCode.D -> {playerControl.tapD()}
            KeyCode.SPACE -> {
                playerControl.tapSpace()
            }
        }
    }

    override fun leftPressed(position: Vec2) {
        super.leftPressed(position)
        if (insideUxElement(position)) return
        playerControl.leftPressed(position)
    }

    override fun rightPressed(position: Vec2) {
        super.rightPressed(position)
        if (insideUxElement(position)) return
        playerControl.rightPressed(position)
    }

    override fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        super.leftUpdate(position, oldPosition)
        playerControl.leftUpdate(position, oldPosition)
    }

    override fun rightUpdate(position: Vec2, oldPosition: Vec2) {
        super.rightUpdate(position, oldPosition)
        playerControl.rightUpdate(position, oldPosition)
    }

    override fun leftReleased(position: Vec2) {
        super.leftReleased(position)
        playerControl.leftReleased(position)
    }

    override fun rightReleased(position: Vec2) {
        super.leftReleased(position)
        playerControl.rightReleased(position)
    }
}