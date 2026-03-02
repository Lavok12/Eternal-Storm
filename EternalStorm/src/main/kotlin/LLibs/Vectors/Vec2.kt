package la.vok.LavokLibrary.Vectors

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sqrt

data class Vec2(var x: Float = 0f, var y: Float = 0f) {

    companion object {
        val ZERO get() = Vec2(0f)
    }
    constructor(p: Vec2) : this(p.x, p.y)
    constructor(p: Float) : this(p, p)
    constructor(p: LPoint) : this(p.x.toFloat(), p.y.toFloat())

    // ===== Свойства-комбинации =====
    var xy: Vec2
        get() = Vec2(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var yx: Vec2
        get() = Vec2(y, x)
        set(value) {
            y = value.x
            x = value.y
        }

    var xx: Vec2
        get() = Vec2(x, x)
        set(value) {
            x = value.x
        }

    var yy: Vec2
        get() = Vec2(y, y)
        set(value) {
            y = value.x
        }

    // ===== Длина =====
    fun length(): Float = sqrt(x * x + y * y)

    fun lengthSquared(): Float = x * x + y * y

    // ===== Нормализация =====
    fun normalize(): Vec2 {
        val len = length()
        if (len != 0f) {
            x /= len
            y /= len
        }
        return this
    }

    fun normalized(): Vec2 {
        val len = length()
        return if (len != 0f) Vec2(x / len, y / len) else Vec2(0f, 0f)
    }

    // ===== Скалярное произведение =====
    infix fun dot(other: Vec2): Float = x * other.x + y * other.y

    // ===== Копия =====
    fun copy(): Vec2 = Vec2(x, y)

    // ===== Операторы =====
    operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
    operator fun times(other: Vec2): Vec2 = Vec2(x * other.x, y * other.y)
    operator fun div(other: Vec2): Vec2 = Vec2(x / other.x, y / other.y)
    operator fun plus(scalar: Float): Vec2 = Vec2(x + scalar, y + scalar)
    operator fun minus(scalar: Float): Vec2 = Vec2(x - scalar, y - scalar)
    operator fun times(scalar: Float): Vec2 = Vec2(x * scalar, y * scalar)
    operator fun div(scalar: Float): Vec2 = Vec2(x / scalar, y / scalar)

    // ===== Присваивающие операторы с числом =====
    operator fun plusAssign(scalar: Float) {
        x += scalar
        y += scalar
    }
    operator fun minusAssign(scalar: Float) {
        x -= scalar
        y -= scalar
    }

    operator fun unaryMinus(): Vec2 = Vec2(-x, -y)

    operator fun plusAssign(other: Vec2) {
        x += other.x
        y += other.y
    }


    operator fun minusAssign(other: Vec2) {
        x -= other.x
        y -= other.y
    }

    operator fun timesAssign(scalar: Float) {
        x *= scalar
        y *= scalar
    }

    operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
    }

    override fun equals(other: Any?): Boolean {
        return other is Vec2 && x == other.x && y == other.y
    }

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()

    override fun toString(): String = "Vec2(x=$x, y=$y)"

    fun toLPoint() : LPoint {
        return LPoint(x.toInt(), y.toInt())
    }

    fun anyGreater(other: Vec2) = x > other.x || y > other.y
    fun allGreater(other: Vec2) = x > other.x && y > other.y

    fun anyLess(other: Vec2) = x < other.x || y < other.y
    fun allLess(other: Vec2) = x < other.x && y < other.y

    fun anyEqual(other: Vec2) = x == other.x || y == other.y
    fun allEqual(other: Vec2) = x == other.x && y == other.y

    fun invert(xInv: Boolean, yInv: Boolean): Vec2 {
        if (xInv) x = -x
        if (yInv) y = -y
        return this
    }

    fun invertX()  = invert(true,  false)
    fun invertY()  = invert(false, true)
    fun invertXY() = invert(true,  true)
    fun invertAll() = invert(true, true)

    fun inverted(xInv: Boolean, yInv: Boolean): Vec2 =
        Vec2(
            if (xInv) -x else x,
            if (yInv) -y else y
        )

    fun invertedX()  = inverted(true,  false)
    fun invertedY()  = inverted(false, true)
    fun invertedXY() = inverted(true,  true)
    fun invertedAll() = inverted(true, true)

    fun invert() = invert(true, true)
    fun inverted() = inverted(true, true)

    fun half(): Vec2 {
        x *= 0.5f
        y *= 0.5f
        return this
    }

