package la.vok.Core.CoreContent.Windows.WindowsStorage.Templates

import la.vok.Core.CoreContent.Windows.WindowLifecycle
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.CoreContent.Input.Interfaces.KeyboardInputInterface
import la.vok.Core.CoreContent.Input.Interfaces.MouseInputInterface
import la.vok.Core.FrameLimiter
import la.vok.LavokLibrary.Geometry.FrameRect
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Utils.Functions
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import la.vok.Windows.Message.WindowMessage
import processing.event.MouseEvent

open class AbstractWindow(
    var windowsManager: WindowsManager
) : MouseInputInterface, KeyboardInputInterface, WindowLifecycle, FrameRect {
    init {
        create()
    }
    fun create() {
        AppState.logger.info("Window created [${this.javaClass.simpleName}]")
    }

    // -------------------------------------------------------------------------
    // ✦ БАЗОВАЯ ИНФОРМАЦИЯ О ОКНЕ
    // -------------------------------------------------------------------------

    open var noCursor = false
    open var lgMultiple = 1f

    open var tags = arrayOf("base")
    var id: Long = 0
    var mouseInside = false

    val coreController: CoreController
        get() = windowsManager.coreController

    // -------------------------------------------------------------------------
    // ✦ ГЕОМЕТРИЯ И ЛОГИЧЕСКИЕ РАЗМЕРЫ
    // -------------------------------------------------------------------------

    open var windowPosition: Vec2 = Vec2(0f)

    open var windowSize: Vec2 = Vec2(100f)
        set(value) {
            logicalSize = value * logicalMultiple
            field = value
        }

    open var minWindowSize: Vec2 = Vec2(100f)

    var logicalMultiple: Vec2 = Vec2(1f)
        set(value) {
            logicalSize = windowSize * value
            field = value
        }

    var logicalSize: Vec2 = windowSize * logicalMultiple
    open var padding: Vec2 = Vec2(0f)

    // -------------------------------------------------------------------------
    // ✦ ГРАФИКА
    // -------------------------------------------------------------------------

    lateinit var lg: LGraphics

    open fun initLG() {
        lg = LGraphics(logicalSize.x.toInt(), logicalSize.y.toInt(), lgMultiple)
    }

    open fun resizeLg() {
        if (!::lg.isInitialized) {
            initLG()
            windowElements.forEach { it.callResize() }
            return
        }

        if (lg.windowWidth != logicalSize.x.toInt() ||
            lg.windowHeight != logicalSize.y.toInt()
        ) {
            windowElements.forEach { it.callResize() }
            initLG()
        }
    }

    // -------------------------------------------------------------------------
    // ✦ ЭЛЕМЕНТЫ ОКНА
    // -------------------------------------------------------------------------

    var windowElements = ArrayList<WindowElement>()

    open fun oneInside(position: Vec2) : Boolean = getTopElementAt(position) != null

    fun getTopElementAt(position: Vec2): WindowElement? {
        for (i in windowElements.asReversed()) {
            if (i.inside(position)) return i
        }
        return null
    }

    fun buttonsLeftPressed(position: Vec2) {
        getTopElementAt(position)?.callLeftClick(position)
    }

    fun buttonsRightPressed(position: Vec2) {
        getTopElementAt(position)?.callRightClick(position)
    }

    open fun physicPreUpdate() {

    }
    open fun physicUpdate() {
        windowElements.forEach { it.callPhysic() }
    }
    open fun physicPostUpdate() {

    }
    fun buttonsMouseInsideFalse() {
        windowElements.forEach { it.mouseInside = false }
    }
    fun buttonsMouseInside(position: Vec2) {
        windowElements.forEach { it.mouseInside = false }
        getTopElementAt(position)?.let { it.mouseInside = true }
    }

    // -------------------------------------------------------------------------
    // ✦ МЕТОДЫ ИНФОРМАЦИИ О ПОЗИЦИИ
    // -------------------------------------------------------------------------

    open fun inside(position: Vec2): Boolean {
        return Functions.tap(windowPosition, windowSize, position)
    }

    fun toWindowLogicalPos(position: Vec2): Vec2 {
        val local = position - windowPosition
        return Vec2(local.x * logicalMultiple.x, local.y * logicalMultiple.y)
    }

    fun toScreenLogicalPos(logical: Vec2): Vec2 {
        return Vec2(
            windowPosition.x + logical.x / logicalMultiple.x,
            windowPosition.y + logical.y / logicalMultiple.y
        )
    }

    fun toWindowLogicalSize(size: Vec2): Vec2 {
        return Vec2(size.x * logicalMultiple.x, size.y * logicalMultiple.y)
    }

    fun toScreenLogicalSize(logicalSize: Vec2): Vec2 {
        return Vec2(
            logicalSize.x / logicalMultiple.x,
            logicalSize.y / logicalMultiple.y
        )
    }

    fun setBase(windowPosition: Vec2, windowSize: Vec2, logicalMultiple: Vec2 = Vec2(1f)) {
        this.windowPosition = windowPosition
        this.windowSize = windowSize
        this.logicalMultiple = logicalMultiple
    }

    // -------------------------------------------------------------------------
    // ✦ ЛАЙФЦИКЛ / ОБНОВЛЕНИЕ
    // -------------------------------------------------------------------------

    open var destroyFunction: AbstractWindow.() -> Unit = {}

    override fun start() {}
    override fun destroy() {destroyFunction()}
    override suspend fun threadUpdate() {}

    override fun preUpdate() {
        windowElements.forEach { it.callUpdate() }
    }

    override fun update() {}
    override fun postUpdate() {}
    override fun postRenderUpdate() {}

    // -------------------------------------------------------------------------
    // ✦ РЕНДЕР
    // -------------------------------------------------------------------------

    fun render(mainRender: MainRender) {
        beginDraw()
        preDraw(mainRender)
        draw(mainRender)
        windowElements.forEach { it.callPreRender(lg) }
        windowElements.forEach { it.callRender(lg) }
        windowElements.forEach { it.callPostRender(lg) }
        postDraw(mainRender)
        endDraw()
    }

    open fun beginDraw() { lg.beginDraw() }
    open fun preDraw(mainRender: MainRender) {}
    open fun draw(mainRender: MainRender) {}
    open fun endDraw() { lg.endDraw() }
    open fun postDraw(mainRender: MainRender) {}

    open fun displayPreDraw(lg: LGraphics) {}
    open fun displayPostDraw(lg: LGraphics) {}

    open fun baseRender(mainRender: MainRender) {
        resizeLg()
        mainRender.lg.beginDraw()
        mainRender.lg.fill(0f)
        mainRender.lg.setBlock(windowPosition, windowSize)
        mainRender.lg.endDraw()
    }

    open fun basePostRender(mainRender: MainRender) {}

    // -------------------------------------------------------------------------
    // ✦ ОБРАБОТКА СООБЩЕНИЙ
    // -------------------------------------------------------------------------

    open fun handleMessage(windowMessage: WindowMessage) {}

    // -------------------------------------------------------------------------
    // ✦ МЫШЬ
    // ------------ -------------------------------------------------------------

    override var startLeftClick: Vec2 = Vec2(0f)
    override var startRightClick: Vec2 = Vec2(0f)
    override var startCenterClick: Vec2 = Vec2(0f)
    
    override fun mouseStartFrame() {}

    open var mousePosition = Vec2.ZERO

    override fun mouseUpdate(position: Vec2, oldPosition: Vec2) {
        mousePosition = position
    }
    override fun mouseMove(position: Vec2, oldPosition: Vec2) {}
    override fun mouseWheel(position: Vec2, event: MouseEvent) {}

    override fun leftPressed(position: Vec2) {}
    override fun rightPressed(position: Vec2) {}
    override fun centerPressed(position: Vec2) {}

    override fun leftReleased(position: Vec2) {}
    override fun rightReleased(position: Vec2) {}
    override fun centerReleased(position: Vec2) {}

    override fun leftUpdate(position: Vec2, oldPosition: Vec2) {}
    override fun rightUpdate(position: Vec2, oldPosition: Vec2) {}
    override fun centerUpdate(position: Vec2, oldPosition: Vec2) {}

    override fun leftDoubleClick(position: Vec2) {}
    override fun rightDoubleClick(position: Vec2) {}
    override fun centerDoubleClick(position: Vec2) {}

    override fun leftDragStart(position: Vec2) {}
    override fun rightDragStart(position: Vec2) {}
    override fun centerDragStart(position: Vec2) {}

    override fun leftDrag(position: Vec2, oldPosition: Vec2, start: Vec2) {}
    override fun rightDrag(position: Vec2, oldPosition: Vec2, start: Vec2) {}
    override fun centerDrag(position: Vec2, oldPosition: Vec2, start: Vec2) {}

    override fun leftDragEnd(position: Vec2) {}
    override fun rightDragEnd(position: Vec2) {}
    override fun centerDragEnd(position: Vec2) {}

    open fun leftPressedCaptured(position: Vec2) {}
    open fun rightPressedCaptured(position: Vec2) {}
    open fun centerPressedCaptured(position: Vec2) {}

    open fun leftReleasedCaptured(position: Vec2) {}
    open fun rightReleasedCaptured(position: Vec2) {}
    open fun centerReleasedCaptured(position: Vec2) {}

    open fun leftUpdateCaptured(position: Vec2, oldPosition: Vec2) {}
    open fun rightUpdateCaptured(position: Vec2, oldPosition: Vec2) {}
    open fun centerUpdateCaptured(position: Vec2, oldPosition: Vec2) {}

    open fun leftDragStartCaptured(position: Vec2) {}
    open fun rightDragStartCaptured(position: Vec2) {}
    open fun centerDragStartCaptured(position: Vec2) {}

    open fun leftDragCaptured(position: Vec2, oldPosition: Vec2, start: Vec2) {}
    open fun rightDragCaptured(position: Vec2, oldPosition: Vec2, start: Vec2) {}
    open fun centerDragCaptured(position: Vec2, oldPosition: Vec2, start: Vec2) {}

    open fun leftDragEndCaptured(position: Vec2) {}
    open fun rightDragEndCaptured(position: Vec2) {}
    open fun centerDragEndCaptured(position: Vec2) {}

    open fun mouseWheelCaptured(position: Vec2, event: MouseEvent) {}

    // -------------------------------------------------------------------------
    // ✦ КЛАВИАТУРА
    // -------------------------------------------------------------------------

    override fun keyboardStartFrame() {}
    override fun keyboardUpdate() {}

    override fun keyPressed(key: Int) {}
    override fun keyPressed(key: Int, keyChar: Char) {}

    override fun keyReleased(key: Int) {}
    override fun keyReleased(key: Int, keyChar: Char) {}

    override fun keyRepeat(key: Int) {}
    override fun keyRepeat(key: Int, keyChar: Char) {}

    override fun keyDoublePress(key: Int) {}
    override fun keyDoublePress(key: Int, keyChar: Char) {}

    override fun keyTyped(keyChar: Char) {}
    override fun keyUpdate(key: Int, heldTime: Float) {}
    override fun keyUpdate(key: Int, keyChar: Char, heldTime: Float) {}
    override fun keyCombo(keys: Set<Int>) {}

    override fun windowBlocked() {
        mouseInside = false
        windowElements.forEach { it.mouseInside = false }
    }
    override fun windowUnblocked() {}
    override fun blockedUpdate() {}

    override val frameLeftTop: Vec2
        get() = windowPosition - windowSize.halved().invertedY()

    override val frameRightBottom: Vec2
        get() = windowPosition - windowSize.halved().invertedX()

}
