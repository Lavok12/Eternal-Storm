package la.vok.Core.CoreContent.Windows.Modules

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.LavokLibrary.LGraphics.LGraphics

interface IUiModule {
    val id: String
    var isEnabled: Boolean

    fun onAttach(window: AbstractWindow) {}
    fun onDetach(window: AbstractWindow) {}

    fun update(window: AbstractWindow) {}
    fun preDraw(window: AbstractWindow, lg: LGraphics) {}
    fun draw(window: AbstractWindow, lg: LGraphics) {}
    fun postDraw(window: AbstractWindow, lg: LGraphics) {}
}
