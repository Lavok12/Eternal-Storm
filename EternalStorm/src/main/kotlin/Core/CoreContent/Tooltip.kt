package la.vok.Core.CoreControllers.Parts

import la.vok.Core.CoreControllers.TooltipController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

data class Tooltip(
    var textSize: Float = 15f,
    var tooltipController: TooltipController = AppState.coreController.tooltipController,
    var text: String = "",
    var contentKey: String = "",

    var extraRender: (lg: LGraphics, pos: Vec2, size: Vec2) -> Unit = { _, _, _ -> }
) {
    fun render(pos: Vec2) {
        tooltipController.push(this, pos)
    }
}