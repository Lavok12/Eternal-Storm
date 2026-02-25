package la.vok.LavokLibrary.Vectors

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sqrt

data class Vec4(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f, var w: Float = 0f) {

    // Полные копии
    constructor(p: Vec4) : this(p.x, p.y, p.z, p.w)
    constructor(p: Float) : this(p, p, p, p)
    constructor(p: Float, v: Vec3) : this(p, v.x, v.y, v.z)
    constructor(v: Vec3, p: Float) : this(v.x, v.y, v.z, p)
    constructor(v: Vec2, p: Vec2) : this (v.x, v.y, p.x, p.y)
    constructor(v1: Float, v2: Float, p: Vec2) : this (v1, v2, p.x, p.y)
    constructor(p: Vec2, v1: Float, v2: Float) : this (p.x, p.y, v1, v2)
    constructor(v1: Float, p: Vec2, v2: Float) : this (v1, p.x, p.y, v2)

    companion object {
        val ZERO: Vec4 = Vec4(0f)
    }

    var xw: Vec2
        get() = Vec2(x, w)
        set(value) {
            x = value.x
            w = value.y
        }

    var wx: Vec2
        get() = Vec2(w, x)
        set(value) {
            w = value.x
            x = value.y
        }

    var yw: Vec2
        get() = Vec2(y, w)
        set(value) {
            y = value.x
            w = value.y
        }

    var wy: Vec2
        get() = Vec2(w, y)
        set(value) {
            w = value.x
            y = value.y
        }

    var zw: Vec2
        get() = Vec2(z, w)
        set(value) {
            z = value.x
            w = value.y
        }

    var wz: Vec2
        get() = Vec2(w, z)
        set(value) {
            w = value.x
            z = value.y
        }

    var ww: Vec2
        get() = Vec2(w, w)
        set(value) {
            w = value.x
        }

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

    var xyw: Vec3
        get() = Vec3(x, y, w)
        set(value) {
            x = value.x
            y = value.y
            w = value.z
        }

    var xwy: Vec3
        get() = Vec3(x, w, y)
        set(value) {
            x = value.x
            w = value.y
            y = value.z
        }

    var yxw: Vec3
        get() = Vec3(y, x, w)
        set(value) {
            y = value.x
            x = value.y
            w = value.z
        }

    var ywx: Vec3
        get() = Vec3(y, w, x)
        set(value) {
            y = value.x
            w = value.y
            x = value.z
        }

    var wxy: Vec3
        get() = Vec3(w, x, y)
        set(value) {
            w = value.x
            x = value.y
            y = value.z
        }

    var wyx: Vec3
        get() = Vec3(w, y, x)
        set(value) {
            w = value.x
            y = value.y
            x = value.z
        }

    var xzw: Vec3
        get() = Vec3(x, z, w)
        set(value) {
            x = value.x
            z = value.y
            w = value.z
        }

    var xwz: Vec3
        get() = Vec3(x, w, z)
        set(value) {
            x = value.x
            w = value.y
            z = value.z
        }

    var zxw: Vec3
        get() = Vec3(z, x, w)
        set(value) {
            z = value.x
            x = value.y
            w = value.z
        }

    var zwx: Vec3
        get() = Vec3(z, w, x)
        set(value) {
            z = value.x
            w = value.y
            x = value.z
        }

    var wxz: Vec3
        get() = Vec3(w, x, z)
        set(value) {
            w = value.x
            x = value.y
            z = value.z
        }

    var wzx: Vec3
        get() = Vec3(w, z, x)
        set(value) {
            w = value.x
            z = value.y
            x = value.z
        }

    var www: Vec3
        get() = Vec3(w, w, w)
        set(value) {
            w = value.x
        }

    var xww: Vec3
        get() = Vec3(x, w, w)
        set(value) {
            x = value.x
            w = value.y
        }

    var wxw: Vec3
        get() = Vec3(w, x, w)
        set(value) {
            w = value.x
            x = value.y
        }

    var wwx: Vec3
        get() = Vec3(w, w, x)
        set(value) {
            w = value.x
            x = value.z
        }

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


    var xxxx: Vec4
        get() = Vec4(x, x, x, x)
        set(value) {
            x = value.x
            x = value.y
            x = value.z
            x = value.w
        }


    var xxxy: Vec4
        get() = Vec4(x, x, x, y)
        set(value) {
            x = value.x
            x = value.y
            x = value.z
            y = value.w
        }


    var xxxz: Vec4
        get() = Vec4(x, x, x, z)
        set(value) {
            x = value.x
            x = value.y
            x = value.z
            z = value.w
        }


    var xxxw: Vec4
        get() = Vec4(x, x, x, w)
        set(value) {
            x = value.x
            x = value.y
            x = value.z
            w = value.w
        }


    var xxyx: Vec4
        get() = Vec4(x, x, y, x)
        set(value) {
            x = value.x
            x = value.y
            y = value.z
            x = value.w
        }


    var xxyy: Vec4
        get() = Vec4(x, x, y, y)
        set(value) {
            x = value.x
            x = value.y
            y = value.z
            y = value.w
        }


    var xxyz: Vec4
        get() = Vec4(x, x, y, z)
        set(value) {
            x = value.x
            x = value.y
            y = value.z
            z = value.w
        }


    var xxyw: Vec4
        get() = Vec4(x, x, y, w)
        set(value) {
            x = value.x
            x = value.y
            y = value.z
            w = value.w
        }


    var xxzx: Vec4
        get() = Vec4(x, x, z, x)
        set(value) {
            x = value.x
            x = value.y
            z = value.z
            x = value.w
        }


    var xxzy: Vec4
        get() = Vec4(x, x, z, y)
        set(value) {
            x = value.x
            x = value.y
            z = value.z
            y = value.w
        }


    var xxzz: Vec4
        get() = Vec4(x, x, z, z)
        set(value) {
            x = value.x
            x = value.y
            z = value.z
            z = value.w
        }


    var xxzw: Vec4
        get() = Vec4(x, x, z, w)
        set(value) {
            x = value.x
            x = value.y
            z = value.z
            w = value.w
        }


    var xxwx: Vec4
        get() = Vec4(x, x, w, x)
        set(value) {
            x = value.x
            x = value.y
            w = value.z
            x = value.w
        }


    var xxwy: Vec4
        get() = Vec4(x, x, w, y)
        set(value) {
            x = value.x
            x = value.y
            w = value.z
            y = value.w
        }


    var xxwz: Vec4
        get() = Vec4(x, x, w, z)
        set(value) {
            x = value.x
            x = value.y
            w = value.z
            z = value.w
        }


    var xxww: Vec4
        get() = Vec4(x, x, w, w)
        set(value) {
            x = value.x
            x = value.y
            w = value.z
            w = value.w
        }


    var xyxx: Vec4
        get() = Vec4(x, y, x, x)
        set(value) {
            x = value.x
            y = value.y
            x = value.z
            x = value.w
        }


    var xyxy: Vec4
        get() = Vec4(x, y, x, y)
        set(value) {
            x = value.x
            y = value.y
            x = value.z
            y = value.w
        }


    var xyxz: Vec4
        get() = Vec4(x, y, x, z)
        set(value) {
            x = value.x
            y = value.y
            x = value.z
            z = value.w
        }


    var xyxw: Vec4
        get() = Vec4(x, y, x, w)
        set(value) {
            x = value.x
            y = value.y
            x = value.z
            w = value.w
        }


