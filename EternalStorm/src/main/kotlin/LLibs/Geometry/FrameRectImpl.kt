package la.vok.LavokLibrary.Geometry

import la.vok.LavokLibrary.Vectors.Vec2

data class FrameRectImpl(
    override val frameLeftTop: Vec2,
    override val frameRightBottom: Vec2
) : FrameRect {

    init {
        require(frameLeftTop.x <= frameRightBottom.x) {
            "logicalLeftTop.x must be <= logicalRightBottom.x"
        }
        require(frameLeftTop.y >= frameRightBottom.y) {
            "logicalLeftTop.y must be >= logicalRightBottom.y"
        }
    }

    constructor(rect: FrameRect) : this(
        rect.frameLeftTop,
        rect.frameRightBottom
    )

    companion object {

        /** Создание из центра и размера */
        fun fromCenterAndSize(center: Vec2, size: Vec2): FrameRectImpl {
            val half = size.halved()
            return FrameRectImpl(
                frameLeftTop = Vec2(
                    center.x - half.x,
                    center.y + half.y
                ),
                frameRightBottom = Vec2(
                    center.x + half.x,
                    center.y - half.y
                )
            )
        }

        /** Создание из позиции (левый верх) и размера */
        fun fromLeftTopAndSize(leftTop: Vec2, size: Vec2): FrameRectImpl =
            FrameRectImpl(
                frameLeftTop = leftTop,
                frameRightBottom = leftTop + Vec2(size.x, -size.y)
            )

        /** Создание из двух углов (автонормализация) */
        fun fromCorners(a: Vec2, b: Vec2): FrameRectImpl {
            val left   = minOf(a.x, b.x)
            val right  = maxOf(a.x, b.x)
            val top    = maxOf(a.y, b.y)
            val bottom = minOf(a.y, b.y)

            return FrameRectImpl(
                Vec2(left, top),
                Vec2(right, bottom)
            )
        }
    }
}
