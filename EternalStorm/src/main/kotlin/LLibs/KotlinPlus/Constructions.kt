package la.vok.LavokLibrary.KotlinPlus

import la.vok.LavokLibrary.Vectors.LPoint
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

fun forEachInArea(p1: LPoint, p2: LPoint, plusBorder: Int = 0, action: (x: Int, y: Int) -> Unit) {
    // Вычисляем границы, чтобы метод работал при любом порядке углов
    val minX = minOf(p1.x, p2.x)
    val maxX = maxOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxY = maxOf(p1.y, p2.y)

    // Проходим по всей области
    for (x in minX-plusBorder..maxX+plusBorder) {
        for (y in minY-plusBorder..maxY+plusBorder) {
            action(x, y)
        }
    }
}

inline fun forEachOnBorder(
    p1: LPoint,
    p2: LPoint,
    plusBorder: Int = 0,
    action: (x: Int, y: Int, sideX: Int, sideY: Int) -> Unit
) {
    val minX = minOf(p1.x, p2.x) - plusBorder
    val maxX = maxOf(p1.x, p2.x) + plusBorder
    val minY = minOf(p1.y, p2.y) - plusBorder
    val maxY = maxOf(p1.y, p2.y) + plusBorder

    // 1. Горизонтальные линии (Верх и Низ)
    for (x in minX..maxX) {
        action(x, minY, 0, -1) // Верхняя грань (нормаль вверх)
        if (minY != maxY) {
            action(x, maxY, 0, 1) // Нижняя грань (нормаль вниз)
        }
    }

    // 2. Вертикальные линии (Лево и Право)
    // Исключаем углы (minY + 1 .. maxY - 1), так как они уже обработаны в горизонталях
    if (maxY - minY > 1) {
        for (y in (minY + 1)..(maxY - 1)) {
            action(minX, y, -1, 0) // Левая грань
            if (minX != maxX) {
                action(maxX, y, 1, 0) // Правая грань
            }
        }
    }
}