    var xyyx: Vec4
        get() = Vec4(x, y, y, x)
        set(value) {
            x = value.x
            y = value.y
            y = value.z
            x = value.w
        }


    var xyyy: Vec4
        get() = Vec4(x, y, y, y)
        set(value) {
            x = value.x
            y = value.y
            y = value.z
            y = value.w
        }


    var xyyz: Vec4
        get() = Vec4(x, y, y, z)
        set(value) {
            x = value.x
            y = value.y
            y = value.z
            z = value.w
        }


    var xyyw: Vec4
        get() = Vec4(x, y, y, w)
        set(value) {
            x = value.x
            y = value.y
            y = value.z
            w = value.w
        }


    var xyzx: Vec4
        get() = Vec4(x, y, z, x)
        set(value) {
            x = value.x
            y = value.y
            z = value.z
            x = value.w
        }


    var xyzy: Vec4
        get() = Vec4(x, y, z, y)
        set(value) {
            x = value.x
            y = value.y
            z = value.z
            y = value.w
        }


    var xyzz: Vec4
        get() = Vec4(x, y, z, z)
        set(value) {
            x = value.x
            y = value.y
            z = value.z
            z = value.w
        }


    var xyzw: Vec4
        get() = Vec4(x, y, z, w)
        set(value) {
            x = value.x
            y = value.y
            z = value.z
            w = value.w
        }


    var xywx: Vec4
        get() = Vec4(x, y, w, x)
        set(value) {
            x = value.x
            y = value.y
            w = value.z
            x = value.w
        }


    var xywy: Vec4
        get() = Vec4(x, y, w, y)
        set(value) {
            x = value.x
            y = value.y
            w = value.z
            y = value.w
        }


    var xywz: Vec4
        get() = Vec4(x, y, w, z)
        set(value) {
            x = value.x
            y = value.y
            w = value.z
            z = value.w
        }


    var xyww: Vec4
        get() = Vec4(x, y, w, w)
        set(value) {
            x = value.x
            y = value.y
            w = value.z
            w = value.w
        }


    var xzxx: Vec4
        get() = Vec4(x, z, x, x)
        set(value) {
            x = value.x
            z = value.y
            x = value.z
            x = value.w
        }


    var xzxy: Vec4
        get() = Vec4(x, z, x, y)
        set(value) {
            x = value.x
            z = value.y
            x = value.z
            y = value.w
        }


    var xzxz: Vec4
        get() = Vec4(x, z, x, z)
        set(value) {
            x = value.x
            z = value.y
            x = value.z
            z = value.w
        }


    var xzxw: Vec4
        get() = Vec4(x, z, x, w)
        set(value) {
            x = value.x
            z = value.y
            x = value.z
            w = value.w
        }


    var xzyx: Vec4
        get() = Vec4(x, z, y, x)
        set(value) {
            x = value.x
            z = value.y
            y = value.z
            x = value.w
        }


    var xzyy: Vec4
        get() = Vec4(x, z, y, y)
        set(value) {
            x = value.x
            z = value.y
            y = value.z
            y = value.w
        }


    var xzyz: Vec4
        get() = Vec4(x, z, y, z)
        set(value) {
            x = value.x
            z = value.y
            y = value.z
            z = value.w
        }


    var xzyw: Vec4
        get() = Vec4(x, z, y, w)
        set(value) {
            x = value.x
            z = value.y
            y = value.z
            w = value.w
        }


    var xzzx: Vec4
        get() = Vec4(x, z, z, x)
        set(value) {
            x = value.x
            z = value.y
            z = value.z
            x = value.w
        }


    var xzzy: Vec4
        get() = Vec4(x, z, z, y)
        set(value) {
            x = value.x
            z = value.y
            z = value.z
            y = value.w
        }


    var xzzz: Vec4
        get() = Vec4(x, z, z, z)
        set(value) {
            x = value.x
            z = value.y
            z = value.z
            z = value.w
        }


    var xzzw: Vec4
        get() = Vec4(x, z, z, w)
        set(value) {
            x = value.x
            z = value.y
            z = value.z
            w = value.w
        }


    var xzwx: Vec4
        get() = Vec4(x, z, w, x)
        set(value) {
            x = value.x
            z = value.y
            w = value.z
            x = value.w
        }


    var xzwy: Vec4
        get() = Vec4(x, z, w, y)
        set(value) {
            x = value.x
            z = value.y
            w = value.z
            y = value.w
        }


    var xzwz: Vec4
        get() = Vec4(x, z, w, z)
        set(value) {
            x = value.x
            z = value.y
            w = value.z
            z = value.w
        }


    var xzww: Vec4
        get() = Vec4(x, z, w, w)
        set(value) {
            x = value.x
            z = value.y
            w = value.z
            w = value.w
        }


    var xwxx: Vec4
        get() = Vec4(x, w, x, x)
        set(value) {
            x = value.x
            w = value.y
            x = value.z
            x = value.w
        }


    var xwxy: Vec4
        get() = Vec4(x, w, x, y)
        set(value) {
            x = value.x
            w = value.y
            x = value.z
            y = value.w
        }


    var xwxz: Vec4
        get() = Vec4(x, w, x, z)
        set(value) {
            x = value.x
            w = value.y
            x = value.z
            z = value.w
        }


    var xwxw: Vec4
        get() = Vec4(x, w, x, w)
        set(value) {
            x = value.x
            w = value.y
            x = value.z
            w = value.w
        }


    var xwyx: Vec4
        get() = Vec4(x, w, y, x)
        set(value) {
            x = value.x
            w = value.y
            y = value.z
            x = value.w
        }


    var xwyy: Vec4
        get() = Vec4(x, w, y, y)
        set(value) {
            x = value.x
            w = value.y
            y = value.z
            y = value.w
        }


    var xwyz: Vec4
        get() = Vec4(x, w, y, z)
        set(value) {
            x = value.x
            w = value.y
            y = value.z
            z = value.w
        }


    var xwyw: Vec4
        get() = Vec4(x, w, y, w)
        set(value) {
            x = value.x
            w = value.y
            y = value.z
            w = value.w
        }


    var xwzx: Vec4
        get() = Vec4(x, w, z, x)
        set(value) {
            x = value.x
            w = value.y
            z = value.z
            x = value.w
        }


    var xwzy: Vec4
        get() = Vec4(x, w, z, y)
        set(value) {
            x = value.x
            w = value.y
            z = value.z
            y = value.w
        }


    var xwzz: Vec4
        get() = Vec4(x, w, z, z)
        set(value) {
            x = value.x
            w = value.y
            z = value.z
            z = value.w
        }


    var xwzw: Vec4
        get() = Vec4(x, w, z, w)
        set(value) {
            x = value.x
            w = value.y
            z = value.z
            w = value.w
        }


    var xwwx: Vec4
        get() = Vec4(x, w, w, x)
        set(value) {
            x = value.x
            w = value.y
            w = value.z
            x = value.w
        }


    var xwwy: Vec4
        get() = Vec4(x, w, w, y)
        set(value) {
            x = value.x
            w = value.y
            w = value.z
            y = value.w
        }


    var xwwz: Vec4
        get() = Vec4(x, w, w, z)
        set(value) {
            x = value.x
            w = value.y
            w = value.z
            z = value.w
        }


