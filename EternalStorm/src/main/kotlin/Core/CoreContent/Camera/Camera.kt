package la.vok.Core.CoreContent.Camera

import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

interface Camera {
    var pos: Vec2
    var size: Float
    var zoomSpeed: Float

    // =========================
    // TRANSFORM
    // =========================

    fun useCamera(pos: Vec2): Vec2 =
        pos * size - this.pos

    fun useCameraSize(size: Vec2): Vec2 =
        size * this.size

    fun useCameraSize(size: Float): Float =
        size * this.size

    fun toWorldPos(screenPos: Vec2): Vec2 =
        (screenPos + pos) / size

    fun toWorldSize(size: Vec2): Vec2 =
        size / this.size

    fun toWorldSize(size: Float): Float =
        size / this.size

    // =========================
    // AXIS HELPERS
    // =========================

    fun useCameraPosX(x: Float): Float = x * size - pos.x
    fun useCameraPosY(y: Float): Float = y * size - pos.y

    fun useCameraSizeX(x: Float): Float = x * size
    fun useCameraSizeY(y: Float): Float = y * size

    fun toWorldPosX(x: Float): Float = (x + pos.x) / size
    fun toWorldPosY(y: Float): Float = (y + pos.y) / size

    fun toWorldSizeX(x: Float): Float = x / size
    fun toWorldSizeY(y: Float): Float = y / size

    // =========================
    // CAMERA CONTROL
    // =========================

    /** Мгновенно установить позицию камеры */
    fun setCameraPos(worldPos: Vec2) {
        pos = worldPos.copy()
    }

    /** Мгновенно установить зум камеры */
    fun setCameraZoom(
        zoom: Float,
        minZoom: Float = 0.01f,
        maxZoom: Float = 100f
    ) {
        size = zoom.coerceIn(minZoom, maxZoom)
    }

    fun updateCamera() {}
    fun moveCamera(screenDelta: Vec2) {}

    fun zoomRelative(
        zoomDelta: Float,
        cursorLogical: Vec2,
        minZoom: Float = 0.01f,
        maxZoom: Float = 100f
    )
}
