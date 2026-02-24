package la.vok.Menu.MenuContent

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.FrameLimiter
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState
import java.text.FieldPosition
import kotlin.math.cos
import kotlin.math.sin

class MenuCommet(
    var position: Vec2,
    var size: Float,
    var color: LColor,
    var angle: Float = 0f,
    var alpha: Float = 1f  // прозрачность 0..1
) {

    fun tick() {
        // движение с учетом угла
        var delta = Vec2(sin(angle + 1.2f), cos(angle + 1.2f))

        position = position - delta * 6.0f

        // постепенное уменьшение размера и прозрачности
        size *= 0.98f
        alpha *= 0.98f
    }

    fun render(lg: LGraphics) {
        val img = AppState.coreController.spriteLoader.getValue("menu_comet.png")
        val wh = img.height.toFloat() / img.width.toFloat()
        lg.setTint(color)
        lg.setRotateImage(img, position, Vec2(size, size * wh), angle)
        lg.noTint()
    }

    fun isDead() = alpha < 0.05f || size < 0.05f
}
