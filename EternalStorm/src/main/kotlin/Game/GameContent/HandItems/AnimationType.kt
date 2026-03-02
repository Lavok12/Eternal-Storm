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

    /**
     * Копьё, направленное к курсору.
     * Спрайт всегда смотрит в сторону цели (угол фиксируется при начале анимации).
     * Флип по X: если курсор левее игрока — изображение отражается.
     * Анимация: выпад вперёд вдоль направления к цели и возврат назад.
     */
    data class Spear(
        val maxOffset: Float = 0.6f,
        val startOffset: Float = 0f,
        val peakScale: Float = 0.08f,
        val shakeAmplitude: Float = 0.025f,
        /** Угол спрайта в покое относительно направления к цели (радианы) */
        val restAngleOffset: Float = 0f
    ) : AnimationType()

    /**
     * Thrust с поворотом к курсору в начале анимации.
     * Направление укола определяется углом к курсору на момент начала действия.
     * Флип по X: если курсор левее игрока — изображение отражается.
     */
    data class DirectionalThrust(
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