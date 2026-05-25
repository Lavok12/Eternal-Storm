package la.vok.Core.CoreControllers

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.Parts.Tooltip
import la.vok.LavokLibrary.KotlinPlus.toVec2X
import la.vok.LavokLibrary.KotlinPlus.toVec2Y
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class TooltipController(var coreController: CoreController) : Controller {

    init {
        create()
    }

    private var bufferedTooltip: Tooltip? = null
    private var bufferedPos: Vec2 = Vec2(0f, 0f)

    fun push(tooltip: Tooltip, pos: Vec2) {
        AppState.logger.debug("[TooltipController] push '${tooltip.text}' at $pos")
        bufferedTooltip = tooltip
        bufferedPos = pos
    }

    fun clearBuffer() {
        AppState.logger.trace("[TooltipController] clearBuffer()")
        bufferedTooltip = null
    }

    fun renderBuffered(lg: LGraphics = coreController.mainRender.lg) {
        val tooltip = bufferedTooltip ?: return

        var text = tooltip.text
        if (tooltip.contentKey != "") {
            text = AppState.getLanguageValue(tooltip.contentKey)
        }

        val size = lg.getTextLogicalSize(text, tooltip.textSize)
        
        // Базовое позиционирование (копируем логику для boxPos)
        var boxDelta = 0f v 0f
        var boxAlign = 0f v 0f
        if (bufferedPos.x > 0f) {
            boxDelta.x -= 20f
            boxAlign = -1f v -1f
        } else {
            boxDelta.x += 20f
            boxAlign = 1f v -1f
        }
        boxDelta.y = -5f

        val boxPos = bufferedPos + boxDelta + size * boxAlign.halved()
        val boxSize = size + Vec2(25f, 8f)

        // Теперь контроллер не рисует стандартный бокс и текст.
        // Отрисовка делегирована полностью в extraRender.
        tooltip.extraRender(lg, boxPos, boxSize)
    }
}
