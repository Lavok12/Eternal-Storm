package la.vok.InitProject

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.FrameLimiter
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.Sketch.ProcessingSketch
import la.vok.State.AppState
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import la.vok.Menu.MenuController.MenuController
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.Toolkit

object BaseInit {
    private val logger = ConsoleLogger("Core/CoreContent/Init", LogLevel.DEBUG)

    fun initLG(w: Int, h: Int) : LGraphics {
        logger.info("Initializing LGraphics: ${w}x${h}")
        return LGraphics(w, h)
    }

    fun initSettings(ps: ProcessingSketch) {
        logger.info("Initializing Application State and CoreController")
        AppState.main = ps
        AppState.coreController = CoreController()
    }

    fun startSettings(ps: ProcessingSketch) {
        logger.debug("Applying sketch settings: FPS=${FrameLimiter.targetUpdateFPS}, PixelDensity=1, NoSmooth")
        ps.frameRate(FrameLimiter.targetUpdateFPS)
        ps.pixelDensity(1)
        ps.noSmooth()
    }

    fun firstFrame(ps: ProcessingSketch) {
        logger.trace("First frame execution")

        AppState.coreController.setScene(MenuController(AppState.coreController))
    }

    fun setCorrectWindowSize(ps: ProcessingSketch) {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val gc = gd.defaultConfiguration

        val scaleX = gc.defaultTransform.scaleX
        val scaleY = gc.defaultTransform.scaleY

        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        val screenW = screenSize.width * scaleX
        val screenH = screenSize.height * scaleY

        logger.info("Screen detected: Actual Resolution=${screenSize.width}x${screenSize.height}, Scale=[$scaleX, $scaleY]")
        logger.info("Setting surface size to: ${screenW.toInt()}x${screenH.toInt()}")

        ps.surface.setSize(screenW.toInt(), screenH.toInt())
    }
}