package la.vok.Core.GameContent.Layers

import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState
import processing.core.PImage

open class EffectLayer(var point: LPoint, var mp: Float = 1f) {
    var mud = true
    var draw = false

    var lg: LGraphics? = null

    protected fun beginDraw() {
        if (mud) {
            initLg()
        }
        lg!!.beginDraw()
    }
    fun tick() {
        if (draw) {
            AppState.logger.trace("LG ${this::class.simpleName} DRAW")
            beginDraw()
            draw()
            endDraw()
            draw = false
        }
    }
    protected open fun draw() {

    }

    protected fun endDraw() {
        if (lg == null) {
            AppState.logger.error("LG ${this::class.simpleName} is null")
        }
        lg!!.endDraw()
    }

    open fun initLg() {
        AppState.logger.info("init LG ${this::class.simpleName}")
        lg = LGraphics(point.x, point.y, mp)
        mud = false
    }

    fun resize(point: LPoint, mp: Float = 1f) {
        AppState.logger.debug("resize LG ${this::class.simpleName}")
        this.point = point
        this.mp = mp
        mud = true
    }

    fun clear() {
        lg = null
    }

    fun containsImage() : Boolean {

        return lg != null
    }
    fun getImage() : PImage {
        if (lg == null) {
            AppState.logger.error("getImage() LG ${this::class.simpleName} is null")
        }
        return lg!!.pg as PImage
    }
}