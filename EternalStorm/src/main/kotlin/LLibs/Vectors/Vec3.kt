package la.vok.LavokLibrary.Vectors

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sqrt

data class Vec3(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    constructor(p: Vec3) : this(p.x, p.y, p.z)
    constructor(p: Float) : this(p, p, p)
    constructor(p: Float, p2: Vec2) : this(p, p2.x, p2.y)
    constructor(p2: Vec2, p: Float) : this(p2.x, p2.y, p)

    companion object {
        val ZERO: Vec3 = Vec3(0f)
    }
    // ===== Комбинированные свойства =====
    var xy: Vec2
        get() = Vec2(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var xz: Vec2
        get() = Vec2(x, z)
        set(value) {
            x = value.x
            z = value.y
        }

    var yz: Vec2
        get() = Vec2(y, z)
        set(value) {
            y = value.x
            z = value.y
        }

    var yx: Vec2
        get() = Vec2(y, x)
        set(value) {
            y = value.x
            x = value.y
        }

    var zx: Vec2
        get() = Vec2(z, x)
        set(value) {
            z = value.x
            x = value.y
        }

    var zy: Vec2
        get() = Vec2(z, y)
        set(value) {
            z = value.x
            y = value.y
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

    var zz: Vec2
        get() = Vec2(z, z)
        set(value) {
            z = value.x
        }

    // ===== Комбинированные 3-компонентные свойства =====
    var xyz: Vec3
        get() = Vec3(x, y, z)
        set(value) {
            x = value.x
            y = value.y
            z = value.z
        }

    var xzy: Vec3
        get() = Vec3(x, z, y)
        set(value) {
            x = value.x
            z = value.y
            y = value.z
        }

    var yxz: Vec3
        get() = Vec3(y, x, z)
        set(value) {
            y = value.x
            x = value.y
            z = value.z
        }

    var yzx: Vec3
        get() = Vec3(y, z, x)
        set(value) {
            y = value.x
            z = value.y
            x = value.z
        }

    var zxy: Vec3
        get() = Vec3(z, x, y)
        set(value) {
            z = value.x
            x = value.y
            y = value.z
        }

    var zyx: Vec3
        get() = Vec3(z, y, x)
        set(value) {
            z = value.x
            y = value.y
            x = value.z
        }

    // ===== Повторы одного компонента =====
    var xxx: Vec3
        get() = Vec3(x, x, x)
        set(value) {
            x = value.x
        }

    var yyy: Vec3
        get() = Vec3(y, y, y)
        set(value) {
            y = value.x
        }

    var zzz: Vec3
        get() = Vec3(z, z, z)
        set(value) {
            z = value.x
        }

    // ===== Длина =====
    fun length(): Float = sqrt(x * x + y * y + z * z)

    fun lengthSquared(): Float = x * x + y * y + z * z

    // ===== Нормализация =====
    fun normalize(): Vec3 {
        val len = length()
        if (len != 0f) {
            x /= len
            y /= len
            z /= len
        }
        return this
    }

    fun normalized(): Vec3 {
        val len = length()
        return if (len != 0f) Vec3(x / len, y / len, z / len) else Vec3(0f)
    }

    // ===== Скалярное произведение =====
    infix fun dot(other: Vec3): Float = x * other.x + y * other.y + z * other.z

    // ===== Копия =====
    fun copy(): Vec3 = Vec3(x, y, z)

    // ===== Операторы =====
    operator fun plus(other: Vec3): Vec3 = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3): Vec3 = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Vec3): Vec3 = Vec3(x * other.x, y * other.y, z * other.z)
    operator fun div(other: Vec3): Vec3 = Vec3(x / other.x, y / other.y, z / other.z)
    operator fun plus(scalar: Float): Vec3 = Vec3(x + scalar, y + scalar, z + scalar)
    operator fun minus(scalar: Float): Vec3 = Vec3(x - scalar, y - scalar, z - scalar)
    operator fun times(scalar: Float): Vec3 = Vec3(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Float): Vec3 = Vec3(x / scalar, y / scalar, z / scalar)

    operator fun unaryMinus(): Vec3 = Vec3(-x, -y, -z)

    operator fun plusAssign(other: Vec3) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun minusAssign(other: Vec3) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    operator fun timesAssign(scalar: Float) {
        x *= scalar
        y *= scalar
        z *= scalar
    }

    operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
        z /= scalar
    }

    operator fun plusAssign(scalar: Float) {
        x += scalar
        y += scalar
        z += scalar
    }

    operator fun minusAssign(scalar: Float) {
        x -= scalar
        y -= scalar
        z -= scalar
    }


    override fun equals(other: Any?): Boolean {
        return other is Vec3 && x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode() + z.hashCode()

    override fun toString(): String = "Vec3(x=$x, y=$y, z=$z)"


    fun anyGreater(other: Vec3) =
        x > other.x || y > other.y || z > other.z
    fun allGreater(other: Vec3) =
        x > other.x && y > other.y && z > other.z

    fun anyLess(other: Vec3) =
        x < other.x || y < other.y || z < other.z
    fun allLess(other: Vec3) =
        x < other.x && y < other.y && z < other.z

    fun anyEqual(other: Vec3) =
        x == other.x || y == other.y || z == other.z
    fun allEqual(other: Vec3) =
        x == other.x && y == other.y && z == other.z

    fun invert(xInv: Boolean, yInv: Boolean, zInv: Boolean): Vec3 {
        if (xInv) x = -x
        if (yInv) y = -y
        if (zInv) z = -z
        return this
    }

    fun invertX()  = invert(true,  false, false)
    fun invertY()  = invert(false, true,  false)
    fun invertZ()  = invert(false, false, true)
    fun invertXY() = invert(true,  true,  false)
    fun invertXZ() = invert(true,  false, true)
    fun invertYZ() = invert(false, true,  true)
    fun invertXYZ() = invert(true, true,  true)

    fun invertAll() = invert(true, true, true)

    fun inverted(xInv: Boolean, yInv: Boolean, zInv: Boolean): Vec3 =
        Vec3(
            if (xInv) -x else x,
            if (yInv) -y else y,
            if (zInv) -z else z
        )

    fun invertedX()  = inverted(true,  false, false)
    fun invertedY()  = inverted(false, true,  false)
    fun invertedZ()  = inverted(false, false, true)
    fun invertedXY() = inverted(true,  true,  false)
    fun invertedXZ() = inverted(true,  false, true)
    fun invertedYZ() = inverted(false, true,  true)
    fun invertedXYZ() = inverted(true, true,  true)
    fun invertedAll() = inverted(true, true, true)

    fun invert() = invert(true, true, true)
    fun inverted() = inverted(true, true, true)

    fun half(): Vec3 {
        x *= 0.5f
        y *= 0.5f
        z *= 0.5f
        return this
    }

    fun halved(): Vec3 =
        Vec3(
            x * 0.5f,
            y * 0.5f,
            z * 0.5f
        )

    fun double(): Vec3 {
        x *= 2f
        y *= 2f
        z *= 2f
        return this
    }

    fun doubled(): Vec3 =
        Vec3(x * 2f, y * 2f, z * 2f)

    fun abs(): Vec3 {
        if (x < 0f) x = -x
        if (y < 0f) y = -y
        if (z < 0f) z = -z
        return this
    }

    fun absed(): Vec3 =
        Vec3(
            kotlin.math.abs(x),
            kotlin.math.abs(y),
            kotlin.math.abs(z)
        )

    fun absDiff(other: Vec3): Vec3 =
        Vec3(
            kotlin.math.abs(x - other.x),
            kotlin.math.abs(y - other.y),
            kotlin.math.abs(z - other.z)
        )

    fun round(): Vec3 {
        x = round(x)
        y = round(y)
        z = round(z)
        return this
    }

    fun rounded(): Vec3 =
        Vec3(
            round(x),
            round(y),
            round(z)
        )

    fun floor(): Vec3 {
        x = floor(x)
        y = floor(y)
        z = floor(z)
        return this
    }

    fun floored(): Vec3 =
        Vec3(
            floor(x),
            floor(y),
            floor(z)
        )

    fun ceil(): Vec3 {
        x = ceil(x)
        y = ceil(y)
        z = ceil(z)
        return this
    }

    fun ceiled(): Vec3 =
        Vec3(
            ceil(x),
            ceil(y),
            ceil(z)
        )

    fun trunc(): Vec3 {
        x = x.toInt().toFloat()
        y = y.toInt().toFloat()
        z = z.toInt().toFloat()
        return this
    }

    fun truncated(): Vec3 =
        Vec3(
            x.toInt().toFloat(),
            y.toInt().toFloat(),
            z.toInt().toFloat()
        )

    fun sign(): Vec3 {
        x = kotlin.math.sign(x)
        y = kotlin.math.sign(y)
        z = kotlin.math.sign(z)
        return this
    }

    fun signed(): Vec3 =
        Vec3(
            kotlin.math.sign(x),
            kotlin.math.sign(y),
            kotlin.math.sign(z)
        )

    fun snap(step: Vec3): Vec3 {
        x = kotlin.math.floor(x / step.x) * step.x
        y = kotlin.math.floor(y / step.y) * step.y
        z = kotlin.math.floor(z / step.z) * step.z
        return this
    }

    fun snapped(step: Vec3): Vec3 =
        Vec3(
            kotlin.math.floor(x / step.x) * step.x,
            kotlin.math.floor(y / step.y) * step.y,
            kotlin.math.floor(z / step.z) * step.z
        )

    fun min(other: Vec3): Vec3 {
        x = kotlin.math.min(x, other.x)
        y = kotlin.math.min(y, other.y)
        z = kotlin.math.min(z, other.z)
        return this
    }

    fun minned(other: Vec3): Vec3 =
        Vec3(
            kotlin.math.min(x, other.x),
            kotlin.math.min(y, other.y),
            kotlin.math.min(z, other.z)
        )

    fun max(other: Vec3): Vec3 {
        x = kotlin.math.max(x, other.x)
        y = kotlin.math.max(y, other.y)
        z = kotlin.math.max(z, other.z)
        return this
    }

    fun maxed(other: Vec3): Vec3 =
        Vec3(
            kotlin.math.max(x, other.x),
            kotlin.math.max(y, other.y),
            kotlin.math.max(z, other.z)
        )

    fun lerp(to: Vec3, t: Float): Vec3 {
        x += (to.x - x) * t
        y += (to.y - y) * t
        z += (to.z - z) * t
        return this
    }

    fun lerped(to: Vec3, t: Float): Vec3 =
        Vec3(
            x + (to.x - x) * t,
            y + (to.y - y) * t,
            z + (to.z - z) * t
        )

    fun toColor() : LColor {
        return LColor(this)
    }
}
