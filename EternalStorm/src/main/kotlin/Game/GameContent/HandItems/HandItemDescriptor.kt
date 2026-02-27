package la.vok.Game.GameContent.HandItems

import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

data class HandItemDescriptor(
    val spriteName: String,
    val spriteSize: Vec2 = 1 v 1,
    val renderDelta: Vec2 = 0 v 0,
    val useDuration: Float = 100f,
    val useStageStep: Float = 6f,
    val animationType: AnimationType = AnimationType.Swing(),
    val renderLayer: Int = 1,
    val spriteAngle: Float = 0f,
    val leftAction: UseAction = UseAction.None,
    val rightAction: UseAction = UseAction.None
)

sealed class UseAction {

    object None : UseAction()

    data class PrintText(val text: String) : UseAction()

    data class PrintAtProgress(
        val text: String,
        val atProgress: Float = 0.5f
    ) : UseAction()

    data class PrintOnStart(val text: String) : UseAction()

    data class PrintOnEnd(val text: String) : UseAction()

    data class Custom(
        val onStart: (HandItem.() -> Unit)? = null,
        val onProgress: (HandItem.(progress: Float) -> Unit)? = null,
        val onEnd: (HandItem.() -> Unit)? = null,
        val triggerAt: Float? = null
    ) : UseAction()
}

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