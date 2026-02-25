package la.vok.Core.CoreContent.Input

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.State.AppState

class KeyboardInput(val coreController: CoreController) : Controller {
    init {
        create()
    }
    override fun logicalTick() {
        super.superTick()

        startFrame()
        update()
    }

    private val keyDown = HashSet<Int>()
    private val keyPressedFrame = HashSet<Int>()
    private val keyReleasedFrame = HashSet<Int>()

    private val keyHeldStart = HashMap<Int, Long>()
    private val keyLastPressTime = HashMap<Int, Long>()

    // -------------------------------------------------------------------------
    // FRAME METHODS
    // -------------------------------------------------------------------------
    fun startFrame() {
        coreController.keyboardProcessing.keyboardStartFrame()
        keyPressedFrame.clear()
        keyReleasedFrame.clear()
    }

    fun update() {
        val now = System.currentTimeMillis()

        for (k in keyDown) {
            val heldTime = ((now - (keyHeldStart[k] ?: now)).toFloat()) / 1000f
            coreController.keyboardProcessing.keyUpdate(k, heldTime)
            coreController.keyboardProcessing.keyUpdate(k, 0.toChar(), heldTime)
        }

        if (keyDown.isNotEmpty())
            coreController.keyboardProcessing.keyCombo(keyDown)

        coreController.keyboardProcessing.keyboardUpdate()
    }

    // -------------------------------------------------------------------------
    // KEY EVENTS
    // -------------------------------------------------------------------------
    fun keyPressed(rawKey: Int, rawChar: Char) {
        if (!keyDown.contains(rawKey)) {
            val now = System.currentTimeMillis()
            keyDown += rawKey
            keyPressedFrame += rawKey

            if (now - (keyLastPressTime[rawKey] ?: 0L) <= AppState.doublePressDelay) {
                coreController.keyboardProcessing.keyDoublePress(rawKey)
                coreController.keyboardProcessing.keyDoublePress(rawKey, rawChar)
            }
            keyLastPressTime[rawKey] = now
            keyHeldStart[rawKey] = now

            coreController.keyboardProcessing.keyPressed(rawKey)
            coreController.keyboardProcessing.keyPressed(rawKey, rawChar)
        }
    }

    fun keyReleased(rawKey: Int, rawChar: Char) {
        if (keyDown.contains(rawKey)) {
            keyDown -= rawKey
            keyReleasedFrame += rawKey

            coreController.keyboardProcessing.keyReleased(rawKey)
            coreController.keyboardProcessing.keyReleased(rawKey, rawChar)
        }
    }

    fun keyTyped(rawChar: Char) {
        coreController.keyboardProcessing.keyTyped(rawChar)
    }

    // -------------------------------------------------------------------------
    // UNIVERSAL KEY CHECK
    // -------------------------------------------------------------------------
    fun isKeyPressed(key: Int): Boolean = keyDown.contains(key)
    fun isKeyReleased(key: Int): Boolean = !keyDown.contains(key)
    fun wasKeyPressedThisFrame(key: Int): Boolean = keyPressedFrame.contains(key)
    fun wasKeyReleasedThisFrame(key: Int): Boolean = keyReleasedFrame.contains(key)

    // -------------------------------------------------------------------------
    // MODIFIERS
    // -------------------------------------------------------------------------
    fun isShiftPressed(): Boolean {
        // Left Shift = 16, Right Shift = 16 (could use separate constants if needed)
        return keyDown.contains(16)
    }

    fun isCtrlPressed(): Boolean {
        // Left Ctrl = 17, Right Ctrl = 17
        return keyDown.contains(17)
    }

    fun isAltPressed(): Boolean {
        // Alt = 18
        return keyDown.contains(18)
    }

    // Можно универсальный метод для модификаторов
    fun isModifierPressed(modifierKey: Int): Boolean = keyDown.contains(modifierKey)
}
