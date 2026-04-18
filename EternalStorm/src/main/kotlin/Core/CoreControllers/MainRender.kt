package la.vok.Core.CoreControllers

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.FrameLimiter
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState
import processing.core.PApplet

class MainRender(var coreController: CoreController) : Controller {

    init {
        create()
    }

    lateinit var lg: LGraphics
    
    private var lastUsedMem = 0L
    private var allocAcc = 0L
    private var lastStatTime = 0L
    private var allocRate = 0f

    override fun renderTick() {
        AppState.logger.trace("[Render] tick() begin")

        startFrame()
        renderWindows()
        renderTooltip()

        AppState.logger.trace("[Render] tick() end")
    }

    inline fun withDraw(block: LGraphics.() -> Unit) {
        AppState.logger.trace("[Render] withDraw begin")
        lg.beginDraw()
        lg.block()
        lg.endDraw()
        AppState.logger.trace("[Render] withDraw end")
    }

    fun startFrame() {
        AppState.logger.trace("[Render] startFrame()")

        withDraw {
            pg.clear()
            pg.noStroke()
            bg(50f)
        }
    }

    fun renderWindows() {
        AppState.logger.trace("[Render] renderWindows() begin")

        coreController.windowsManager.renderWindows(this)

        withDraw {
            coreController.windowsManager.useWindows(lg)
        }

        withDraw {
            coreController.windowsManager.call { displayPreDraw(coreController.mainRender.lg) }
        }

        withDraw {
            coreController.windowsManager.useBlockedByWindow(lg)
        }

        withDraw {
            coreController.windowsManager.call { displayPostDraw(coreController.mainRender.lg) }
        }

        AppState.logger.trace("[Render] renderWindows() end")
    }

    fun useLG() {
        AppState.logger.trace("[Render] useLG()")

        AppState.main.background(0)
        AppState.main.image(
            lg.pg,
            0f,
            0f,
            AppState.main.width.toFloat(),
            AppState.main.height.toFloat()
        )

        AppState.main.textSize(20f)
        AppState.main.fill(255f)
        AppState.main.textAlign(PApplet.LEFT, PApplet.TOP)
        AppState.main.text("${FrameLimiter.currentUpdateFPS}", 10f, 30f)
        AppState.main.text("${FrameLimiter.currentRenderFPS}", 10f, 50f)
        AppState.main.text("${FrameLimiter.currentPhysicsFPS}", 10f, 70f)

        val rt = Runtime.getRuntime()
        val used = rt.totalMemory() - rt.freeMemory()
        val usedMB = used / (1024 * 1024)
        val maxMB = rt.maxMemory() / (1024 * 1024)

        val now = System.currentTimeMillis()
        if (used > lastUsedMem) {
            allocAcc += (used - lastUsedMem)
        }
        if (now - lastStatTime >= 1000) {
            allocRate = allocAcc.toFloat() / (1024f * 1024f)
            allocAcc = 0
            lastStatTime = now
        }
        lastUsedMem = used

        AppState.main.text("Memory: $usedMB MB / $maxMB MB (${"%.1f".format(allocRate)} MB/s)", 10f, 10f)

    }

    fun renderTooltip() {
        AppState.logger.trace("[Render] renderTooltip()")

        lg.beginDraw()
        coreController.tooltipController.renderBuffered(lg)
        coreController.tooltipController.clearBuffer()
        lg.endDraw()
    }
}
