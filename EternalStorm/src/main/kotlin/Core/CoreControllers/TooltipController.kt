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
        AppState.logger.trace("[TooltipController] renderBuffered()")

        val tooltip = bufferedTooltip ?: run {
            AppState.logger.trace("[TooltipController] no tooltip buffered")
            return
        }

        var boxDelta = 0f v 0f
        var textDelta = 0f v 0f
        var boxAlign = 0f v 0f

        var text = tooltip.text
        if (tooltip.contentKey != "") {
            AppState.logger.trace("[TooltipController] resolving contentKey '${tooltip.contentKey}'")
            text = AppState.getLanguageValue(tooltip.contentKey)
        }

        val size = lg.getTextLogicalSize(text, tooltip.textSize)

        if (bufferedPos.x > 0f) {
            lg.setTextAlign(-1, -1)
            boxDelta.x -= 20f
            boxAlign = -1f v -1f
            textDelta.x = -size.x
        } else {
            lg.setTextAlign(-1, -1)
            boxDelta.x += 20f
            boxAlign = 1f v -1f
        }

        boxDelta.y = -5f

        val boxPos = bufferedPos + boxDelta + size * boxAlign.halved()
        val boxSize = size + Vec2(25f, 8f)

        lg.stroke(LColor(30f, 30f, 30f))
        lg.strokeWeight(3f)
        lg.fill(LColor(180f, 185f, 190f))
        lg.setBlock(boxPos, boxSize)
        lg.noStroke()

        lg.fill(255f)
        lg.setText(
            text,
            bufferedPos + boxDelta + textDelta
                    - tooltip.textSize.toVec2X().halved()
                    + tooltip.textSize.toVec2Y() * 0.20f,
            tooltip.textSize
        )

        tooltip.extraRender(lg, boxPos, boxSize)

        AppState.logger.trace("[TooltipController] renderBuffered() done")
    }
}