    fun halved(): Vec2 =
        Vec2(
            x * 0.5f,
            y * 0.5f
        )

    fun double(): Vec2 {
        x *= 2f
        y *= 2f
        return this
    }

    fun doubled(): Vec2 =
        Vec2(x * 2f, y * 2f)

    fun abs(): Vec2 {
        if (x < 0f) x = -x
        if (y < 0f) y = -y
        return this
    }

    fun absed(): Vec2 =
        Vec2(
            kotlin.math.abs(x),
            kotlin.math.abs(y)
        )

    fun clamp(min: Vec2, max: Vec2): Vec2 {
        x = x.coerceIn(min.x, max.x)
        y = y.coerceIn(min.y, max.y)
        return this
    }

    fun clamped(min: Vec2, max: Vec2): Vec2 =
        Vec2(
            x.coerceIn(min.x, max.x),
            y.coerceIn(min.y, max.y)
        )

    fun absDiff(other: Vec2): Vec2 =
        Vec2(
            kotlin.math.abs(x - other.x),
            kotlin.math.abs(y - other.y)
        )

    fun round(): Vec2 {
        x = round(x)
        y = round(y)
        return this
    }

    fun rounded(): Vec2 =
        Vec2(
            round(x),
            round(y)
        )
    fun floor(): Vec2 {
        x = floor(x)
        y = floor(y)
        return this
    }

    fun floored(): Vec2 =
        Vec2(
            floor(x),
            floor(y)
        )

    fun ceil(): Vec2 {
        x = ceil(x)
        y = ceil(y)
        return this
    }

    fun ceiled(): Vec2 =
        Vec2(
            ceil(x),
            ceil(y)
        )

    fun trunc(): Vec2 {
        x = x.toInt().toFloat()
        y = y.toInt().toFloat()
        return this
    }

    fun truncated(): Vec2 =
        Vec2(
            x.toInt().toFloat(),
            y.toInt().toFloat()
        )

    fun sign(): Vec2 {
        x = kotlin.math.sign(x)
        y = kotlin.math.sign(y)
        return this
    }

    fun signed(): Vec2 =
        Vec2(
            kotlin.math.sign(x),
            kotlin.math.sign(y)
        )

    fun snap(step: Vec2): Vec2 {
        x = kotlin.math.floor(x / step.x) * step.x
        y = kotlin.math.floor(y / step.y) * step.y
        return this
    }

    fun snapped(step: Vec2): Vec2 =
        Vec2(
            kotlin.math.floor(x / step.x) * step.x,
            kotlin.math.floor(y / step.y) * step.y
        )

    fun min(other: Vec2): Vec2 {
        x = kotlin.math.min(x, other.x)
        y = kotlin.math.min(y, other.y)
        return this
    }

    fun minned(other: Vec2): Vec2 =
        Vec2(
            kotlin.math.min(x, other.x),
            kotlin.math.min(y, other.y)
        )

    fun max(other: Vec2): Vec2 {
        x = kotlin.math.max(x, other.x)
        y = kotlin.math.max(y, other.y)
        return this
    }

    fun maxed(other: Vec2): Vec2 =
        Vec2(
            kotlin.math.max(x, other.x),
            kotlin.math.max(y, other.y)
        )

    fun lerp(to: Vec2, t: Float): Vec2 {
        x += (to.x - x) * t
        y += (to.y - y) * t
        return this
    }

    fun lerped(to: Vec2, t: Float): Vec2 =
        Vec2(
            x + (to.x - x) * t,
            y + (to.y - y) * t
        )

    fun toColor() : LColor {
        return LColor(this)
    }

    fun wrapped(size: Vec2): Vec2 =
        Vec2(
            ((x % size.x) + size.x) % size.x,
            ((y % size.y) + size.y) % size.y
        )

    fun wrap(size: Vec2): Vec2 {
        x = ((x % size.x) + size.x) % size.x
        y = ((y % size.y) + size.y) % size.y
        return this
    }

    fun angle(): Float = kotlin.math.atan2(y.toDouble(), x.toDouble()).toFloat()

    fun angleDeg(): Float = Math.toDegrees(angle().toDouble()).toFloat()

    fun fromAngle(angle: Float, length: Float = 1f): Vec2 {
        this.x = kotlin.math.cos(angle.toDouble()).toFloat() * length
        this.y = kotlin.math.sin(angle.toDouble()).toFloat() * length
        return this
    }
}