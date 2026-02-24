package la.vok.LavokLibrary.Geometry

import la.vok.LavokLibrary.Vectors.Vec2

interface FrameRect {

    // -------------------------------------------------------------------------
    // ✦ ОБЯЗАТЕЛЬНЫЕ ДАННЫЕ
    // -------------------------------------------------------------------------

    /** Левый верхний угол (frame space) */
    val frameLeftTop: Vec2

    /** Правый нижний угол (frame space) */
    val frameRightBottom: Vec2

    // -------------------------------------------------------------------------
    // ✦ ПРОИЗВОДНЫЕ (ПО УМОЛЧАНИЮ)
    // -------------------------------------------------------------------------

    val frameLeft: Float
        get() = frameLeftTop.x

    val frameRight: Float
        get() = frameRightBottom.x

    val frameTop: Float
        get() = frameLeftTop.y

    val frameBottom: Float
        get() = frameRightBottom.y

    val frameSize: Vec2
        get() = Vec2(frameRight - frameLeft, frameTop - frameBottom)

    val frameHalfSize: Vec2
        get() = frameSize * 0.5f

    val frameCenter: Vec2
        get() = frameLeftTop + frameHalfSize

    val frameLeftBottom: Vec2
        get() = Vec2(frameLeft, frameBottom)

    val frameRightTop: Vec2
        get() = Vec2(frameRight, frameTop)

    // -------------------------------------------------------------------------
    // ✦ ПРОВЕРКИ
    // -------------------------------------------------------------------------

    fun contains(pos: Vec2): Boolean =
        pos.x in frameLeft..frameRight &&
                pos.y in frameBottom..frameTop  // Y вверх: bottom <= y <= top

    fun contains(rect: FrameRect): Boolean =
        rect.frameLeft >= frameLeft &&
                rect.frameRight <= frameRight &&
                rect.frameBottom >= frameBottom &&
                rect.frameTop <= frameTop

    fun intersects(rect: FrameRect): Boolean =
        frameLeft < rect.frameRight &&
                frameRight > rect.frameLeft &&
                frameBottom < rect.frameTop &&
                frameTop > rect.frameBottom

    fun isOutside(rect: FrameRect): Boolean =
        frameRight <= rect.frameLeft ||
                frameLeft >= rect.frameRight ||
                frameTop <= rect.frameBottom ||
                frameBottom >= rect.frameTop

    fun touches(rect: FrameRect): Boolean =
        frameRight >= rect.frameLeft &&
                frameLeft <= rect.frameRight &&
                frameTop >= rect.frameBottom &&
                frameBottom <= rect.frameTop

}