    var xwww: Vec4
        get() = Vec4(x, w, w, w)
        set(value) {
            x = value.x
            w = value.y
            w = value.z
            w = value.w
        }


    var yxxx: Vec4
        get() = Vec4(y, x, x, x)
        set(value) {
            y = value.x
            x = value.y
            x = value.z
            x = value.w
        }


    var yxxy: Vec4
        get() = Vec4(y, x, x, y)
        set(value) {
            y = value.x
            x = value.y
            x = value.z
            y = value.w
        }


    var yxxz: Vec4
        get() = Vec4(y, x, x, z)
        set(value) {
            y = value.x
            x = value.y
            x = value.z
            z = value.w
        }


    var yxxw: Vec4
        get() = Vec4(y, x, x, w)
        set(value) {
            y = value.x
            x = value.y
            x = value.z
            w = value.w
        }


    var yxyx: Vec4
        get() = Vec4(y, x, y, x)
        set(value) {
            y = value.x
            x = value.y
            y = value.z
            x = value.w
        }


    var yxyy: Vec4
        get() = Vec4(y, x, y, y)
        set(value) {
            y = value.x
            x = value.y
            y = value.z
            y = value.w
        }


    var yxyz: Vec4
        get() = Vec4(y, x, y, z)
        set(value) {
            y = value.x
            x = value.y
            y = value.z
            z = value.w
        }


    var yxyw: Vec4
        get() = Vec4(y, x, y, w)
        set(value) {
            y = value.x
            x = value.y
            y = value.z
            w = value.w
        }


    var yxzx: Vec4
        get() = Vec4(y, x, z, x)
        set(value) {
            y = value.x
            x = value.y
            z = value.z
            x = value.w
        }


    var yxzy: Vec4
        get() = Vec4(y, x, z, y)
        set(value) {
            y = value.x
            x = value.y
            z = value.z
            y = value.w
        }


    var yxzz: Vec4
        get() = Vec4(y, x, z, z)
        set(value) {
            y = value.x
            x = value.y
            z = value.z
            z = value.w
        }


    var yxzw: Vec4
        get() = Vec4(y, x, z, w)
        set(value) {
            y = value.x
            x = value.y
            z = value.z
            w = value.w
        }


    var yxwx: Vec4
        get() = Vec4(y, x, w, x)
        set(value) {
            y = value.x
            x = value.y
            w = value.z
            x = value.w
        }


    var yxwy: Vec4
        get() = Vec4(y, x, w, y)
        set(value) {
            y = value.x
            x = value.y
            w = value.z
            y = value.w
        }


    var yxwz: Vec4
        get() = Vec4(y, x, w, z)
        set(value) {
            y = value.x
            x = value.y
            w = value.z
            z = value.w
        }


    var yxww: Vec4
        get() = Vec4(y, x, w, w)
        set(value) {
            y = value.x
            x = value.y
            w = value.z
            w = value.w
        }


    var yyxx: Vec4
        get() = Vec4(y, y, x, x)
        set(value) {
            y = value.x
            y = value.y
            x = value.z
            x = value.w
        }


    var yyxy: Vec4
        get() = Vec4(y, y, x, y)
        set(value) {
            y = value.x
            y = value.y
            x = value.z
            y = value.w
        }


    var yyxz: Vec4
        get() = Vec4(y, y, x, z)
        set(value) {
            y = value.x
            y = value.y
            x = value.z
            z = value.w
        }


    var yyxw: Vec4
        get() = Vec4(y, y, x, w)
        set(value) {
            y = value.x
            y = value.y
            x = value.z
            w = value.w
        }


    var yyyx: Vec4
        get() = Vec4(y, y, y, x)
        set(value) {
            y = value.x
            y = value.y
            y = value.z
            x = value.w
        }


    var yyyy: Vec4
        get() = Vec4(y, y, y, y)
        set(value) {
            y = value.x
            y = value.y
            y = value.z
            y = value.w
        }


    var yyyz: Vec4
        get() = Vec4(y, y, y, z)
        set(value) {
            y = value.x
            y = value.y
            y = value.z
            z = value.w
        }


    var yyyw: Vec4
        get() = Vec4(y, y, y, w)
        set(value) {
            y = value.x
            y = value.y
            y = value.z
            w = value.w
        }


    var yyzx: Vec4
        get() = Vec4(y, y, z, x)
        set(value) {
            y = value.x
            y = value.y
            z = value.z
            x = value.w
        }


    var yyzy: Vec4
        get() = Vec4(y, y, z, y)
        set(value) {
            y = value.x
            y = value.y
            z = value.z
            y = value.w
        }


    var yyzz: Vec4
        get() = Vec4(y, y, z, z)
        set(value) {
            y = value.x
            y = value.y
            z = value.z
            z = value.w
        }


    var yyzw: Vec4
        get() = Vec4(y, y, z, w)
        set(value) {
            y = value.x
            y = value.y
            z = value.z
            w = value.w
        }


    var yywx: Vec4
        get() = Vec4(y, y, w, x)
        set(value) {
            y = value.x
            y = value.y
            w = value.z
            x = value.w
        }


    var yywy: Vec4
        get() = Vec4(y, y, w, y)
        set(value) {
            y = value.x
            y = value.y
            w = value.z
            y = value.w
        }


    var yywz: Vec4
        get() = Vec4(y, y, w, z)
        set(value) {
            y = value.x
            y = value.y
            w = value.z
            z = value.w
        }


    var yyww: Vec4
        get() = Vec4(y, y, w, w)
        set(value) {
            y = value.x
            y = value.y
            w = value.z
            w = value.w
        }


    var yzxx: Vec4
        get() = Vec4(y, z, x, x)
        set(value) {
            y = value.x
            z = value.y
            x = value.z
            x = value.w
        }


    var yzxy: Vec4
        get() = Vec4(y, z, x, y)
        set(value) {
            y = value.x
            z = value.y
            x = value.z
            y = value.w
        }


    var yzxz: Vec4
        get() = Vec4(y, z, x, z)
        set(value) {
            y = value.x
            z = value.y
            x = value.z
            z = value.w
        }


    var yzxw: Vec4
        get() = Vec4(y, z, x, w)
        set(value) {
            y = value.x
            z = value.y
            x = value.z
            w = value.w
        }


    var yzyx: Vec4
        get() = Vec4(y, z, y, x)
        set(value) {
            y = value.x
            z = value.y
            y = value.z
            x = value.w
        }


    var yzyy: Vec4
        get() = Vec4(y, z, y, y)
        set(value) {
            y = value.x
            z = value.y
            y = value.z
            y = value.w
        }


    var yzyz: Vec4
        get() = Vec4(y, z, y, z)
        set(value) {
            y = value.x
            z = value.y
            y = value.z
            z = value.w
        }


    var yzyw: Vec4
        get() = Vec4(y, z, y, w)
        set(value) {
            y = value.x
            z = value.y
            y = value.z
            w = value.w
        }


    var yzzx: Vec4
        get() = Vec4(y, z, z, x)
        set(value) {
            y = value.x
            z = value.y
            z = value.z
            x = value.w
        }


    var yzzy: Vec4
        get() = Vec4(y, z, z, y)
        set(value) {
            y = value.x
            z = value.y
            z = value.z
            y = value.w
        }


    var yzzz: Vec4
        get() = Vec4(y, z, z, z)
        set(value) {
            y = value.x
            z = value.y
            z = value.z
            z = value.w
        }


