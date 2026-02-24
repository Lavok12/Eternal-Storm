package la.vok.LavokLibrary.Gradient

import processing.core.PApplet
import processing.core.PImage
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel

object CheckerboardGenerator {

    private val logger = ConsoleLogger("CheckerboardGenerator", LogLevel.DEBUG)
    private val cache = HashMap<Key, PImage>()

    private data class Key(
        val cellSize: Int,
        val w: Int,
        val h: Int,
        val c1: Int,
        val c2: Int
    )

    fun generate(info: CheckerboardInfo, app: PApplet): PImage {
        val size = info.resolution

        val key = Key(
            info.cellSize,
            size.x, size.y,
            info.color1,
            info.color2
        )

        cache[key]?.let {
            logger.trace("Cache hit: $key")
            return it
        }

        logger.debug("Generating new checkerboard: ${size.x}x${size.y}, cell: ${info.cellSize}")

        val pg = app.createGraphics(size.x, size.y)
        pg.beginDraw()
        pg.loadPixels()

        val cell = info.cellSize

        for (y in 0 until size.y) {
            for (x in 0 until size.x) {
                val cx = x / cell
                val cy = y / cell

                val isWhite = (cx + cy) % 2 == 0
                pg.pixels[y * size.x + x] =
                    if (isWhite) info.color1 else info.color2
            }
        }

        pg.updatePixels()
        pg.endDraw()

        val img = pg.get()
        if (info.save) {
            logger.info("Texture cached: $key")
            cache[key] = img
        }

        return img
    }
}