package la.vok.LavokLibrary.Gradient

import processing.core.PApplet
import processing.core.PImage

object ShadowGenerator {

    private data class Key(
        val imageHash: Int,
        val outputW: Int,
        val outputH: Int,
        val padding: Int,
        val blurRadius: Int
    )

    private val cache = HashMap<Key, PImage>()

    fun generate(info: ShadowInfo, app: PApplet): PImage {
        val key = Key(
            System.identityHashCode(info.image),
            info.outputSize.x,
            info.outputSize.y,
            info.padding,
            info.blurRadius
        )

        cache[key]?.let { return it }

        val innerW = info.outputSize.x - info.padding * 2
        val innerH = info.outputSize.y - info.padding * 2

        val pg = app.createGraphics(info.outputSize.x, info.outputSize.y)
        pg.beginDraw()
        pg.clear()

        // Рисуем чёрную маску формы изображения
        pg.loadPixels()
        info.image.loadPixels()
        val scaleX = info.image.width.toFloat() / innerW
        val scaleY = info.image.height.toFloat() / innerH

        for (y in 0 until innerH) {
            for (x in 0 until innerW) {
                val srcX = (x * scaleX).toInt().coerceIn(0, info.image.width - 1)
                val srcY = (y * scaleY).toInt().coerceIn(0, info.image.height - 1)
                val srcAlpha = (info.image.pixels[srcY * info.image.width + srcX] ushr 24) and 0xFF
                // Чёрный пиксель с альфой исходного изображения
                pg.pixels[(y + info.padding) * info.outputSize.x + (x + info.padding)] =
                    (srcAlpha shl 24) or 0x000000
            }
        }

        pg.updatePixels()
        pg.filter(PApplet.BLUR, info.blurRadius.toFloat())
        pg.endDraw()

        val result = pg.get()
        if (info.save) cache[key] = result
        return result
    }
}