    var yzzw: Vec4
        get() = Vec4(y, z, z, w)
        set(value) {
            y = value.x
            z = value.y
            z = value.z
            w = value.w
        }


    var yzwx: Vec4
        get() = Vec4(y, z, w, x)
        set(value) {
            y = value.x
            z = value.y
            w = value.z
            x = value.w
        }


    var yzwy: Vec4
        get() = Vec4(y, z, w, y)
        set(value) {
            y = value.x
            z = value.y
            w = value.z
            y = value.w
        }


    var yzwz: Vec4
        get() = Vec4(y, z, w, z)
        set(value) {
            y = value.x
            z = value.y
            w = value.z
            z = value.w
        }


    var yzww: Vec4
        get() = Vec4(y, z, w, w)
        set(value) {
            y = value.x
            z = value.y
            w = value.z
            w = value.w
        }


    var ywxx: Vec4
        get() = Vec4(y, w, x, x)
        set(value) {
            y = value.x
            w = value.y
            x = value.z
            x = value.w
        }


    var ywxy: Vec4
        get() = Vec4(y, w, x, y)
        set(value) {
            y = value.x
            w = value.y
            x = value.z
            y = value.w
        }


    var ywxz: Vec4
        get() = Vec4(y, w, x, z)
        set(value) {
            y = value.x
            w = value.y
            x = value.z
            z = value.w
        }


    var ywxw: Vec4
        get() = Vec4(y, w, x, w)
        set(value) {
            y = value.x
            w = value.y
            x = value.z
            w = value.w
        }


    var ywyx: Vec4
        get() = Vec4(y, w, y, x)
        set(value) {
            y = value.x
            w = value.y
            y = value.z
            x = value.w
        }


    var ywyy: Vec4
        get() = Vec4(y, w, y, y)
        set(value) {
            y = value.x
            w = value.y
            y = value.z
            y = value.w
        }


    var ywyz: Vec4
        get() = Vec4(y, w, y, z)
        set(value) {
            y = value.x
            w = value.y
            y = value.z
            z = value.w
        }


    var ywyw: Vec4
        get() = Vec4(y, w, y, w)
        set(value) {
            y = value.x
            w = value.y
            y = value.z
            w = value.w
        }


    var ywzx: Vec4
        get() = Vec4(y, w, z, x)
        set(value) {
            y = value.x
            w = value.y
            z = value.z
            x = value.w
        }


    var ywzy: Vec4
        get() = Vec4(y, w, z, y)
        set(value) {
            y = value.x
            w = value.y
            z = value.z
            y = value.w
        }


    var ywzz: Vec4
        get() = Vec4(y, w, z, z)
        set(value) {
            y = value.x
            w = value.y
            z = value.z
            z = value.w
        }


    var ywzw: Vec4
        get() = Vec4(y, w, z, w)
        set(value) {
            y = value.x
            w = value.y
            z = value.z
            w = value.w
        }


    var ywwx: Vec4
        get() = Vec4(y, w, w, x)
        set(value) {
            y = value.x
            w = value.y
            w = value.z
            x = value.w
        }


    var ywwy: Vec4
        get() = Vec4(y, w, w, y)
        set(value) {
            y = value.x
            w = value.y
            w = value.z
            y = value.w
        }


    var ywwz: Vec4
        get() = Vec4(y, w, w, z)
        set(value) {
            y = value.x
            w = value.y
            w = value.z
            z = value.w
        }


    var ywww: Vec4
        get() = Vec4(y, w, w, w)
        set(value) {
            y = value.x
            w = value.y
            w = value.z
            w = value.w
        }


    var zxxx: Vec4
        get() = Vec4(z, x, x, x)
        set(value) {
            z = value.x
            x = value.y
            x = value.z
            x = value.w
        }


    var zxxy: Vec4
        get() = Vec4(z, x, x, y)
        set(value) {
            z = value.x
            x = value.y
            x = value.z
            y = value.w
        }


    var zxxz: Vec4
        get() = Vec4(z, x, x, z)
        set(value) {
            z = value.x
            x = value.y
            x = value.z
            z = value.w
        }


    var zxxw: Vec4
        get() = Vec4(z, x, x, w)
        set(value) {
            z = value.x
            x = value.y
            x = value.z
            w = value.w
        }


    var zxyx: Vec4
        get() = Vec4(z, x, y, x)
        set(value) {
            z = value.x
            x = value.y
            y = value.z
            x = value.w
        }


    var zxyy: Vec4
        get() = Vec4(z, x, y, y)
        set(value) {
            z = value.x
            x = value.y
            y = value.z
            y = value.w
        }


    var zxyz: Vec4
        get() = Vec4(z, x, y, z)
        set(value) {
            z = value.x
            x = value.y
            y = value.z
            z = value.w
        }


    var zxyw: Vec4
        get() = Vec4(z, x, y, w)
        set(value) {
            z = value.x
            x = value.y
            y = value.z
            w = value.w
        }


    var zxzx: Vec4
        get() = Vec4(z, x, z, x)
        set(value) {
            z = value.x
            x = value.y
            z = value.z
            x = value.w
        }


    var zxzy: Vec4
        get() = Vec4(z, x, z, y)
        set(value) {
            z = value.x
            x = value.y
            z = value.z
            y = value.w
        }


    var zxzz: Vec4
        get() = Vec4(z, x, z, z)
        set(value) {
            z = value.x
            x = value.y
            z = value.z
            z = value.w
        }


    var zxzw: Vec4
        get() = Vec4(z, x, z, w)
        set(value) {
            z = value.x
            x = value.y
            z = value.z
            w = value.w
        }


    var zxwx: Vec4
        get() = Vec4(z, x, w, x)
        set(value) {
            z = value.x
            x = value.y
            w = value.z
            x = value.w
        }


    var zxwy: Vec4
        get() = Vec4(z, x, w, y)
        set(value) {
            z = value.x
            x = value.y
            w = value.z
            y = value.w
        }


    var zxwz: Vec4
        get() = Vec4(z, x, w, z)
        set(value) {
            z = value.x
            x = value.y
            w = value.z
            z = value.w
        }


    var zxww: Vec4
        get() = Vec4(z, x, w, w)
        set(value) {
            z = value.x
            x = value.y
            w = value.z
            w = value.w
        }


    var zyxx: Vec4
        get() = Vec4(z, y, x, x)
        set(value) {
            z = value.x
            y = value.y
            x = value.z
            x = value.w
        }


    var zyxy: Vec4
        get() = Vec4(z, y, x, y)
        set(value) {
            z = value.x
            y = value.y
            x = value.z
            y = value.w
        }


    var zyxz: Vec4
        get() = Vec4(z, y, x, z)
        set(value) {
            z = value.x
            y = value.y
            x = value.z
            z = value.w
        }


    var zyxw: Vec4
        get() = Vec4(z, y, x, w)
        set(value) {
            z = value.x
            y = value.y
            x = value.z
            w = value.w
        }


    var zyyx: Vec4
        get() = Vec4(z, y, y, x)
        set(value) {
            z = value.x
            y = value.y
            y = value.z
            x = value.w
        }


    var zyyy: Vec4
        get() = Vec4(z, y, y, y)
        set(value) {
            z = value.x
            y = value.y
            y = value.z
            y = value.w
        }


