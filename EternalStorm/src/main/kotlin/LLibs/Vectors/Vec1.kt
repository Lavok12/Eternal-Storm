package la.vok.LavokLibrary.Vectors

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

data class Vec1(var x: Float = 0f) {
    constructor(v: Vec1) : this(v.x)

    companion object {
        val ZERO: Vec1 = Vec1(0f)
    }
    // ===== Длина =====
    fun length(): Float = kotlin.math.abs(x)
    fun lengthSquared(): Float = x * x

    // ===== Нормализация =====
    fun normalize(): Vec1 {
        val len = length()
        if (len != 0f) x /= len
        return this
    }

    fun normalized(): Vec1 {
        val len = length()
        return if (len != 0f) Vec1(x / len) else Vec1(0f)
    }

    // ===== Копия =====
    fun copy(): Vec1 = Vec1(x)

    // ===== Операторы =====
    operator fun plus(other: Vec1): Vec1 = Vec1(x + other.x)
    operator fun minus(other: Vec1): Vec1 = Vec1(x - other.x)
    operator fun times(other: Vec1): Vec1 = Vec1(x * other.x)
    operator fun div(other: Vec1): Vec1 = Vec1(x / other.x)

    operator fun plus(scalar: Float): Vec1 = Vec1(x + scalar)
    operator fun minus(scalar: Float): Vec1 = Vec1(x - scalar)
    operator fun times(scalar: Float): Vec1 = Vec1(x * scalar)
    operator fun div(scalar: Float): Vec1 = Vec1(x / scalar)

    operator fun unaryMinus(): Vec1 = Vec1(-x)

    // Присваивающие
    operator fun plusAssign(other: Vec1) { x += other.x }
    operator fun minusAssign(other: Vec1) { x -= other.x }
    operator fun plusAssign(scalar: Float) { x += scalar }
    operator fun minusAssign(scalar: Float) { x -= scalar }
    operator fun timesAssign(scalar: Float) { x *= scalar }
    operator fun divAssign(scalar: Float) { x /= scalar }

    // ===== Сравнения по компонентам =====
    fun anyGreater(other: Vec1) = x > other.x
    fun allGreater(other: Vec1) = x > other.x

    fun anyLess(other: Vec1) = x < other.x
    fun allLess(other: Vec1) = x < other.x

    fun anyEqual(other: Vec1) = x == other.x
    fun allEqual(other: Vec1) = x == other.x

    // ===== Скалярное =====
    infix fun dot(other: Vec1): Float = x * other.x

    fun invert(xInv: Boolean): Vec1 {
        if (xInv) x = -x
        return this
    }

    fun invertX() = invert(true)
    fun invertAll() = invert(true)

    fun inverted(xInv: Boolean): Vec1 =
        Vec1(if (xInv) -x else x)

    fun invertedX() = inverted(true)
    fun invertedAll() = inverted(true)

    fun invert() = invert(true)
    fun inverted() = inverted(true)

    override fun toString(): String = "Vec1(x=$x)"

    fun half(): Vec1 {
        x *= 0.5f
        return this
    }

    fun halved(): Vec1 =
        Vec1(x * 0.5f)

    fun double(): Vec1 {
        x *= 2f
        return this
    }

    fun doubled(): Vec1 =
        Vec1(x * 2f)

    fun abs(): Vec1 {
        if (x < 0f) x = -x
        return this
    }

    fun absed(): Vec1 =
        Vec1(kotlin.math.abs(x))

    fun absDiff(other: Vec1): Vec1 =
        Vec1(kotlin.math.abs(x - other.x))


    fun round(): Vec1 {
        x = round(x)
        return this
    }

    fun rounded(): Vec1 =
        Vec1(round(x))

    fun floor(): Vec1 {
        x = floor(x)
        return this
    }

    fun floored(): Vec1 =
        Vec1(floor(x))

    fun ceil(): Vec1 {
        x = ceil(x)
        return this
    }

    fun ceiled(): Vec1 =
        Vec1(ceil(x))

    fun trunc(): Vec1 {
        x = x.toInt().toFloat()
        return this
    }

    fun truncated(): Vec1 =
        Vec1(x.toInt().toFloat())

    fun sign(): Vec1 {
        x = kotlin.math.sign(x)
        return this
    }

    fun signed(): Vec1 =
        Vec1(kotlin.math.sign(x))

    fun snap(step: Float): Vec1 {
        x = kotlin.math.floor(x / step) * step
        return this
    }

    fun snapped(step: Float): Vec1 =
        Vec1(kotlin.math.floor(x / step) * step)

    fun min(other: Vec1): Vec1 {
        x = kotlin.math.min(x, other.x)
        return this
    }

    fun minned(other: Vec1): Vec1 =
        Vec1(kotlin.math.min(x, other.x))

    fun max(other: Vec1): Vec1 {
        x = kotlin.math.max(x, other.x)
        return this
    }

    fun maxed(other: Vec1): Vec1 =
        Vec1(kotlin.math.max(x, other.x))

    fun lerp(to: Vec1, t: Float): Vec1 {
        x += (to.x - x) * t
        return this
    }

    fun lerped(to: Vec1, t: Float): Vec1 =
        Vec1(x + (to.x - x) * t)

    fun toColor() : LColor {
        return LColor(this)
    }
}
