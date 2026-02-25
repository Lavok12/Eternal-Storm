package la.vok.Core.CoreContent.Camera

import la.vok.Core.FrameLimiter
import la.vok.LavokLibrary.KotlinPlus.lerp
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.pow

class SoftCamera(
    override var pos: Vec2 = Vec2(0f),
    override var size: Float = 1f,
    var smoothness: Float = 0.15f
) : Camera {

    override var zoomSpeed: Float = 0.1f

    private var targetPos: Vec2 = pos.copy()
    private var targetSize: Float = size

    private fun t(): Float {
        val dt = FrameLimiter.logicDeltaTime
        return 1f - (1f - smoothness).pow(dt)
    }


    override fun setCameraPos(worldPos: Vec2) {
        targetPos = worldPos.copy()
    }

    override fun setCameraZoom(
        zoom: Float,
        minZoom: Float,
        maxZoom: Float
    ) {
        targetSize = zoom.coerceIn(minZoom, maxZoom)
    }

    override fun moveCamera(screenDelta: Vec2) {
        targetPos = targetPos - screenDelta
    }

    override fun zoomRelative(
        zoomDelta: Float,
        cursorLogical: Vec2,
        minZoom: Float,
        maxZoom: Float
    ) {
        if (zoomDelta == 0f) return

        val zoomFactor = 1f + -zoomDelta * zoomSpeed
        val oldZoom = targetSize
        val newZoom = (targetSize * zoomFactor).coerceIn(minZoom, maxZoom)
        val ratio = newZoom / oldZoom

        targetPos = (targetPos + cursorLogical) * ratio - cursorLogical
        targetSize = newZoom
    }

    override fun updateCamera() {
        val k = t()
        pos = pos.lerp(targetPos, k)
        size = size.lerp(targetSize, k)
    }


    fun snap() {
        pos = targetPos.copy()
        size = targetSize
    }
}
