package la.vok.LavokLibrary.Vectors


infix fun Number.v(y: Number): Vec2 =
    Vec2(this.toFloat(), y.toFloat())

fun Number.v(): Vec1 =
    Vec1(this.toFloat())

infix fun Vec1.v(y: Number): Vec2 =
    Vec2(this.x, y.toFloat())

infix fun Vec1.v(v: Vec1): Vec2 =
    Vec2(this.x, v.x)

infix fun Vec2.v(z: Number): Vec3 =
    Vec3(this.x, this.y, z.toFloat())

infix fun Vec2.v(v: Vec1): Vec3 =
    Vec3(this.x, this.y, v.x)

infix fun Vec3.v(w: Number): Vec4 =
    Vec4(this.x, this.y, this.z, w.toFloat())

infix fun Vec3.v(v: Vec1): Vec4 =
    Vec4(this.x, this.y, this.z, v.x)

infix fun Number.v(v: Vec2): Vec3 =
    Vec3(this.toFloat(), v.x, v.y)

infix fun Number.v(v: Vec3): Vec4 =
    Vec4(this.toFloat(), v.x, v.y, v.z)

infix fun Vec1.v(v: Vec2): Vec3 =
    Vec3(this.x, v.x, v.y)

infix fun Vec1.v(v: Vec3): Vec4 =
    Vec4(this.x, v.x, v.y, v.z)

infix fun Number.v(v: Vec1): Vec2 =
    Vec2(this.toFloat(), v.x)

infix fun Vec2.v(v: Vec2): Vec4 =
    Vec4(this.x, this.y, v.x, v.y)
