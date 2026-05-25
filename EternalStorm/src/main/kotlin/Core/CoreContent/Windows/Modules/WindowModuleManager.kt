package la.vok.Core.CoreContent.Windows.Modules

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import processing.event.MouseEvent

class WindowModuleManager(val window: AbstractWindow) {
    private val modules = mutableListOf<IUiModule>()

    fun addModule(module: IUiModule) {
        if (modules.any { it.id == module.id }) return
        modules.add(module)
        module.onAttach(window)
    }

    fun removeModule(id: String) {
        val module = modules.find { it.id == id } ?: return
        module.onDetach(window)
        modules.remove(module)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : IUiModule> getModule(id: String): T? = modules.find { it.id == id } as? T

    fun update() {
        modules.forEach { if (it.isEnabled) it.update(window) }
    }

    fun physicUpdate() {
        modules.forEach { if (it.isEnabled) it.physicUpdate(window) }
    }

    // --- Делегирование событий мыши ---
    fun leftPressed(position: Vec2) = modules.forEach { if (it.isEnabled) it.leftPressed(window, position) }
    fun rightPressed(position: Vec2) = modules.forEach { if (it.isEnabled) it.rightPressed(window, position) }
    fun centerPressed(position: Vec2) = modules.forEach { if (it.isEnabled) it.centerPressed(window, position) }

    fun leftReleased(position: Vec2) = modules.forEach { if (it.isEnabled) it.leftReleased(window, position) }
    fun rightReleased(position: Vec2) = modules.forEach { if (it.isEnabled) it.rightReleased(window, position) }
    fun centerReleased(position: Vec2) = modules.forEach { if (it.isEnabled) it.centerReleased(window, position) }

    fun leftUpdate(position: Vec2, oldPosition: Vec2) = modules.forEach { if (it.isEnabled) it.leftUpdate(window, position, oldPosition) }
    fun rightUpdate(position: Vec2, oldPosition: Vec2) = modules.forEach { if (it.isEnabled) it.rightUpdate(window, position, oldPosition) }
    fun centerUpdate(position: Vec2, oldPosition: Vec2) = modules.forEach { if (it.isEnabled) it.centerUpdate(window, position, oldPosition) }

    fun leftDoubleClick(position: Vec2) = modules.forEach { if (it.isEnabled) it.leftDoubleClick(window, position) }
    fun rightDoubleClick(position: Vec2) = modules.forEach { if (it.isEnabled) it.rightDoubleClick(window, position) }
    fun centerDoubleClick(position: Vec2) = modules.forEach { if (it.isEnabled) it.centerDoubleClick(window, position) }

    fun leftDragStart(position: Vec2) = modules.forEach { if (it.isEnabled) it.leftDragStart(window, position) }
    fun rightDragStart(position: Vec2) = modules.forEach { if (it.isEnabled) it.rightDragStart(window, position) }
    fun centerDragStart(position: Vec2) = modules.forEach { if (it.isEnabled) it.centerDragStart(window, position) }

    fun leftDrag(position: Vec2, oldPosition: Vec2, start: Vec2) = modules.forEach { if (it.isEnabled) it.leftDrag(window, position, oldPosition, start) }
    fun rightDrag(position: Vec2, oldPosition: Vec2, start: Vec2) = modules.forEach { if (it.isEnabled) it.rightDrag(window, position, oldPosition, start) }
    fun centerDrag(position: Vec2, oldPosition: Vec2, start: Vec2) = modules.forEach { if (it.isEnabled) it.centerDrag(window, position, oldPosition, start) }

    fun leftDragEnd(position: Vec2) = modules.forEach { if (it.isEnabled) it.leftDragEnd(window, position) }
    fun rightDragEnd(position: Vec2) = modules.forEach { if (it.isEnabled) it.rightDragEnd(window, position) }
    fun centerDragEnd(position: Vec2) = modules.forEach { if (it.isEnabled) it.centerDragEnd(window, position) }

    fun mouseWheel(position: Vec2, event: MouseEvent) = modules.forEach { if (it.isEnabled) it.mouseWheel(window, position, event) }
    fun mouseUpdate(position: Vec2, oldPosition: Vec2) = modules.forEach { if (it.isEnabled) it.mouseUpdate(window, position, oldPosition) }
    fun mouseMove(position: Vec2, oldPosition: Vec2) = modules.forEach { if (it.isEnabled) it.mouseMove(window, position, oldPosition) }

    // --- Делегирование событий клавиатуры ---
    fun keyPressed(key: Int) = modules.forEach { if (it.isEnabled) it.keyPressed(window, key) }
    fun keyPressed(key: Int, keyChar: Char) = modules.forEach { if (it.isEnabled) it.keyPressed(window, key, keyChar) }
    fun keyReleased(key: Int) = modules.forEach { if (it.isEnabled) it.keyReleased(window, key) }
    fun keyReleased(key: Int, keyChar: Char) = modules.forEach { if (it.isEnabled) it.keyReleased(window, key, keyChar) }
    fun keyRepeat(key: Int) = modules.forEach { if (it.isEnabled) it.keyRepeat(window, key) }
    fun keyRepeat(key: Int, keyChar: Char) = modules.forEach { if (it.isEnabled) it.keyRepeat(window, key, keyChar) }
    fun keyDoublePress(key: Int) = modules.forEach { if (it.isEnabled) it.keyDoublePress(window, key) }
    fun keyDoublePress(key: Int, keyChar: Char) = modules.forEach { if (it.isEnabled) it.keyDoublePress(window, key, keyChar) }
    fun keyTyped(keyChar: Char) = modules.forEach { if (it.isEnabled) it.keyTyped(window, keyChar) }
    fun keyUpdate(key: Int, heldTime: Float) = modules.forEach { if (it.isEnabled) it.keyUpdate(window, key, heldTime) }
    fun keyUpdate(key: Int, keyChar: Char, heldTime: Float) = modules.forEach { if (it.isEnabled) it.keyUpdate(window, key, keyChar, heldTime) }
    fun keyCombo(keys: Set<Int>) = modules.forEach { if (it.isEnabled) it.keyCombo(window, keys) }

    fun preDraw(lg: LGraphics) {
        modules.forEach { if (it.isEnabled) it.preDraw(window, lg) }
    }

    fun draw(lg: LGraphics) {
        modules.forEach { if (it.isEnabled) it.draw(window, lg) }
    }

    fun postDraw(lg: LGraphics) {
        modules.forEach { if (it.isEnabled) it.postDraw(window, lg) }
    }
}
