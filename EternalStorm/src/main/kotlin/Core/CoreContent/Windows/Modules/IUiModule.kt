package la.vok.Core.CoreContent.Windows.Modules

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import processing.event.MouseEvent

interface IUiModule {
    val id: String
    var isEnabled: Boolean

    fun onAttach(window: AbstractWindow) {}
    fun onDetach(window: AbstractWindow) {}

    fun update(window: AbstractWindow) {}
    fun physicUpdate(window: AbstractWindow) {}

    // --- События мыши ---
    fun leftPressed(window: AbstractWindow, position: Vec2) {}
    fun rightPressed(window: AbstractWindow, position: Vec2) {}
    fun centerPressed(window: AbstractWindow, position: Vec2) {}

    fun leftReleased(window: AbstractWindow, position: Vec2) {}
    fun rightReleased(window: AbstractWindow, position: Vec2) {}
    fun centerReleased(window: AbstractWindow, position: Vec2) {}

    fun leftUpdate(window: AbstractWindow, position: Vec2, oldPosition: Vec2) {}
    fun rightUpdate(window: AbstractWindow, position: Vec2, oldPosition: Vec2) {}
    fun centerUpdate(window: AbstractWindow, position: Vec2, oldPosition: Vec2) {}

    fun leftDoubleClick(window: AbstractWindow, position: Vec2) {}
    fun rightDoubleClick(window: AbstractWindow, position: Vec2) {}
    fun centerDoubleClick(window: AbstractWindow, position: Vec2) {}

    fun leftDragStart(window: AbstractWindow, position: Vec2) {}
    fun rightDragStart(window: AbstractWindow, position: Vec2) {}
    fun centerDragStart(window: AbstractWindow, position: Vec2) {}

    fun leftDrag(window: AbstractWindow, position: Vec2, oldPosition: Vec2, start: Vec2) {}
    fun rightDrag(window: AbstractWindow, position: Vec2, oldPosition: Vec2, start: Vec2) {}
    fun centerDrag(window: AbstractWindow, position: Vec2, oldPosition: Vec2, start: Vec2) {}

    fun leftDragEnd(window: AbstractWindow, position: Vec2) {}
    fun rightDragEnd(window: AbstractWindow, position: Vec2) {}
    fun centerDragEnd(window: AbstractWindow, position: Vec2) {}

    fun mouseWheel(window: AbstractWindow, position: Vec2, event: MouseEvent) {}
    fun mouseUpdate(window: AbstractWindow, position: Vec2, oldPosition: Vec2) {}
    fun mouseMove(window: AbstractWindow, position: Vec2, oldPosition: Vec2) {}

    // --- События клавиатуры ---
    fun keyPressed(window: AbstractWindow, key: Int) {}
    fun keyPressed(window: AbstractWindow, key: Int, keyChar: Char) {}
    fun keyReleased(window: AbstractWindow, key: Int) {}
    fun keyReleased(window: AbstractWindow, key: Int, keyChar: Char) {}
    fun keyRepeat(window: AbstractWindow, key: Int) {}
    fun keyRepeat(window: AbstractWindow, key: Int, keyChar: Char) {}
    fun keyDoublePress(window: AbstractWindow, key: Int) {}
    fun keyDoublePress(window: AbstractWindow, key: Int, keyChar: Char) {}
    fun keyTyped(window: AbstractWindow, keyChar: Char) {}
    fun keyUpdate(window: AbstractWindow, key: Int, heldTime: Float) {}
    fun keyUpdate(window: AbstractWindow, key: Int, keyChar: Char, heldTime: Float) {}
    fun keyCombo(window: AbstractWindow, keys: Set<Int>) {}

    fun preDraw(window: AbstractWindow, lg: LGraphics) {}
    fun draw(window: AbstractWindow, lg: LGraphics) {}
    fun postDraw(window: AbstractWindow, lg: LGraphics) {}
}
