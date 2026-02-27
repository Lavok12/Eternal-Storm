package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Camera.StandartCamera
import la.vok.Core.CoreContent.Input.KeyCode
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.EntitiesList
import la.vok.Game.GameController.PlayerControl
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class WGamePanel(windowsManager: WindowsManager, var gameController: GameController) : WStandartPanel(windowsManager) {
    val playerControl: PlayerControl get() = gameController.playerControl

    override var tags: Array<String>
        get() = arrayOf("game")
        set(value) {}
    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        lg.bg(0f)
        gameController.renderTick(lg)
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
}