    var zyyz: Vec4
        get() = Vec4(z, y, y, z)
        set(value) {
            z = value.x
            y = value.y
            y = value.z
            z = value.w
        }


    var zyyw: Vec4
        get() = Vec4(z, y, y, w)
        set(value) {
            z = value.x
            y = value.y
            y = value.z
            w = value.w
        }


    var zyzx: Vec4
        get() = Vec4(z, y, z, x)
        set(value) {
            z = value.x
            y = value.y
            z = value.z
            x = value.w
        }


    var zyzy: Vec4
        get() = Vec4(z, y, z, y)
        set(value) {
            z = value.x
            y = value.y
            z = value.z
            y = value.w
        }


    var zyzz: Vec4
        get() = Vec4(z, y, z, z)
        set(value) {
            z = value.x
            y = value.y
            z = value.z
            z = value.w
        }


    var zyzw: Vec4
        get() = Vec4(z, y, z, w)
        set(value) {
            z = value.x
            y = value.y
            z = value.z
            w = value.w
        }


    var zywx: Vec4
        get() = Vec4(z, y, w, x)
        set(value) {
            z = value.x
            y = value.y
            w = value.z
            x = value.w
        }


    var zywy: Vec4
        get() = Vec4(z, y, w, y)
        set(value) {
            z = value.x
            y = value.y
            w = value.z
            y = value.w
        }


    var zywz: Vec4
        get() = Vec4(z, y, w, z)
        set(value) {
            z = value.x
            y = value.y
            w = value.z
            z = value.w
        }


    var zyww: Vec4
        get() = Vec4(z, y, w, w)
        set(value) {
            z = value.x
            y = value.y
            w = value.z
            w = value.w
        }


    var zzxx: Vec4
        get() = Vec4(z, z, x, x)
        set(value) {
            z = value.x
            z = value.y
            x = value.z
            x = value.w
        }


    var zzxy: Vec4
        get() = Vec4(z, z, x, y)
        set(value) {
            z = value.x
            z = value.y
            x = value.z
            y = value.w
        }


    var zzxz: Vec4
        get() = Vec4(z, z, x, z)
        set(value) {
            z = value.x
            z = value.y
            x = value.z
            z = value.w
        }


    var zzxw: Vec4
        get() = Vec4(z, z, x, w)
        set(value) {
            z = value.x
            z = value.y
            x = value.z
            w = value.w
        }


    var zzyx: Vec4
        get() = Vec4(z, z, y, x)
        set(value) {
            z = value.x
            z = value.y
            y = value.z
            x = value.w
        }


    var zzyy: Vec4
        get() = Vec4(z, z, y, y)
        set(value) {
            z = value.x
            z = value.y
            y = value.z
            y = value.w
        }


    var zzyz: Vec4
        get() = Vec4(z, z, y, z)
        set(value) {
            z = value.x
            z = value.y
            y = value.z
            z = value.w
        }


    var zzyw: Vec4
        get() = Vec4(z, z, y, w)
        set(value) {
            z = value.x
            z = value.y
            y = value.z
            w = value.w
        }


    var zzzx: Vec4
        get() = Vec4(z, z, z, x)
        set(value) {
            z = value.x
            z = value.y
            z = value.z
            x = value.w
        }


    var zzzy: Vec4
        get() = Vec4(z, z, z, y)
        set(value) {
            z = value.x
            z = value.y
            z = value.z
            y = value.w
        }


    var zzzz: Vec4
        get() = Vec4(z, z, z, z)
        set(value) {
            z = value.x
            z = value.y
            z = value.z
            z = value.w
        }


    var zzzw: Vec4
        get() = Vec4(z, z, z, w)
        set(value) {
            z = value.x
            z = value.y
            z = value.z
            w = value.w
        }


    var zzwx: Vec4
        get() = Vec4(z, z, w, x)
        set(value) {
            z = value.x
            z = value.y
            w = value.z
            x = value.w
        }


    var zzwy: Vec4
        get() = Vec4(z, z, w, y)
        set(value) {
            z = value.x
            z = value.y
            w = value.z
            y = value.w
        }


    var zzwz: Vec4
        get() = Vec4(z, z, w, z)
        set(value) {
            z = value.x
            z = value.y
            w = value.z
            z = value.w
        }


    var zzww: Vec4
        get() = Vec4(z, z, w, w)
        set(value) {
            z = value.x
            z = value.y
            w = value.z
            w = value.w
        }


    var zwxx: Vec4
        get() = Vec4(z, w, x, x)
        set(value) {
            z = value.x
            w = value.y
            x = value.z
            x = value.w
        }


    var zwxy: Vec4
        get() = Vec4(z, w, x, y)
        set(value) {
            z = value.x
            w = value.y
            x = value.z
            y = value.w
        }


    var zwxz: Vec4
        get() = Vec4(z, w, x, z)
        set(value) {
            z = value.x
            w = value.y
            x = value.z
            z = value.w
        }


    var zwxw: Vec4
        get() = Vec4(z, w, x, w)
        set(value) {
            z = value.x
            w = value.y
            x = value.z
            w = value.w
        }


    var zwyx: Vec4
        get() = Vec4(z, w, y, x)
        set(value) {
            z = value.x
            w = value.y
            y = value.z
            x = value.w
        }


    var zwyy: Vec4
        get() = Vec4(z, w, y, y)
        set(value) {
            z = value.x
            w = value.y
            y = value.z
            y = value.w
        }


    var zwyz: Vec4
        get() = Vec4(z, w, y, z)
        set(value) {
            z = value.x
            w = value.y
            y = value.z
            z = value.w
        }


    var zwyw: Vec4
        get() = Vec4(z, w, y, w)
        set(value) {
            z = value.x
            w = value.y
            y = value.z
            w = value.w
        }


    var zwzx: Vec4
        get() = Vec4(z, w, z, x)
        set(value) {
            z = value.x
            w = value.y
            z = value.z
            x = value.w
        }


    var zwzy: Vec4
        get() = Vec4(z, w, z, y)
        set(value) {
            z = value.x
            w = value.y
            z = value.z
            y = value.w
        }


    var zwzz: Vec4
        get() = Vec4(z, w, z, z)
        set(value) {
            z = value.x
            w = value.y
            z = value.z
            z = value.w
        }


    var zwzw: Vec4
        get() = Vec4(z, w, z, w)
        set(value) {
            z = value.x
            w = value.y
            z = value.z
            w = value.w
        }


    var zwwx: Vec4
        get() = Vec4(z, w, w, x)
        set(value) {
            z = value.x
            w = value.y
            w = value.z
            x = value.w
        }


    var zwwy: Vec4
        get() = Vec4(z, w, w, y)
        set(value) {
            z = value.x
            w = value.y
            w = value.z
            y = value.w
        }


    var zwwz: Vec4
        get() = Vec4(z, w, w, z)
        set(value) {
            z = value.x
            w = value.y
            w = value.z
            z = value.w
        }


    var zwww: Vec4
        get() = Vec4(z, w, w, w)
        set(value) {
            z = value.x
            w = value.y
            w = value.z
            w = value.w
        }


    var wxxx: Vec4
        get() = Vec4(w, x, x, x)
        set(value) {
            w = value.x
            x = value.y
            x = value.z
            x = value.w
        }


    var wxxy: Vec4
        get() = Vec4(w, x, x, y)
        set(value) {
            w = value.x
            x = value.y
            x = value.z
            y = value.w
        }


