package la.vok.Sketch

import la.vok.InitProject.BaseInit
import la.vok.State.AppState
import processing.core.PApplet
import processing.event.MouseEvent

class ProcessingSketch : PApplet() {

    override fun settings() {
        System.setProperty("sun.java2d.uiScale", "1.0")
        fullScreen(P2D)
        BaseInit.initSettings(this)
    }

    override fun setup() {
        BaseInit.setCorrectWindowSize(this)
        AppState.coreController.mainRender.lg = BaseInit.initLG(width, height)
        BaseInit.startSettings(this)
        AppState.coreController.start()
    }

    override fun draw() {
        if (frameCount == 1) {
            BaseInit.firstFrame(this)
        }

        AppState.coreController.mainRender.lg.checkResolution()

        AppState.coreController.logicalTick()
    }
    override fun mousePressed() {
        val mi = AppState.coreController.mouseInput
        when (mouseButton) {
            LEFT   -> mi.leftPressed()
            RIGHT  -> mi.rightPressed()
            CENTER -> mi.centerPressed()
        }
    }

    override fun mouseReleased() {
        val mi = AppState.coreController.mouseInput
        when (mouseButton) {
            LEFT   -> mi.leftReleased()
            RIGHT  -> mi.rightReleased()
            CENTER -> mi.centerReleased()
        }
    }

    override fun mouseWheel(event: MouseEvent) {
        AppState.coreController.mouseInput.mouseWheel(event)
    }

    override fun keyPressed() {
        val ki = AppState.coreController.keyboardInput
        ki.keyPressed(keyCode, key)
    }

    override fun keyReleased() {
        val ki = AppState.coreController.keyboardInput
        ki.keyReleased(keyCode, key)
    }

    override fun keyTyped() {
        val ki = AppState.coreController.keyboardInput
        ki.keyTyped(key)
    }
}