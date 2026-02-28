package la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.Parts.Tooltip
import la.vok.LavokLibrary.Geometry.FrameRect
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Utils.Functions
import la.vok.LavokLibrary.Utils.MetaStorage
import la.vok.LavokLibrary.Vectors.Vec2

open class WindowElement(
    val window: AbstractWindow,
    var position: Vec2 = Vec2.ZERO,
    var size: Vec2 = Vec2.ZERO,
    var align: Vec2 = Vec2.ZERO,
    var insideMethod: WindowElement.(Vec2) -> Boolean = EMPTY_INSIDE,
    var preRender: WindowElement.(LGraphics) -> Unit = {},
    var render: WindowElement.(LGraphics) -> Unit = {},
    var postRender: WindowElement.(LGraphics) -> Unit = {},
    var resize: WindowElement.() -> Unit = {},
    var update: WindowElement.() -> Unit = {},
    var physicUpdate: WindowElement.() -> Unit = {},
    var leftClick: WindowElement.(Vec2) -> Unit = {},
    var rightClick: WindowElement.(Vec2) -> Unit = {},
    var start: WindowElement.() -> Unit = {},
    var tooltip: Tooltip? = null
) : MetaStorage(), FrameRect {

    companion object {
        val EMPTY_INSIDE: WindowElement.(Vec2) -> Boolean = { false }
    }

    var mouseInside = false

    // --------------------------------------------------------------------
    // POSITION CACHE
    // --------------------------------------------------------------------
    private var cachedPosition: Vec2 = Vec2.ZERO
    private var positionDirty: Boolean = true

    fun markDirty() {
        positionDirty = true
    }

    open var positionWithCache: Vec2
        get() {
            if (positionDirty) {
                cachedPosition = calculatePositionInternal()
                positionDirty = false
            }
            return cachedPosition
        }
        set(value) {
            position = value
            markDirty()
        }


    protected open fun calculatePositionInternal(): Vec2 {
        val full = window.logicalSize
        val usable = full - window.padding * 2f - size

        val norm = (Vec2(align.x, -align.y) + Vec2(1f)) * 0.5f

        val topLeft = Vec2(
            -full.x / 2f + window.padding.x + size.x / 2f,
            full.y / 2f - window.padding.y - size.y / 2f
        )

        return Vec2(
            topLeft.x + usable.x * norm.x,
            topLeft.y - usable.y * norm.y
        ) + position
    }

    // --------------------------------------------------------------------
    // OVERRIDE FRAME RECT
    // --------------------------------------------------------------------
    override val frameLeftTop: Vec2
        get() = positionWithCache - size.invertedY()

    override val frameRightBottom: Vec2
        get() = positionWithCache - size.invertedX()

    // --------------------------------------------------------------------
    // POSITION & SIZE MUTATORS
    // --------------------------------------------------------------------
    var cachedSize: Vec2
        get() = size
        set(value) {
            size = value
            markDirty()
        }

    var cachedAlign: Vec2
        get() = align
        set(value) {
            align = value
            markDirty()
        }

    init {
        start()
    }

    // --------------------------------------------------------------------
    // INSIDE
    // --------------------------------------------------------------------
    open fun inside(pos: Vec2): Boolean =
        if (insideMethod !== EMPTY_INSIDE)
            insideMethod(pos)
        else
            absInside(positionWithCache, size, pos)

    open fun absInside(pos: Vec2, size: Vec2, tap: Vec2) : Boolean {
        return Functions.tap(pos, size, tap)
    }

    // --------------------------------------------------------------------
    // CALLS
    // --------------------------------------------------------------------
    open fun callUpdate() {
        tooltip?.takeIf { mouseInside }?.let {
            window.coreController.tooltipController.push(
                it,
                window.coreController.mouseInput.logicalPosition
            )
        }
        update()
    }
    open fun callPhysic() = physicUpdate()

    open fun callPreRender(lg: LGraphics) = preRender(lg)
    open fun callRender(lg: LGraphics) = render(lg)
    open fun callPostRender(lg: LGraphics) = postRender(lg)

    open fun callResize() = resize()

    open fun callLeftClick(pos: Vec2) = leftClick(pos)
    open fun callRightClick(pos: Vec2) = rightClick(pos)
}