    var wxxz: Vec4
        get() = Vec4(w, x, x, z)
        set(value) {
            w = value.x
            x = value.y
            x = value.z
            z = value.w
        }


    var wxxw: Vec4
        get() = Vec4(w, x, x, w)
        set(value) {
            w = value.x
            x = value.y
            x = value.z
            w = value.w
        }


    var wxyx: Vec4
        get() = Vec4(w, x, y, x)
        set(value) {
            w = value.x
            x = value.y
            y = value.z
            x = value.w
        }


    var wxyy: Vec4
        get() = Vec4(w, x, y, y)
        set(value) {
            w = value.x
            x = value.y
            y = value.z
            y = value.w
        }


    var wxyz: Vec4
        get() = Vec4(w, x, y, z)
        set(value) {
            w = value.x
            x = value.y
            y = value.z
            z = value.w
        }


    var wxyw: Vec4
        get() = Vec4(w, x, y, w)
        set(value) {
            w = value.x
            x = value.y
            y = value.z
            w = value.w
        }


    var wxzx: Vec4
        get() = Vec4(w, x, z, x)
        set(value) {
            w = value.x
            x = value.y
            z = value.z
            x = value.w
        }


    var wxzy: Vec4
        get() = Vec4(w, x, z, y)
        set(value) {
            w = value.x
            x = value.y
            z = value.z
            y = value.w
        }


    var wxzz: Vec4
        get() = Vec4(w, x, z, z)
        set(value) {
            w = value.x
            x = value.y
            z = value.z
            z = value.w
        }


    var wxzw: Vec4
        get() = Vec4(w, x, z, w)
        set(value) {
            w = value.x
            x = value.y
            z = value.z
            w = value.w
        }


    var wxwx: Vec4
        get() = Vec4(w, x, w, x)
        set(value) {
            w = value.x
            x = value.y
            w = value.z
            x = value.w
        }


    var wxwy: Vec4
        get() = Vec4(w, x, w, y)
        set(value) {
            w = value.x
            x = value.y
            w = value.z
            y = value.w
        }


    var wxwz: Vec4
        get() = Vec4(w, x, w, z)
        set(value) {
            w = value.x
            x = value.y
            w = value.z
            z = value.w
        }


    var wxww: Vec4
        get() = Vec4(w, x, w, w)
        set(value) {
            w = value.x
            x = value.y
            w = value.z
            w = value.w
        }


    var wyxx: Vec4
        get() = Vec4(w, y, x, x)
        set(value) {
            w = value.x
            y = value.y
            x = value.z
            x = value.w
        }


    var wyxy: Vec4
        get() = Vec4(w, y, x, y)
        set(value) {
            w = value.x
            y = value.y
            x = value.z
            y = value.w
        }


    var wyxz: Vec4
        get() = Vec4(w, y, x, z)
        set(value) {
            w = value.x
            y = value.y
            x = value.z
            z = value.w
        }


    var wyxw: Vec4
        get() = Vec4(w, y, x, w)
        set(value) {
            w = value.x
            y = value.y
            x = value.z
            w = value.w
        }


    var wyyx: Vec4
        get() = Vec4(w, y, y, x)
        set(value) {
            w = value.x
            y = value.y
            y = value.z
            x = value.w
        }


    var wyyy: Vec4
        get() = Vec4(w, y, y, y)
        set(value) {
            w = value.x
            y = value.y
            y = value.z
            y = value.w
        }


    var wyyz: Vec4
        get() = Vec4(w, y, y, z)
        set(value) {
            w = value.x
            y = value.y
            y = value.z
            z = value.w
        }


    var wyyw: Vec4
        get() = Vec4(w, y, y, w)
        set(value) {
            w = value.x
            y = value.y
            y = value.z
            w = value.w
        }


    var wyzx: Vec4
        get() = Vec4(w, y, z, x)
        set(value) {
            w = value.x
            y = value.y
            z = value.z
            x = value.w
        }


    var wyzy: Vec4
        get() = Vec4(w, y, z, y)
        set(value) {
            w = value.x
            y = value.y
            z = value.z
            y = value.w
        }


    var wyzz: Vec4
        get() = Vec4(w, y, z, z)
        set(value) {
            w = value.x
            y = value.y
            z = value.z
            z = value.w
        }


    var wyzw: Vec4
        get() = Vec4(w, y, z, w)
        set(value) {
            w = value.x
            y = value.y
            z = value.z
            w = value.w
        }


    var wywx: Vec4
        get() = Vec4(w, y, w, x)
        set(value) {
            w = value.x
            y = value.y
            w = value.z
            x = value.w
        }


    var wywy: Vec4
        get() = Vec4(w, y, w, y)
        set(value) {
            w = value.x
            y = value.y
            w = value.z
            y = value.w
        }


    var wywz: Vec4
        get() = Vec4(w, y, w, z)
        set(value) {
            w = value.x
            y = value.y
            w = value.z
            z = value.w
        }


    var wyww: Vec4
        get() = Vec4(w, y, w, w)
        set(value) {
            w = value.x
            y = value.y
            w = value.z
            w = value.w
        }


    var wzxx: Vec4
        get() = Vec4(w, z, x, x)
        set(value) {
            w = value.x
            z = value.y
            x = value.z
            x = value.w
        }


    var wzxy: Vec4
        get() = Vec4(w, z, x, y)
        set(value) {
            w = value.x
            z = value.y
            x = value.z
            y = value.w
        }


    var wzxz: Vec4
        get() = Vec4(w, z, x, z)
        set(value) {
            w = value.x
            z = value.y
            x = value.z
            z = value.w
        }


    var wzxw: Vec4
        get() = Vec4(w, z, x, w)
        set(value) {
            w = value.x
            z = value.y
            x = value.z
            w = value.w
        }


    var wzyx: Vec4
        get() = Vec4(w, z, y, x)
        set(value) {
            w = value.x
            z = value.y
            y = value.z
            x = value.w
        }


    var wzyy: Vec4
        get() = Vec4(w, z, y, y)
        set(value) {
            w = value.x
            z = value.y
            y = value.z
            y = value.w
        }


    var wzyz: Vec4
        get() = Vec4(w, z, y, z)
        set(value) {
            w = value.x
            z = value.y
            y = value.z
            z = value.w
        }


    var wzyw: Vec4
        get() = Vec4(w, z, y, w)
        set(value) {
            w = value.x
            z = value.y
            y = value.z
            w = value.w
        }


    var wzzx: Vec4
        get() = Vec4(w, z, z, x)
        set(value) {
            w = value.x
            z = value.y
            z = value.z
            x = value.w
        }


    var wzzy: Vec4
        get() = Vec4(w, z, z, y)
        set(value) {
            w = value.x
            z = value.y
            z = value.z
            y = value.w
        }


    var wzzz: Vec4
        get() = Vec4(w, z, z, z)
        set(value) {
            w = value.x
            z = value.y
            z = value.z
            z = value.w
        }


    var wzzw: Vec4
        get() = Vec4(w, z, z, w)
        set(value) {
            w = value.x
            z = value.y
            z = value.z
            w = value.w
        }


    var wzwx: Vec4
        get() = Vec4(w, z, w, x)
        set(value) {
            w = value.x
            z = value.y
            w = value.z
            x = value.w
        }


