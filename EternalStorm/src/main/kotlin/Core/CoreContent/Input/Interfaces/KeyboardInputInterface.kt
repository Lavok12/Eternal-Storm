package la.vok.Core.CoreContent.Input.Interfaces

interface KeyboardInputInterface {
    fun keyboardStartFrame()

    fun keyPressed(key: Int)
    fun keyPressed(key: Int, keyChar: Char)

    fun keyReleased(key: Int)
    fun keyReleased(key: Int, keyChar: Char)

    fun keyRepeat(key: Int)
    fun keyRepeat(key: Int, keyChar: Char)

    fun keyDoublePress(key: Int)
    fun keyDoublePress(key: Int, keyChar: Char)

    fun keyTyped(keyChar: Char)

    fun keyUpdate(key: Int, heldTime: Float)
    fun keyUpdate(key: Int, keyChar: Char, heldTime: Float)

    fun keyCombo(keys: Set<Int>)

    fun keyboardUpdate()
}