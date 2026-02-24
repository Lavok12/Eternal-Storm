package la.vok.LavokLibrary.KotlinPlus

import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.Vec3
import la.vok.LavokLibrary.Vectors.Vec4

inline fun forEach(
    from: Float,
    to: Float,
    step: Float = 1f,
    action: (value: Float) -> Unit
) {
    var v = from
    while (v <= to + 1e-6f) {
        action(v)
        v += step
    }
}

inline fun forEach2D(
    from: Vec2,
    to: Vec2,
    step: Float,
    action: (x: Float, y: Float) -> Unit
) {
    var y = from.y
    while (y <= to.y + 1e-6f) {
        var x = from.x
        while (x <= to.x + 1e-6f) {
            action(x, y)
            x += step
        }
        y += step
    }
}

inline fun forEachVec2(from: Vec2, to: Vec2, step: Float, action: (Vec2) -> Unit) {
    val direction = to - from
    val distance = direction.length()
    if (distance < 1e-6f) {
        action(from)
        return
    }
    val dirNorm = direction / distance
    var traveled = 0f
    while (traveled <= distance + 1e-6f) {
        action(from + dirNorm * traveled)
        traveled += step
    }
}

inline fun forEachVec3(from: Vec3, to: Vec3, step: Float, action: (Vec3) -> Unit) {
    val direction = to - from
    val distance = direction.length()
    if (distance < 1e-6f) {
        action(from)
        return
    }
    val dirNorm = direction / distance
    var traveled = 0f
    while (traveled <= distance + 1e-6f) {
        action(from + dirNorm * traveled)
        traveled += step
    }
}

inline fun forEachVec4(from: Vec4, to: Vec4, step: Float, action: (Vec4) -> Unit) {
    val direction = to - from
    val distance = direction.length()
    if (distance < 1e-6f) {
        action(from)
        return
    }
    val dirNorm = direction / distance
    var traveled = 0f
    while (traveled <= distance + 1e-6f) {
        action(from + dirNorm * traveled)
        traveled += step
    }
}
