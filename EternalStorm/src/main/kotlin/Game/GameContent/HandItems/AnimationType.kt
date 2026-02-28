package la.vok.Game.GameContent.HandItems

import la.vok.LavokLibrary.Vectors.Vec2

sealed class AnimationType {

    data class Swing(
        val swingAngle: Float = Math.PI.toFloat() * 0.65f,
        val startAngle: Float = 0f,
        val lungeDistance: Float = 0.15f,
        val peakScale: Float = 0.18f,
        val shakeAmplitude: Float = 0.04f
    ) : AnimationType()

    data class Thrust(
        val maxOffset: Float = 0.5f,
        val startOffset: Float = 0f,
        val peakScale: Float = 0.1f,
        val shakeAmplitude: Float = 0.03f
    ) : AnimationType()

    object Idle : AnimationType()

    data class Custom(
        val draw: HandItemRender.(
            lg: la.vok.LavokLibrary.LGraphics.LGraphics,
            pos: Vec2,
            size: Vec2,
            camera: la.vok.Core.CoreContent.Camera.Camera
        ) -> Unit
    ) : AnimationType()
}