package la.vok.Core.CoreControllers

import la.vok.Core.CoreContent.Input.Interfaces.KeyboardInputInterface
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import la.vok.Core.CoreControllers.Intergaces.Controller

class KeyboardProcessing(val coreController: CoreController) : KeyboardInputInterface, Controller {

    private val logger = ConsoleLogger("KeyboardProcessing", LogLevel.DEBUG)

    init {
        create()
    }

    override fun keyboardStartFrame() {
        coreController.windowsManager.call { keyboardStartFrame() }
    }

    override fun keyPressed(key: Int) {
        logger.debug("Key Pressed: $key")
        coreController.windowsManager.call { keyPressed(key) }
    }

    override fun keyPressed(key: Int, keyChar: Char) {
        logger.debug("Key Pressed: $key ('$keyChar')")
        coreController.windowsManager.call { keyPressed(key, keyChar) }
    }

    override fun keyReleased(key: Int) {
        logger.debug("Key Released: $key")
        coreController.windowsManager.call { keyReleased(key) }
    }

    override fun keyReleased(key: Int, keyChar: Char) {
        logger.debug("Key Released: $key ('$keyChar')")
        coreController.windowsManager.call { keyReleased(key, keyChar) }
    }

    override fun keyRepeat(key: Int) {
        coreController.windowsManager.call { keyRepeat(key) }
    }

    override fun keyRepeat(key: Int, keyChar: Char) {
        coreController.windowsManager.call { keyRepeat(key, keyChar) }
    }

    override fun keyDoublePress(key: Int) {
        logger.debug("Key Double Press: $key")
        coreController.windowsManager.call { keyDoublePress(key) }
    }

    override fun keyDoublePress(key: Int, keyChar: Char) {
        logger.debug("Key Double Press: $key ('$keyChar')")
        coreController.windowsManager.call { keyDoublePress(key, keyChar) }
    }

    override fun keyTyped(keyChar: Char) {
        coreController.windowsManager.call { keyTyped(keyChar) }
    }

    override fun keyUpdate(key: Int, heldTime: Float) {
        coreController.windowsManager.call { keyUpdate(key, heldTime) }
    }

    override fun keyUpdate(key: Int, keyChar: Char, heldTime: Float) {
        coreController.windowsManager.call { keyUpdate(key, keyChar, heldTime) }
    }

    override fun keyCombo(keys: Set<Int>) {
        coreController.windowsManager.call { keyCombo(keys) }
    }

    override fun keyboardUpdate() {
        coreController.windowsManager.call { keyboardUpdate() }
    }
}