    var wzwy: Vec4
        get() = Vec4(w, z, w, y)
        set(value) {
            w = value.x
            z = value.y
            w = value.z
            y = value.w
        }


    var wzwz: Vec4
        get() = Vec4(w, z, w, z)
        set(value) {
            w = value.x
            z = value.y
            w = value.z
            z = value.w
        }


    var wzww: Vec4
        get() = Vec4(w, z, w, w)
        set(value) {
            w = value.x
            z = value.y
            w = value.z
            w = value.w
        }


    var wwxx: Vec4
        get() = Vec4(w, w, x, x)
        set(value) {
            w = value.x
            w = value.y
            x = value.z
            x = value.w
        }


    var wwxy: Vec4
        get() = Vec4(w, w, x, y)
        set(value) {
            w = value.x
            w = value.y
            x = value.z
            y = value.w
        }


    var wwxz: Vec4
        get() = Vec4(w, w, x, z)
        set(value) {
            w = value.x
            w = value.y
            x = value.z
            z = value.w
        }


    var wwxw: Vec4
        get() = Vec4(w, w, x, w)
        set(value) {
            w = value.x
            w = value.y
            x = value.z
            w = value.w
        }


    var wwyx: Vec4
        get() = Vec4(w, w, y, x)
        set(value) {
            w = value.x
            w = value.y
            y = value.z
            x = value.w
        }


    var wwyy: Vec4
        get() = Vec4(w, w, y, y)
        set(value) {
            w = value.x
            w = value.y
            y = value.z
            y = value.w
        }


    var wwyz: Vec4
        get() = Vec4(w, w, y, z)
        set(value) {
            w = value.x
            w = value.y
            y = value.z
            z = value.w
        }


    var wwyw: Vec4
        get() = Vec4(w, w, y, w)
        set(value) {
            w = value.x
            w = value.y
            y = value.z
            w = value.w
        }


    var wwzx: Vec4
        get() = Vec4(w, w, z, x)
        set(value) {
            w = value.x
            w = value.y
            z = value.z
            x = value.w
        }


    var wwzy: Vec4
        get() = Vec4(w, w, z, y)
        set(value) {
            w = value.x
            w = value.y
            z = value.z
            y = value.w
        }


    var wwzz: Vec4
        get() = Vec4(w, w, z, z)
        set(value) {
            w = value.x
            w = value.y
            z = value.z
            z = value.w
        }


    var wwzw: Vec4
        get() = Vec4(w, w, z, w)
        set(value) {
            w = value.x
            w = value.y
            z = value.z
            w = value.w
        }


    var wwwx: Vec4
        get() = Vec4(w, w, w, x)
        set(value) {
            w = value.x
            w = value.y
            w = value.z
            x = value.w
        }


    var wwwy: Vec4
        get() = Vec4(w, w, w, y)
        set(value) {
            w = value.x
            w = value.y
            w = value.z
            y = value.w
        }


    var wwwz: Vec4
        get() = Vec4(w, w, w, z)
        set(value) {
            w = value.x
            w = value.y
            w = value.z
            z = value.w
        }


    var wwww: Vec4
        get() = Vec4(w, w, w, w)
        set(value) {
            w = value.x
            w = value.y
            w = value.z
            w = value.w
        }


    // Длина
    fun length(): Float = sqrt(x * x + y * y + z * z + w * w)
    fun lengthSquared(): Float = x * x + y * y + z * z + w * w

    // Нормализация
    fun normalize(): Vec4 {
        val len = length()
        if (len != 0f) {
            x /= len; y /= len; z /= len; w /= len
        }
        return this
    }

    fun normalized(): Vec4 {
        val len = length()
        return if (len != 0f) Vec4(x / len, y / len, z / len, w / len) else Vec4()
    }

    // Скалярное произведение
    infix fun dot(other: Vec4): Float =
        x * other.x + y * other.y + z * other.z + w * other.w

    // Копия
    fun copy(): Vec4 = Vec4(x, y, z, w)

    // Операторы
    operator fun plus(other: Vec4): Vec4 = Vec4(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: Vec4): Vec4 = Vec4(x - other.x, y - other.y, z - other.z, w - other.w)
    operator fun times(other: Vec4): Vec4 = Vec4(x * other.x, y * other.y, z * other.z, w * other.w)
    operator fun div(other: Vec4): Vec4 = Vec4(x / other.x, y / other.y, z / other.z, w / other.w)

    operator fun times(scalar: Float): Vec4 = Vec4(x * scalar, y * scalar, z * scalar, w * scalar)
    operator fun div(scalar: Float): Vec4 = Vec4(x / scalar, y / scalar, z / scalar, w / scalar)

    operator fun unaryMinus(): Vec4 = Vec4(-x, -y, -z, -w)

    operator fun plus(scalar: Float): Vec4 = Vec4(x + scalar, y + scalar, z + scalar, w + scalar)
    operator fun minus(scalar: Float): Vec4 = Vec4(x - scalar, y - scalar, z - scalar, w - scalar)

    operator fun plusAssign(scalar: Float) { x += scalar; y += scalar; z += scalar; w += scalar }
    operator fun minusAssign(scalar: Float) { x -= scalar; y -= scalar; z -= scalar; w -= scalar }

    operator fun plusAssign(other: Vec4) { x += other.x; y += other.y; z += other.z; w += other.w }
    operator fun minusAssign(other: Vec4) { x -= other.x; y -= other.y; z -= other.z; w -= other.w }
    operator fun timesAssign(scalar: Float) { x *= scalar; y *= scalar; z *= scalar; w *= scalar }
    operator fun divAssign(scalar: Float) { x /= scalar; y /= scalar; z /= scalar; w /= scalar }

    override fun equals(other: Any?): Boolean =
        other is Vec4 && x == other.x && y == other.y && z == other.z && w == other.w

    override fun hashCode(): Int =
        31 * (31 * (31 * x.hashCode() + y.hashCode()) + z.hashCode()) + w.hashCode()

    override fun toString(): String = "Vec4(x=$x, y=$y, z=$z, w=$w)"


    fun anyGreater(other: Vec4) =
        x > other.x || y > other.y || z > other.z || w > other.w
    fun allGreater(other: Vec4) =
        x > other.x && y > other.y && z > other.z && w > other.w

    fun anyLess(other: Vec4) =
        x < other.x || y < other.y || z < other.z || w < other.w
    fun allLess(other: Vec4) =
        x < other.x && y < other.y && z < other.z && w < other.w

    fun anyEqual(other: Vec4) =
        x == other.x || y == other.y || z == other.z || w == other.w
    fun allEqual(other: Vec4) =
        x == other.x && y == other.y && z == other.z && w == other.w

    fun invert(
        xInv: Boolean,
        yInv: Boolean,
        zInv: Boolean,
        wInv: Boolean
    ): Vec4 {
        if (xInv) x = -x
        if (yInv) y = -y
        if (zInv) z = -z
        if (wInv) w = -w
        return this
    }

    fun invertX()   = invert(true,  false, false, false)
    fun invertY()   = invert(false, true,  false, false)
    fun invertZ()   = invert(false, false, true,  false)
    fun invertW()   = invert(false, false, false, true)

    fun invertXY()  = invert(true,  true,  false, false)
    fun invertXZ()  = invert(true,  false, true,  false)
    fun invertXW()  = invert(true,  false, false, true)
    fun invertYZ()  = invert(false, true,  true,  false)
    fun invertYW()  = invert(false, true,  false, true)
    fun invertZW()  = invert(false, false, true,  true)

