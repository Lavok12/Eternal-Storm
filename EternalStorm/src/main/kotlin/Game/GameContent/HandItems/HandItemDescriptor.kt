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
    val spriteAngle: Float = 0f
)

sealed class AnimationType {

    /** Замах — вращение вокруг точки руки */
    data class Swing(
        // угол взмаха в радианах (~117° по умолчанию)
        val swingAngle: Float = Math.PI.toFloat() * 0.65f,
        // начальный угол перед замахом (отрицательный = сначала отводит назад)
        val startAngle: Float = 0f,
        // выдвижение руки вперёд во время взмаха (в тайлах)
        val lungeDistance: Float = 0.15f,
        // увеличение спрайта на пике удара (0.18 = +18%)
        val peakScale: Float = 0.18f,
        // амплитуда вибрации после удара (в тайлах)
        val shakeAmplitude: Float = 0.04f
    ) : AnimationType()

    /** Укол — движение вперёд-назад */
    data class Thrust(
        // максимальное смещение вперёд (в тайлах, отрицательное = сначала отдёргивает назад)
        val maxOffset: Float = 0.5f,
        // начальное смещение перед выпадом (отрицательное = замах назад)
        val startOffset: Float = 0f,
        // увеличение спрайта на пике выпада (0.1 = +10%)
        val peakScale: Float = 0.1f,
        // амплитуда вибрации при отдаче (в тайлах)
        val shakeAmplitude: Float = 0.03f
    ) : AnimationType()

    /** Просто держится без анимации */
    object Idle : AnimationType()

    /** Своя анимация через лямбду */
    data class Custom(
        val draw: HandItemRender.(
            lg: la.vok.LavokLibrary.LGraphics.LGraphics,
            pos: Vec2,
            size: Vec2,
            camera: la.vok.Core.CoreContent.Camera.Camera
        ) -> Unit
    ) : AnimationType()
}