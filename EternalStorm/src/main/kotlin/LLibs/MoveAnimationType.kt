package la.vok.LLibs

import kotlin.math.*

sealed class AnimationType {

    object Linear : AnimationType()

    data class EaseOut(val power: Float = 2f) : AnimationType()

    data class EaseIn(val power: Float = 2f) : AnimationType()

    data class EaseInOut(val power: Float = 2f) : AnimationType()

    data class Elastic(
        val oscillations: Float = 3f,
        val decay: Float = 4f
    ) : AnimationType()

    data class Bounce(val bounces: Int = 3, val decay: Float = 0.5f) : AnimationType()

    data class BackIn(val overshoot: Float = 1.70158f) : AnimationType()

    object Sine : AnimationType()

    data class Steps(val steps: Int = 4) : AnimationType()

    // Дуга и шейк убраны — они не имеют смысла для скалярных значений
}

class FloatAnimation(
    var from: Float,
    var to: Float,
    var type: AnimationType = AnimationType.Linear
) {
    var progress: Float = 0f
        set(value) { field = value.coerceIn(0f, 1f) }

    val isFinished: Boolean get() = progress >= 1f

    val current: Float get() = evaluate(progress)

    fun evaluate(t: Float): Float {
        val eased = ease(t.coerceIn(0f, 1f))
        return from + (to - from) * eased
    }

    private fun ease(t: Float): Float = when (val anim = type) {
        is AnimationType.Linear -> t

        is AnimationType.EaseOut -> 1f - (1f - t).pow(anim.power)

        is AnimationType.EaseIn -> t.pow(anim.power)

        is AnimationType.EaseInOut ->
            if (t < 0.5f) (2f.pow(anim.power - 1f)) * t.pow(anim.power)
            else 1f - (-2f * t + 2f).pow(anim.power) / 2f

        is AnimationType.Elastic -> {
            if (t == 0f || t == 1f) t
            else {
                val decay = exp(-anim.decay * t)
                val wave = sin(anim.oscillations * t * PI.toFloat() * 2f)
                1f - decay * wave
            }
        }

        is AnimationType.Bounce -> {
            var v = t
            var amplitude = 1f
            repeat(anim.bounces) {
                if (v < 1f / (anim.bounces + 1)) return@repeat
                v -= 1f / (anim.bounces + 1)
                amplitude *= anim.decay
            }
            1f - amplitude * cos(v * PI.toFloat() * (anim.bounces + 1))
        }

        is AnimationType.BackIn -> {
            val c = anim.overshoot
            t * t * ((c + 1f) * t - c)
        }

        is AnimationType.Sine -> (1f - cos(t * PI.toFloat())) / 2f

        is AnimationType.Steps -> floor(t * anim.steps) / anim.steps
    }

    fun reset() { progress = 0f }

    fun reverse() {
        val tmp = from
        from = to
        to = tmp
        progress = 1f - progress
    }
}

fun floatAnimation(
    from: Float,
    to: Float,
    type: AnimationType = AnimationType.Linear,
    block: FloatAnimation.() -> Unit = {}
): FloatAnimation = FloatAnimation(from, to, type).apply(block)