package la.vok.Core.CoreContent.Windows.WindowsStorage.Templates


import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.LavokLibrary.Gradient.GradientInfo
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.Windows.Message.WindowMessage
import kotlin.math.floor

open class WStandartPanel(windowsManager: WindowsManager) : AbstractWindow(windowsManager) {

    open val bgTopColor: LColor = LColor(200f)
    open val bgBottomColor: LColor = LColor(200f)

    open var gradientMultiple = 0.5f
    override fun draw(mainRender: MainRender) {
        lg.setImage(
            GradientInfo(
                bgTopColor,
                bgBottomColor,
                LPoint(0, 0),
                LPoint(0, (logicalSize.y * gradientMultiple).toInt()),
                LPoint((logicalSize.x * gradientMultiple).toInt(), (logicalSize.y * gradientMultiple).toInt())
            ).generate(),
            Vec2(0f),
            logicalSize
        )
    }

    override fun postDraw(mainRender: MainRender) {

    }

    override fun handleMessage(windowMessage: WindowMessage) {}

    open fun calculateIconPos(position: Vec2, size: Vec2, delta: Vec2, freeZone: Float = 0f): Vec2 {
        val pos = position.copy()

        var sizeX = logicalSize.x - padding.x * 2f - size.x - freeZone
        sizeX /= delta.x
        sizeX = floor(sizeX)
        sizeX *= delta.x

        var guard = 0
        while (pos.x + freeZone / 2f >= sizeX / 2f) {
            pos.x -= sizeX + delta.x
            pos.y -= delta.y
            guard++
            if (guard > 500) break
        }

        return pos
    }
}
