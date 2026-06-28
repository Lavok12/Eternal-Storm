package la.vok.LavokLibrary.Gradient

import processing.core.PImage

data class TransitionInfo(
    val image: PImage,
    val neighborMask: Int,
    val strength: Float,
    val cornerStrength: Float = strength
)