    fun invertXYZ() = invert(true,  true,  true,  false)
    fun invertXYW() = invert(true,  true,  false, true)
    fun invertXZW() = invert(true,  false, true,  true)
    fun invertYZW() = invert(false, true,  true,  true)

    fun invertAll() = invert(true, true, true, true)

    fun inverted(
        xInv: Boolean,
        yInv: Boolean,
        zInv: Boolean,
        wInv: Boolean
    ): Vec4 =
        Vec4(
            if (xInv) -x else x,
            if (yInv) -y else y,
            if (zInv) -z else z,
            if (wInv) -w else w
        )

    fun invertedX()   = inverted(true,  false, false, false)
    fun invertedY()   = inverted(false, true,  false, false)
    fun invertedZ()   = inverted(false, false, true,  false)
    fun invertedW()   = inverted(false, false, false, true)

    fun invertedXY()  = inverted(true,  true,  false, false)
    fun invertedXZ()  = inverted(true,  false, true,  false)
    fun invertedXW()  = inverted(true,  false, false, true)
    fun invertedYZ()  = inverted(false, true,  true,  false)
    fun invertedYW()  = inverted(false, true,  false, true)
    fun invertedZW()  = inverted(false, false, true,  true)

    fun invertedXYZ() = inverted(true,  true,  true,  false)
    fun invertedXYW() = inverted(true,  true,  false, true)
    fun invertedXZW() = inverted(true,  false, true,  true)
    fun invertedYZW() = inverted(false, true,  true,  true)

    fun invertedAll() = inverted(true, true, true, true)

    fun invertXYZW() = invertAll()
    fun invertedXYZW() = invertedAll()

    fun invert() = invert(true, true, true, true)
    fun inverted() = inverted(true, true, true, true)

    fun half(): Vec4 {
        x *= 0.5f
        y *= 0.5f
        z *= 0.5f
        w *= 0.5f
        return this
    }

    fun halved(): Vec4 =
        Vec4(
            x * 0.5f,
            y * 0.5f,
            z * 0.5f,
            w * 0.5f
        )

    fun double(): Vec4 {
        x *= 2f
        y *= 2f
        z *= 2f
        w *= 2f
        return this
    }

    fun doubled(): Vec4 =
        Vec4(x * 2f, y * 2f, z * 2f, w * 2f)

    fun abs(): Vec4 {
        if (x < 0f) x = -x
        if (y < 0f) y = -y
        if (z < 0f) z = -z
        if (w < 0f) w = -w
        return this
    }

    fun absed(): Vec4 =
        Vec4(
            kotlin.math.abs(x),
            kotlin.math.abs(y),
            kotlin.math.abs(z),
            kotlin.math.abs(w)
        )

    fun absDiff(other: Vec4): Vec4 =
        Vec4(
            kotlin.math.abs(x - other.x),
            kotlin.math.abs(y - other.y),
            kotlin.math.abs(z - other.z),
            kotlin.math.abs(w - other.w)
        )

    fun round(): Vec4 {
        x = round(x)
        y = round(y)
        z = round(z)
        w = round(w)
        return this
    }

    fun rounded(): Vec4 =
        Vec4(
            round(x),
            round(y),
            round(z),
            round(w)
        )

    fun floor(): Vec4 {
        x = floor(x)
        y = floor(y)
        z = floor(z)
        w = floor(w)
        return this
    }

    fun floored(): Vec4 =
        Vec4(
            floor(x),
            floor(y),
            floor(z),
            floor(w)
        )

    fun ceil(): Vec4 {
        x = ceil(x)
        y = ceil(y)
        z = ceil(z)
        w = ceil(w)
        return this
    }

    fun ceiled(): Vec4 =
        Vec4(
            ceil(x),
            ceil(y),
            ceil(z),
            ceil(w)
        )

    fun trunc(): Vec4 {
        x = x.toInt().toFloat()
        y = y.toInt().toFloat()
        z = z.toInt().toFloat()
        w = w.toInt().toFloat()
        return this
    }

    fun truncated(): Vec4 =
        Vec4(
            x.toInt().toFloat(),
            y.toInt().toFloat(),
            z.toInt().toFloat(),
            w.toInt().toFloat()
        )

    fun sign(): Vec4 {
        x = kotlin.math.sign(x)
        y = kotlin.math.sign(y)
        z = kotlin.math.sign(z)
        w = kotlin.math.sign(w)
        return this
    }

    fun signed(): Vec4 =
        Vec4(
            kotlin.math.sign(x),
            kotlin.math.sign(y),
            kotlin.math.sign(z),
            kotlin.math.sign(w)
        )

    fun snap(step: Vec4): Vec4 {
        x = kotlin.math.floor(x / step.x) * step.x
        y = kotlin.math.floor(y / step.y) * step.y
        z = kotlin.math.floor(z / step.z) * step.z
        w = kotlin.math.floor(w / step.w) * step.w
        return this
    }

    fun snapped(step: Vec4): Vec4 =
        Vec4(
            kotlin.math.floor(x / step.x) * step.x,
            kotlin.math.floor(y / step.y) * step.y,
            kotlin.math.floor(z / step.z) * step.z,
            kotlin.math.floor(w / step.w) * step.w
        )

    fun min(other: Vec4): Vec4 {
        x = kotlin.math.min(x, other.x)
        y = kotlin.math.min(y, other.y)
        z = kotlin.math.min(z, other.z)
        w = kotlin.math.min(w, other.w)
        return this
    }

    fun minned(other: Vec4): Vec4 =
        Vec4(
            kotlin.math.min(x, other.x),
            kotlin.math.min(y, other.y),
            kotlin.math.min(z, other.z),
            kotlin.math.min(w, other.w)
        )

    fun max(other: Vec4): Vec4 {
        x = kotlin.math.max(x, other.x)
        y = kotlin.math.max(y, other.y)
        z = kotlin.math.max(z, other.z)
        w = kotlin.math.max(w, other.w)
        return this
    }

    fun maxed(other: Vec4): Vec4 =
        Vec4(
            kotlin.math.max(x, other.x),
            kotlin.math.max(y, other.y),
            kotlin.math.max(z, other.z),
            kotlin.math.max(w, other.w)
        )

    fun lerp(to: Vec4, t: Float): Vec4 {
        x += (to.x - x) * t
        y += (to.y - y) * t
        z += (to.z - z) * t
        w += (to.w - w) * t
        return this
    }

    fun lerped(to: Vec4, t: Float): Vec4 =
        Vec4(
            x + (to.x - x) * t,
            y + (to.y - y) * t,
            z + (to.z - z) * t,
            w + (to.w - w) * t
        )

    fun toColor() : LColor {
        return LColor(this)
    }

    fun wrapped(size: Vec4): Vec4 =
        Vec4(
            ((x % size.x) + size.x) % size.x,
            ((y % size.y) + size.y) % size.y,
            ((z % size.z) + size.z) % size.z,
            ((w % size.w) + size.w) % size.w
        )

    fun wrap(size: Vec4): Vec4 {
        x = ((x % size.x) + size.x) % size.x
        y = ((y % size.y) + size.y) % size.y
        z = ((z % size.z) + size.z) % size.z
        w = ((w % size.w) + size.w) % size.w
        return this
    }

}
