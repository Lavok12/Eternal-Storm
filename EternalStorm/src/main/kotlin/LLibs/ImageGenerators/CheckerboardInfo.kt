package la.vok.LavokLibrary.Gradient

import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState
import processing.core.PImage

data class CheckerboardInfo(
    val resolution: LPoint,
    val cellSize: Int = 8,              // размер клетки
    val color1: Int = 0xFFFFFFFF.toInt(), // белый
    val color2: Int = 0xFFBFBFBF.toInt(), // светло-серый
    var save: Boolean = true
) {

    fun generate(): PImage {
        return CheckerboardGenerator.generate(this, AppState.main)
    }
}
