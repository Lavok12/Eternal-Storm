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

    private var cachedBg: processing.core.PImage? = null
    private var lastBgRes = LPoint(-1, -1)
    private var lastTopCol: LColor? = null
    private var lastBottomCol: LColor? = null

    override fun draw(mainRender: MainRender) {
        val resX = (logicalSize.x * gradientMultiple).toInt()
        val resY = (logicalSize.y * gradientMultiple).toInt()

        if (cachedBg == null || 
            resX != lastBgRes.x || resY != lastBgRes.y || 
            bgTopColor != lastTopCol || bgBottomColor != lastBottomCol) {
            
            cachedBg = GradientInfo(
                bgTopColor,
                bgBottomColor,
                LPoint(0, 0),
                LPoint(0, resY),
                LPoint(resX, resY)
            ).generate()
            
            lastBgRes = LPoint(resX, resY)
            lastTopCol = bgTopColor.copy()
            lastBottomCol = bgBottomColor.copy()
        }

        lg.setImage(
            cachedBg!!,
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
