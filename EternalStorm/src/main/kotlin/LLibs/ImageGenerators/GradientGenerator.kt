package la.vok.LavokLibrary.Gradient

import la.vok.LavokLibrary.Vectors.LColor
import processing.core.PApplet
import processing.core.PImage
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel

object GradientGenerator {

    private val logger = ConsoleLogger("GradientGenerator", LogLevel.DEBUG)
    private val cache = HashMap<Key, PImage>()

    private data class Key(
        val c1: Int,
        val c2: Int,
        val p1x: Int, val p1y: Int,
        val p2x: Int, val p2y: Int,
        val w: Int, val h: Int
    )

    fun generate(info: GradientInfo, app: PApplet): PImage {
        val size = info.resolution

        val key = Key(
            info.c1.toIntARGB(),
            info.c2.toIntARGB(),
            info.p1.x, info.p1.y,
            info.p2.x, info.p2.y,
            size.x, size.y
        )

        cache[key]?.let {
            logger.trace("Cache hit: $key")
            return it
        }

        logger.debug("Generating linear gradient: ${size.x}x${size.y} from (${info.p1.x}, ${info.p1.y}) to (${info.p2.x}, ${info.p2.y})")

        val pg = app.createGraphics(size.x, size.y)
        pg.beginDraw()
        pg.loadPixels()

        val p1x = info.p1.x.toFloat()
        val p1y = info.p1.y.toFloat()
        val p2x = info.p2.x.toFloat()
        val p2y = info.p2.y.toFloat()

        val dx = p2x - p1x
        val dy = p2y - p1y
        val lenSq = dx * dx + dy * dy

        for (y in 0 until size.y) {
            for (x in 0 until size.x) {
                val vx = x - p1x
                val vy = y - p1y

                var t = (vx * dx + vy * dy) / lenSq
                t = t.coerceIn(0f, 1f)

                val col = LColor.lerp(info.c1, info.c2, t)
                pg.pixels[y * size.x + x] = col.toIntARGB()
            }
        }

        pg.updatePixels()
        pg.endDraw()

        val img = pg.get()
        if (info.save) {
            logger.info("Gradient cached: $key")
            cache[key] = img
        }
        return img
    }
}