package la.vok.Core.CoreContent.Camera

import la.vok.LavokLibrary.Vectors.Vec2

class StandartCamera(
    override var pos: Vec2 = Vec2(0f),
    override var size: Float = 1f
) : Camera {

    override var zoomSpeed: Float = 0.1f

    override fun moveCamera(screenDelta: Vec2) {
        pos = pos - screenDelta
    }

    override fun zoomRelative(
        zoomDelta: Float,
        cursorLogical: Vec2,
        minZoom: Float,
        maxZoom: Float
    ) {
        if (zoomDelta == 0f) return

        val zoomFactor = 1f + -zoomDelta * zoomSpeed
        val oldZoom = size
        val newZoom = (size * zoomFactor).coerceIn(minZoom, maxZoom)
        val ratio = newZoom / oldZoom

        pos = (pos + cursorLogical) * ratio - cursorLogical
        size = newZoom
    }
}
