package la.vok.Core.CoreControllers.CoreContent.Windows.DrawElements

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.LavokLibrary.KotlinPlus.forEach
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow

object SceneDrawElements {
    fun drawGrid(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera, autoStepRange: Boolean = false, stepRange: Float = 50f, alpha: Float = 100f, width: Float = 2f) {
        var cameraSize = camera.size
        var sr = stepRange
        if (autoStepRange) {
            val zoomLevel = floor(log2(1f/cameraSize)).toInt()
            val clamped = zoomLevel.coerceIn(-10, 10)
            sr = stepRange * 2f.pow(clamped)
        }
        lg.noStroke()
        lg.fill(200f,alpha)

        var leftTop = camera.toWorldPos(pos - size.invertedY().half())
        var rightDown = camera.toWorldPos(pos - size.invertedX().half())

        val startX = floor(leftTop.x / sr) * sr
        val endX   = ceil(rightDown.x / sr) * sr

        forEach(startX, endX, sr) { x ->
            val sx = camera.useCameraPosX(x)
            lg.setBlock(
                Vec2(sx, 0f),
                Vec2(width, lg.disH)
            )
        }

        val startY = floor(rightDown.y / sr) * sr
        val endY   = ceil(leftTop.y / sr) * sr

        forEach(startY, endY, sr) { y ->
            val sy = camera.useCameraPosY(y)
            lg.setBlock(
                Vec2(0f, sy),
                Vec2(lg.disW, width)
            )
        }
    }

    fun drawComplexGrid(
        lg: LGraphics,
        pos: Vec2,
        size: Vec2,
        camera: Camera,
        autoStepRange: Boolean = true,
        layers: List<GridLayer>
    ) {
        for (layer in layers) {
            drawGrid(
                lg = lg,
                pos = pos,
                size = size,
                camera = camera,
                autoStepRange = autoStepRange,
                stepRange = layer.step,
                alpha = layer.alpha,
                width = layer.width
            )
        }
    }

}