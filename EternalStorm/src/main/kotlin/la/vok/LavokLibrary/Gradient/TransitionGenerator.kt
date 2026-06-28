package la.vok.LavokLibrary.Gradient

import processing.core.PApplet
import processing.core.PImage
import la.vok.State.AppState
import kotlin.math.max

object TransitionGenerator {
    private data class Key(
        val imageHash: Int,
        val neighborMask: Int,
        val strength: Float,
        val cornerStrength: Float
    )

    private val cache = HashMap<Key, PImage>()

    fun generate(info: TransitionInfo, app: PApplet): PImage {
        val key = Key(
            System.identityHashCode(info.image),
            info.neighborMask,
            info.strength,
            info.cornerStrength
        )

        cache[key]?.let { return it }

        val size = AppState.TRANSITION_TEXTURE_SIZE
        val maskPg = app.createGraphics(size, size)
        maskPg.beginDraw()
        maskPg.clear()
        
        // neighborMask bits:
        // 1 - TOP, 2 - RIGHT, 4 - BOTTOM, 8 - LEFT
        // 16 - TOP_RIGHT, 32 - BOTTOM_RIGHT, 64 - BOTTOM_LEFT, 128 - TOP_LEFT

        maskPg.loadPixels()
        info.image.loadPixels()

        val scaleX = info.image.width.toFloat() / size
        val scaleY = info.image.height.toFloat() / size

        for (y in 0 until size) {
            for (x in 0 until size) {
                val px = x / size.toFloat()
                val py = y / size.toFloat()

                var maxAlpha = 0f
                
                // Direct sides (fade out over ~0.33 of block)
                if ((info.neighborMask and 1) != 0) maxAlpha = max(maxAlpha, (1f - py * 3f) * info.strength)
                if ((info.neighborMask and 4) != 0) maxAlpha = max(maxAlpha, (1f - (1f - py) * 3f) * info.strength)
                if ((info.neighborMask and 8) != 0) maxAlpha = max(maxAlpha, (1f - px * 3f) * info.strength)
                if ((info.neighborMask and 2) != 0) maxAlpha = max(maxAlpha, (1f - (1f - px) * 3f) * info.strength)

                // Corners (fade out diagonally, using Euclidean distance)
                if ((info.neighborMask and 128) != 0) maxAlpha = max(maxAlpha, (1f - kotlin.math.sqrt(px*px + py*py) * 3f) * info.cornerStrength)
                if ((info.neighborMask and 16) != 0) maxAlpha = max(maxAlpha, (1f - kotlin.math.sqrt((1f - px)*(1f - px) + py*py) * 3f) * info.cornerStrength)
                if ((info.neighborMask and 32) != 0) maxAlpha = max(maxAlpha, (1f - kotlin.math.sqrt((1f - px)*(1f - px) + (1f - py)*(1f - py)) * 3f) * info.cornerStrength)
                if ((info.neighborMask and 64) != 0) maxAlpha = max(maxAlpha, (1f - kotlin.math.sqrt(px*px + (1f - py)*(1f - py)) * 3f) * info.cornerStrength)
                
                maxAlpha = maxAlpha.coerceIn(0f, 1f)

                val srcX = (x * scaleX).toInt().coerceIn(0, info.image.width - 1)
                val srcY = (y * scaleY).toInt().coerceIn(0, info.image.height - 1)
                val srcColor = info.image.pixels[srcY * info.image.width + srcX]
                
                val srcAlpha = (srcColor ushr 24) and 0xFF
                val finalAlpha = (srcAlpha * maxAlpha).toInt()

                maskPg.pixels[y * size + x] = (finalAlpha shl 24) or (srcColor and 0x00FFFFFF)
            }
        }

        maskPg.updatePixels()
        maskPg.endDraw()

        val result = maskPg.get()
        cache[key] = result
        return result
    }
}
