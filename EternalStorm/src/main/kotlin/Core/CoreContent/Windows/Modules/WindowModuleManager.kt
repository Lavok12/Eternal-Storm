package la.vok.Core.CoreContent.Windows.Modules

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.LavokLibrary.LGraphics.LGraphics

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
