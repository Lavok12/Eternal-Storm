package la.vok.LavokLibrary.Gradient

import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import processing.core.PApplet
import processing.core.PImage
import kotlin.math.min

/**
 * Генератор рамочных теней.
 *
 * INNER — изображение ТОГО ЖЕ размера что и size.
 *   Тень рисуется ВНУТРИ, от края вглубь на spread пикселей.
 *   Прозрачный центр, тёмные края.
 *   Параметр inset: отступ от границы до начала тени.
 *
 * OUTER — изображение БОЛЬШЕ на (spread * 2) по каждой стороне.
 *   Центральная зона (size.x × size.y) прозрачна.
 *   Тень рисуется СНАРУЖИ: затухает от внутреннего края изображения наружу.
 *   При рендеринге помести это изображение с тем же центром что и контент —
 *   тень будет выступать за его границы.
 */
object ShadowFrameGenerator {

    private val logger = ConsoleLogger("ShadowFrameGenerator", LogLevel.DEBUG)
    private val cache = HashMap<Key, PImage>()

    private data class Key(
        val w: Int, val h: Int,
        val type: ShadowType,
        val spread: Int,
        val inset: Int,
        val color: Int,
        val intensityBits: Int
    )

    fun generate(info: ShadowFrameInfo, app: PApplet): PImage {
        val key = Key(
            info.size.x, info.size.y,
            info.type,
            info.spread,
            info.inset,
            info.color.toIntARGB(),
            java.lang.Float.floatToIntBits(info.intensity)
        )
        cache[key]?.let { logger.trace("Cache hit: $key"); return it }

        val result = when (info.type) {
            ShadowType.INNER -> generateInner(info, app)
            ShadowType.OUTER -> generateOuter(info, app)
        }

        if (info.save) {
            logger.info("ShadowFrame cached: $key")
            cache[key] = result
        }
        return result
    }

    /** Тень внутри изображения: прозрачный центр, тёмные края. Размер = size. */
    private fun generateInner(info: ShadowFrameInfo, app: PApplet): PImage {
        val w = info.size.x
        val h = info.size.y
        val spread = info.spread.toFloat()
        val cr = info.color.r.toInt().coerceIn(0, 255)
        val cg = info.color.g.toInt().coerceIn(0, 255)
        val cb = info.color.b.toInt().coerceIn(0, 255)

        val pg = app.createGraphics(w, h)
        pg.beginDraw()
        pg.loadPixels()
        pg.pixels.fill(0x00000000.toInt())

        for (y in 0 until h) {
            for (x in 0 until w) {
                val dx = min(x, w - 1 - x).toFloat()
                val dy = min(y, h - 1 - y).toFloat()
                val d = min(dx, dy)

                val innerD = d - info.inset
                if (innerD < 0 || innerD >= spread) continue
                val alpha = (1f - innerD / spread) * info.intensity
                val a = (alpha * 255f).toInt().coerceIn(0, 255)
                pg.pixels[y * w + x] = (a shl 24) or (cr shl 16) or (cg shl 8) or cb
            }
        }

        pg.updatePixels()
        pg.endDraw()

        logger.debug("INNER ${w}x${h} spread=${info.spread} inset=${info.inset}")
        return pg.get()
    }

    /**
     * Тень снаружи: изображение увеличено на spread с каждой стороны.
     * Центральная зона (оригинальный size) — прозрачная.
     * Тень затухает от внутреннего края наружу.
     * Итоговый размер: (size.x + spread*2) × (size.y + spread*2)
     */
    private fun generateOuter(info: ShadowFrameInfo, app: PApplet): PImage {
        val s = info.spread
        val fw = info.size.x + s * 2   // полная ширина с тенью
        val fh = info.size.y + s * 2   // полная высота с тенью
        val spread = s.toFloat()
        val cr = info.color.r.toInt().coerceIn(0, 255)
        val cg = info.color.g.toInt().coerceIn(0, 255)
        val cb = info.color.b.toInt().coerceIn(0, 255)

        val pg = app.createGraphics(fw, fh)
        pg.beginDraw()
        pg.loadPixels()
        pg.pixels.fill(0x00000000.toInt())

        for (y in 0 until fh) {
            for (x in 0 until fw) {
                // Расстояние до внутреннего «отверстия» (прозрачного центра)
                // Центральная зона: [s .. s+size.x) × [s .. s+size.y)
                val insideX = x in s until (s + info.size.x)
                val insideY = y in s until (s + info.size.y)

                // Если пиксель внутри контентной области — пропускаем (прозрачный)
                if (insideX && insideY) continue

                // Расстояние до границы контентной области
                val dx = when {
                    x < s -> (s - x).toFloat()        // слева от контента
                    x >= s + info.size.x -> (x - (s + info.size.x - 1)).toFloat()  // справа
                    else -> 0f
                }
                val dy = when {
                    y < s -> (s - y).toFloat()        // сверху
                    y >= s + info.size.y -> (y - (s + info.size.y - 1)).toFloat()   // снизу
                    else -> 0f
                }
                val d = if (dx == 0f) dy else if (dy == 0f) dx else kotlin.math.sqrt(dx * dx + dy * dy)

                val innerD = d - info.inset
                if (innerD < 0 || innerD >= spread) continue
                val alpha = (1f - innerD / spread) * info.intensity
                val a = (alpha * 255f).toInt().coerceIn(0, 255)
                pg.pixels[y * fw + x] = (a shl 24) or (cr shl 16) or (cg shl 8) or cb
            }
        }

        pg.updatePixels()
        pg.endDraw()

        logger.debug("OUTER ${fw}x${fh} (content=${info.size.x}x${info.size.y}) spread=${info.spread}")
        return pg.get()
    }
}