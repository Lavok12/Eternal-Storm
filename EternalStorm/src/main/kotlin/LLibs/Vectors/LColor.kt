package la.vok.LavokLibrary.Vectors

import la.vok.State.AppState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class LColor(var v: Vec4) {

    companion object {
        fun fromHSV(
            h: Float,     // 0..360
            s: Float,     // 0..1
            v: Float,     // 0..1
            a: Float = 255f // 0..255
        ): LColor {
            val hh = ((h % 360f) + 360f) % 360f  // Убедимся, что угол находится в пределах от 0 до 360
            val ss = s.coerceIn(0f, 1f)  // Проверяем насыщенность
            val vv = v.coerceIn(0f, 1f)  // Проверяем яркость

            val c = vv * ss  // Цветовой компонент
            val x = c * (1f - abs((hh / 60f) % 2f - 1f))  // Интермедиарный компонент
            val m = vv - c  // Смещение, для корректного добавления черного

            // В зависимости от угла (hh), присваиваем значения для r, g, b
            val (r1, g1, b1) = when {
                hh < 60f  -> Triple(c, x, 0f)
                hh < 120f -> Triple(x, c, 0f)
                hh < 180f -> Triple(0f, c, x)
                hh < 240f -> Triple(0f, x, c)
                hh < 300f -> Triple(x, 0f, c)
                else      -> Triple(c, 0f, x)
            }

            // Возвращаем цвет с альфа-каналом, который координируется в диапазоне от 0 до 255
            return LColor(
                (r1 + m) * 255f,  // Красный компонент
                (g1 + m) * 255f,  // Зеленый компонент
                (b1 + m) * 255f,  // Синий компонент
                a.coerceIn(0f, 255f)  // Альфа-канал, с ограничением от 0 до 255
            )
        }

        fun fromHex(hex: String): LColor {
            val h = hex.removePrefix("#")

            return when (h.length) {
                6 -> {
                    val r = h.substring(0, 2).toInt(16)
                    val g = h.substring(2, 4).toInt(16)
                    val b = h.substring(4, 6).toInt(16)
                    LColor(r.toFloat(), g.toFloat(), b.toFloat(), 255f)
                }
                8 -> {
                    val r = h.substring(0, 2).toInt(16)
                    val g = h.substring(2, 4).toInt(16)
                    val b = h.substring(4, 6).toInt(16)
                    val a = h.substring(6, 8).toInt(16)
                    LColor(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
                }
                else -> error("Invalid hex color: $hex")
            }
        }

        val ZERO: LColor = LColor(0f,0f,0f,0f)
        // ===== БАЗОВЫЕ =====
        val WHITE        = LColor(255f, 255f, 255f, 255f)
        val BLACK        = LColor(0f,   0f,   0f,   255f)
        val TRANSPARENT  = LColor(0f,   0f,   0f,   0f)

        // ===== ОТТЕНКИ СЕРОГО =====
        val GRAY         = LColor(128f, 128f, 128f)
        val LIGHT_GRAY   = LColor(192f, 192f, 192f)
        val DARK_GRAY    = LColor(64f,  64f,  64f)

        // ===== RGB =====
        val RED          = LColor(255f, 0f,   0f)
        val GREEN        = LColor(0f,   255f, 0f)
        val BLUE         = LColor(0f,   0f,   255f)

        val YELLOW       = LColor(255f, 255f, 0f)
        val CYAN         = LColor(0f,   255f, 255f)
        val MAGENTA      = LColor(255f, 0f,   255f)

        // ===== ДОП =====
        val ORANGE       = LColor(255f, 165f, 0f)
        val PURPLE       = LColor(128f, 0f,   128f)
        val BROWN        = LColor(165f, 42f,  42f)
        val PINK         = LColor(255f, 105f, 180f)

        // ===== НОЧНЫЕ =====
        val NIGHT_1      = LColor(20f, 20f, 25f)
        val NIGHT_2      = LColor(35f, 35f, 40f)
        val NIGHT_3      = LColor(60f, 60f, 70f)

        // ===== UI =====
        val UI_BLUE        = LColor(33f, 150f, 243f)
        val UI_BLUE_DARK   = LColor(25f, 118f, 210f)

        val UI_GREEN       = LColor(76f, 175f, 80f)
        val UI_GREEN_DARK  = LColor(56f, 142f, 60f)

        val UI_RED         = LColor(244f, 67f, 54f)
        val UI_RED_DARK    = LColor(211f, 47f, 47f)

        val UI_YELLOW      = LColor(255f, 235f, 59f)
        val UI_YELLOW_DARK = LColor(251f, 192f, 45f)

        // ===== ПАСТЕЛЬ =====
        val PASTEL_BLUE    = LColor(137f, 207f, 240f)
        val PASTEL_GREEN  = LColor(119f, 221f, 119f)
        val PASTEL_PINK   = LColor(255f, 179f, 186f)
        val PASTEL_PURPLE = LColor(202f, 171f, 255f)

        // ===== ФАБРИКИ =====
        fun rgb(r: Int, g: Int, b: Int, a: Int = 255): LColor =
            LColor(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())

        fun gray(v: Int, a: Int = 255): LColor =
            LColor(v.toFloat(), v.toFloat(), v.toFloat(), a.toFloat())

        // ===== LERP =====
        fun lerp(from: LColor, to: LColor, t: Float): LColor {
            val tt = t.coerceIn(0f, 1f)

            fun c(a: Float, b: Float): Float =
                (a + (b - a) * tt).coerceIn(0f, 255f)

            return LColor(
                c(from.r, to.r),
                c(from.g, to.g),
                c(from.b, to.b),
                c(from.a, to.a)
            )
        }
    }

    // ===== Конструкторы (сигнатура сохранена) =====

    constructor(r: Float, g: Float, b: Float, a: Float = 255f)
            : this(Vec4(r, g, b, a))

    constructor(f1: Float, f2: Float = 255f)
            : this(Vec4(f1, f1, f1, f2))

    constructor(v: Vec3, a: Float = 255f) : this(Vec4(v.x, v.y, v.z, a))
    constructor(v: Vec2) : this(Vec4(v.x, v.x, v.x, v.y))
    constructor(v: Vec1, a: Float = 255f) : this(Vec4(v.x, v.x, v.x, a))


    fun toHex(withAlpha: Boolean = true, prefix: String = "#"): String {
        fun c(v: Float) = v.roundToInt().coerceIn(0, 255)

        return if (withAlpha) {
            "%s%02X%02X%02X%02X".format(prefix, c(r), c(g), c(b), c(a))
        } else {
            "%s%02X%02X%02X".format(prefix, c(r), c(g), c(b))
        }
    }

    fun toHSV(): Vec3 {
        val rr = (r / 255f).coerceIn(0f, 1f)
        val gg = (g / 255f).coerceIn(0f, 1f)
        val bb = (b / 255f).coerceIn(0f, 1f)

        val max = max(rr, max(gg, bb))
        val min = min(rr, min(gg, bb))
        val delta = max - min

        val h = when {
            delta == 0f -> 0f
            max == rr   -> 60f * (((gg - bb) / delta) % 6f)
            max == gg   -> 60f * (((bb - rr) / delta) + 2f)
            else        -> 60f * (((rr - gg) / delta) + 4f)
        }.let { if (it < 0f) it + 360f else it }

        val s = if (max == 0f) 0f else delta / max
        val v = max

        return Vec3(h, s, v)
    }

    // ===== Делегированные компоненты =====

    val rgb: LColor get() = LColor(r, g, b)

    val r: Float get() = v.x
    val g: Float get() = v.y
    val b: Float get() = v.z
    val a: Float get() = v.w

    // ===== Операторы =====

    operator fun plus(other: LColor): LColor =
        LColor(
            (r + other.r).coerceIn(0f, 255f),
            (g + other.g).coerceIn(0f, 255f),
            (b + other.b).coerceIn(0f, 255f),
            (a + other.a).coerceIn(0f, 255f)
        )

    operator fun minus(other: LColor): LColor =
        LColor(
            (r - other.r).coerceIn(0f, 255f),
            (g - other.g).coerceIn(0f, 255f),
            (b - other.b).coerceIn(0f, 255f),
            (a - other.a).coerceIn(0f, 255f)
        )

    operator fun times(factor: Float): LColor =
        LColor(
            (r * factor).coerceIn(0f, 255f),
            (g * factor).coerceIn(0f, 255f),
            (b * factor).coerceIn(0f, 255f),
            a
        )

    // ===== Цветовые операции =====

    fun toIntARGB(): Int =
        AppState.main.color(
            v.x,
            v.y,
            v.z,
            v.w
        )

    fun withAlpha(alpha: Float): LColor =
        LColor(r, g, b, alpha.coerceIn(0f, 255f))

    fun copyWith(
        r: Float = this.r,
        g: Float = this.g,
        b: Float = this.b,
        a: Float = this.a
    ): LColor = LColor(r, g, b, a)

    fun invert(): LColor =
        LColor(255f - r, 255f - g, 255f - b, a)

    fun brightness(): Float =
        r * 0.299f + g * 0.587f + b * 0.114f

    fun averageBrightness(): Float =
        (r + g + b) / 3f

    fun saturate(amount: Float): LColor {
        val gray = averageBrightness()
        return LColor(
            (gray + (r - gray) * amount).coerceIn(0f, 255f),
            (gray + (g - gray) * amount).coerceIn(0f, 255f),
            (gray + (b - gray) * amount).coerceIn(0f, 255f),
            a
        )
    }

    fun desaturate(amount: Float): LColor =
        saturate(1f - amount)

    // ===== Конверсии =====

    fun toVec4(): Vec4 = v.copy()
    fun toVec3(): Vec3 = Vec3(r, g, b)
    fun toVec2(): Vec2 = Vec2(averageBrightness(), a)
    fun toVec1(): Vec1 = Vec1(averageBrightness())
}
