package la.vok.LavokLibrary.Vectors

import kotlin.math.sqrt

data class LPoint(var x: Int = 0, var y: Int = 0) {

    constructor(p: LPoint) : this(p.x, p.y)
    constructor(p: Int) : this(p, p)

    companion object {
        val ZERO: LPoint = LPoint(0, 0)
    }
        // ===== Свойства-комбинации =====
    var xy: LPoint
        get() = LPoint(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var yx: LPoint
        get() = LPoint(y, x)
        set(value) {
            y = value.x
            x = value.y
        }

    var xx: LPoint
        get() = LPoint(x, x)
        set(value) {
            x = value.x
        }

    var yy: LPoint
        get() = LPoint(y, y)
        set(value) {
            y = value.x
        }

    // ===== Длина (модуль) - для целочисленных векторов обычно не float, но сохраняем для совместимости, если нужно точное значение.
    // Если требуется только сравнение длин, лучше использовать lengthSquared.
    fun length(): Double = sqrt((x * x + y * y).toDouble())

    fun lengthSquared(): Int = x * x + y * y

    // ===== Нормализация - для целочисленных векторов нормализация в большинстве случаев не имеет смысла, так как результат будет дробным.
    // Если же нужна нормализация, то результат будет Vec2 (Float-based).
    // Возвращаем Vec2, так как нормализованный вектор с целочисленными компонентами почти всегда будет (0,0) или (+/-1, 0), (0, +/-1)
    fun normalized(): Vec2 {
        val len = length().toFloat() // Преобразуем в Float для нормализации
        return if (len != 0f) Vec2(x / len, y / len) else Vec2(0f, 0f)
    }

    // ===== Скалярное произведение =====
    infix fun dot(other: LPoint): Int = x * other.x + y * other.y

    // ===== Копия =====
    fun copy(): LPoint = LPoint(x, y)

    // ===== Операторы =====
    operator fun plus(other: LPoint): LPoint = LPoint(x + other.x, y + other.y)
    operator fun minus(other: LPoint): LPoint = LPoint(x - other.x, y - other.y)
    operator fun times(other: LPoint): LPoint = LPoint(x * other.x, y * other.y)
    operator fun div(other: LPoint): LPoint = LPoint(x / other.x, y / other.y) // Целочисленное деление
    operator fun plus(scalar: Int): LPoint = LPoint(x + scalar, y + scalar)
    operator fun minus(scalar: Int): LPoint = LPoint(x - scalar, y - scalar)
    operator fun times(scalar: Int): LPoint = LPoint(x * scalar, y * scalar)
    operator fun div(scalar: Int): LPoint = LPoint(x / scalar, y / scalar) // Целочисленное деление

    // ===== Присваивающие операторы с числом =====
    operator fun plusAssign(scalar: Int) {
        x += scalar
        y += scalar
    }
    operator fun minusAssign(scalar: Int) {
        x -= scalar
        y -= scalar
    }
    operator fun timesAssign(scalar: Int) {
        x *= scalar
        y *= scalar
    }
    operator fun divAssign(scalar: Int) {
        x /= scalar
        y /= scalar
    }

    operator fun unaryMinus(): LPoint = LPoint(-x, -y)

    operator fun plusAssign(other: LPoint) {
        x += other.x
        y += other.y
    }

    operator fun minusAssign(other: LPoint) {
        x -= other.x
        y -= other.y
    }

    override fun equals(other: Any?): Boolean {
        return other is LPoint && x == other.x && y == other.y
    }

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()

    override fun toString(): String = "LPoint(x=$x, y=$y)"

    fun toVec() : Vec2 {
        return Vec2(x.toFloat(), y.toFloat())
    }

    fun anyGreater(other: LPoint) =
        this.x > other.x || this.y > other.y

    fun allGreater(other: LPoint) =
        this.x > other.x && this.y > other.y

    fun anyLess(other: LPoint) =
        this.x < other.x || this.y < other.y

    fun allLess(other: LPoint) =
        this.x < other.x && this.y < other.y

    fun anyEqual(other: LPoint) = x == other.x || y == other.y
    fun allEqual(other: LPoint) = x == other.x && y == other.y

    fun invert(xInv: Boolean, yInv: Boolean): LPoint {
        if (xInv) x = -x
        if (yInv) y = -y
        return this
    }

    fun invertX()   = invert(true,  false)
    fun invertY()   = invert(false, true)
    fun invertXY()  = invert(true,  true)
    fun invertAll() = invert(true,  true)

    fun inverted(xInv: Boolean, yInv: Boolean): LPoint =
        LPoint(
            if (xInv) -x else x,
            if (yInv) -y else y
        )

    fun invertedX()   = inverted(true,  false)
    fun invertedY()   = inverted(false, true)
    fun invertedXY()  = inverted(true,  true)
    fun invertedAll() = inverted(true,  true)

    fun half(): LPoint {
        x /= 2
        y /= 2
        return this
    }

    fun halved(): LPoint =
        LPoint(x / 2, y / 2)

    fun double(): LPoint {
        x *= 2
        y *= 2
        return this
    }

    fun doubled(): LPoint =
        LPoint(x * 2, y * 2)

    fun abs(): LPoint {
        if (x < 0) x = -x
        if (y < 0) y = -y
        return this
    }

    fun absed(): LPoint =
        LPoint(
            kotlin.math.abs(x),
            kotlin.math.abs(y)
        )

    fun clamp(min: LPoint, max: LPoint): LPoint {
        x = x.coerceIn(min.x, max.x)
        y = y.coerceIn(min.y, max.y)
        return this
    }

    fun clamped(min: LPoint, max: LPoint): LPoint =
        LPoint(
            x.coerceIn(min.x, max.x),
            y.coerceIn(min.y, max.y)
        )

    fun absDiff(other: LPoint): LPoint =
        LPoint(
            kotlin.math.abs(x - other.x),
            kotlin.math.abs(y - other.y)
        )

    fun wrapped(size: LPoint): LPoint =
        LPoint(
            Math.floorMod(x, size.x),
            Math.floorMod(y, size.y)
        )

    fun wrap(size: LPoint): LPoint {
        x = Math.floorMod(x, size.x)
        y = Math.floorMod(y, size.y)
        return this
    }

}