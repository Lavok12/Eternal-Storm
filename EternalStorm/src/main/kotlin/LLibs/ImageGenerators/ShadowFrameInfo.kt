package la.vok.LavokLibrary.Gradient

import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState
import processing.core.PImage

/**
 * Тип рамочной тени.
 * INNER — затемнение по внутреннему краю (вигнетка).
 * OUTER — полупрозрачная тень снаружи (прозрачный центр, тёмные края).
 */
enum class ShadowType { INNER, OUTER }

/**
 * Параметры рамочного генератора теней.
 *
 * @param size        размер создаваемого изображения в пикселях
 * @param type        тип тени: INNER или OUTER
 * @param spread      ширина тени от края (в пикселях)
 * @param color       цвет тени
 * @param intensity   максимальная прозрачность тени (0.0..1.0)
 * @param save        кешировать ли результат
 */
data class ShadowFrameInfo(
    val size: LPoint,
    val type: ShadowType = ShadowType.INNER,
    val spread: Int = 10,
    val inset: Int = 0,     
    val color: LColor = LColor(0f, 0f, 0f),
    val intensity: Float = 0.5f,
    val save: Boolean = true
) {
    fun generate(): PImage = ShadowFrameGenerator.generate(this, AppState.main)
}