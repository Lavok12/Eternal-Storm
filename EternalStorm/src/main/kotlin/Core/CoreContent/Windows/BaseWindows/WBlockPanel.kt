package la.vok.Core.CoreContent.Windows.WindowsStorage.Templates

import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.FrameLimiter
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.min

open class WBlockPanel(
    windowsManager: WindowsManager,
    var dim: Float
) : WStandartPanel(windowsManager) {

    // --------------------------------------------------------------------
    // CONFIG
    // --------------------------------------------------------------------

    override var padding = Vec2(10f)


    override fun start() {
        super.start()
        windowsManager.blockBy(this)
    }

    override fun preUpdate() {
        super.preUpdate()
        dim = min(dim + 320f * FrameLimiter.logicDeltaTime, 140f)
    }

    override fun displayPreDraw(lg: LGraphics) {
        super.displayPreDraw(lg)
        lg.fill(0f, dim)
        lg.setBlock(Vec2.Companion.ZERO, lg.frameSize)
    }
}