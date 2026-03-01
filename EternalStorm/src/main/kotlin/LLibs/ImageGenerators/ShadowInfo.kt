package la.vok.LavokLibrary.Gradient

import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState
import processing.core.PImage

data class ShadowInfo(
    val image: PImage,
    val outputSize: LPoint = LPoint(60, 60),
    val padding: Int = 5,
    val blurRadius: Int = 4,
    val save: Boolean = true
) {
    fun generate(): PImage {
        return ShadowGenerator.generate(this, AppState.main)
    }
}