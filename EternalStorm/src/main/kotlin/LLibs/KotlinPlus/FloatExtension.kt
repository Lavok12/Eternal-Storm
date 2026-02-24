package la.vok.LavokLibrary.KotlinPlus

import la.vok.LavokLibrary.Vectors.Vec1
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.Vec3
import la.vok.LavokLibrary.Vectors.Vec4

fun Float.lerp(b: Float, t: Float): Float =
    this + (b - this) * t

fun Float.toVec1(): Vec1 =
    Vec1(this)

fun Float.toVec2X(y: Float = 0f): Vec2 =
    Vec2(this, y)

fun Float.toVec2Y(x: Float = 0f): Vec2 =
    Vec2(x, this)

fun Float.toVec3X(y: Float = 0f, z: Float = 0f): Vec3 =
    Vec3(this, y, z)

fun Float.toVec3Y(x: Float = 0f, z: Float = 0f): Vec3 =
    Vec3(x, this, z)

fun Float.toVec3Z(x: Float = 0f, y: Float = 0f): Vec3 =
    Vec3(x, y, this)

fun Float.toVec4X(y: Float = 0f, z: Float = 0f, w: Float = 0f): Vec4 =
    Vec4(this, y, z, w)

fun Float.toVec4Y(x: Float = 0f, z: Float = 0f, w: Float = 0f): Vec4 =
    Vec4(x, this, z, w)

fun Float.toVec4Z(x: Float = 0f, y: Float = 0f, w: Float = 0f): Vec4 =
    Vec4(x, y, this, w)

fun Float.toVec4W(x: Float = 0f, y: Float = 0f, z: Float = 0f): Vec4 =
    Vec4(x, y, z, this)

val Float.r: Int
    get() = this.toInt()