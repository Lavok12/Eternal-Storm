package la.vok.LavokLibrary.Gradient

import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState
import processing.core.PImage

data class GradientInfo(
    val c1: LColor,     // цвет 1
    val c2: LColor,     // цвет 2
    val p1: LPoint,     // позиция точки 1
    val p2: LPoint,     // позиция точки 2
    val resolution: LPoint, // разрешение изображения
    var save: Boolean = true
) {

    fun generate(): PImage {
        return GradientGenerator.generate(this, AppState.main)
    }
}
