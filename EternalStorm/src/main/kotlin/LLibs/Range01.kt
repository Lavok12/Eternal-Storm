package la.vok.Utils

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Класс для работы с диапазоном от 0 до 1
 * Особенность: чем ближе к 0 или 1, тем сложнее продвигаться в том направлении
 */
class Range01(value: Float = 0f) {

    var value: Float = value.coerceIn(0f, 1f)
        private set

    // ===== Добавление / смещение с нелинейностью =====
    fun add(delta: Float, factor: Float = 1f): Range01 {
        value = nonlinearAdd(value, delta, factor)
        return this
    }

    fun subtract(delta: Float, factor: Float = 1f): Range01 {
        value = nonlinearAdd(value, -delta, factor)
        return this
    }

    fun addSymmetric(delta: Float, factor: Float = 1f): Range01 {
        value = nonlinearAddSymmetric(value, delta, factor)
        return this
    }

    fun subtractSymmetric(delta: Float, factor: Float = 1f): Range01 {
        value = nonlinearAddSymmetric(value, -delta, factor)
        return this
    }

    private fun nonlinearAddSymmetric(current: Float, delta: Float, factor: Float = 1f): Float {
        // Чем ближе к 0 или 1, тем тяжелее двигаться в любую сторону
        val distanceToEdge = min(current, 1f - current)
        val modFactor = (distanceToEdge * 2f).pow(factor) // 0..1 → коэффициент
        return (current + delta * modFactor).coerceIn(0f, 1f)
    }


    // ===== Нелинейное смещение =====
    private fun nonlinearAdd(current: Float, delta: Float, factor: Float = 1f): Float {
        // Чем ближе к 0 или 1, тем движение медленнее
        val modFactor = if (delta >= 0) (1 - current).pow(factor) else current.pow(factor)
        return (current + delta * modFactor).coerceIn(0f, 1f)
    }

    // ===== Инвертация =====
    fun invert(): Range01 {
        value = 1f - value
        return this
    }

    fun inverted(): Range01 = Range01(1f - value)

    // ===== Разница между двумя диапазонами =====
    fun difference(other: Range01): Float = abs(this.value - other.value)

    // ===== Проверка близости к другому диапазону =====
    fun isCloseTo(other: Range01, threshold: Float = 0.05f): Boolean =
        difference(other) <= threshold

    // ===== Примитивы =====
    fun set(v: Float): Range01 {
        value = v.coerceIn(0f, 1f)
        return this
    }

    fun reset(): Range01 {
        value = 0f
        return this
    }

    fun clone(): Range01 = Range01(value)

    // ===== Линейная интерполяция =====
    fun lerp(target: Range01, t: Float): Range01 {
        value = (value + (target.value - value) * t).coerceIn(0f, 1f)
        return this
    }

    /**
     * Близость к 0: 0 = далеко, 1 = максимально близко к 0
     */
    fun proximityToZero(): Float = 1f - value

    /**
     * Близость к 1: 0 = далеко, 1 = максимально близко к 1
     */
    fun proximityToOne(): Float = value

    // ===== Операторы для удобства =====
    operator fun plus(delta: Float) = clone().add(delta)
    operator fun minus(delta: Float) = clone().subtract(delta)
    operator fun times(factor: Float) = clone().set(value * factor)
    operator fun div(factor: Float) = clone().set(value / factor)

    operator fun plus(other: Range01) = clone().set((value + other.value).coerceIn(0f, 1f))
    operator fun minus(other: Range01) = clone().set((value - other.value).coerceIn(0f, 1f))

    override fun toString(): String = "Range01(value=${"%.3f".format(value)})"

    // ===== Статические вспомогательные методы =====
    companion object {
        fun average(a: Range01, b: Range01): Range01 = Range01((a.value + b.value) / 2f)
        fun max(a: Range01, b: Range01): Range01 = Range01(max(a.value, b.value))
        fun min(a: Range01, b: Range01): Range01 = Range01(min(a.value, b.value))

        fun random(): Range01 = Range01.random(0f,1f)

        fun random(min: Float, max: Float): Range01 =
            Range01((min.coerceIn(0f, 1f) + ((max - min).coerceIn(0f, 1f)) * Math.random().toFloat()))
    }
}
