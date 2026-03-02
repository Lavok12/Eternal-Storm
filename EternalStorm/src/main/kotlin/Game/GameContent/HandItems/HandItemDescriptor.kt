package la.vok.Game.GameContent.HandItems

import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

data class HandItemDescriptor(
    val spriteName: String,
    val spriteSize: Vec2 = 1 v 1,
    val renderDelta: Vec2 = Vec2.ZERO,
    val useDuration: Float = 100f,
    val useStageStep: Float = 1f,
    val animationType: AnimationType = AnimationType.Swing(),
    val renderLayer: Int = 1,
    val spriteAngle: Float = 0f,
    val leftAction: UseAction = UseAction.None,
    val rightAction: UseAction = UseAction.None,
    val blockFacing: Boolean = true,
    val changeFacingToTarget: Boolean = false,

    val autoRepeat: Boolean = false,
    val renderMineHighlight: Boolean = false,
    val renderPlaceHighlight: Boolean = false,

    ) {
}

