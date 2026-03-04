package la.vok.Game.GameContent.HandItems

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.atan2

open class HandItem(
    var item: Item,
    var handItemComponent: HandItemComponent,
    open val descriptor: HandItemDescriptor = HandItemDescriptor(spriteName = "default.png")
) {
    val gameCycle: GameCycle get() = handItemComponent.gameCycle
    val gameController: GameController get() = gameCycle.gameController
val gameRender: GameRender get() = gameController.gameRender

    val entity: Entity = handItemComponent.entity

    open var handItemRender = HandItemRender(this, gameRender.renderLayer)

    var useStage = 0f
    var block = false

    private var activeAction: UseAction = UseAction.None
    private var actionTriggered = false

    private var leftHeld = false
    private var rightHeld = false

    open fun leftPressed(pos: Vec2) {
        leftHeld = true
        if (!block) startUse(descriptor.leftAction)
    }

    open fun rightPressed(pos: Vec2) {
        rightHeld = true
        if (!block) startUse(descriptor.rightAction)
    }

    open fun leftReleased() { leftHeld = false }
    open fun rightReleased() { rightHeld = false }

    open fun leftUpdate(pos: Vec2, oldPosition: Vec2) {}
    open fun rightUpdate(pos: Vec2, oldPosition: Vec2) {}

    private fun startUse(action: UseAction) {
        if ((entity.ai?.targetScreenPos()?.x ?: 0f) < entity.position.x) {
            entity.changeFacing(-1)
        }
        if ((entity.ai?.targetScreenPos()?.x ?: 0f) > entity.position.x) {
            entity.changeFacing(1)
        }
        if (action is UseAction.None) return

        when (descriptor.animationType) {
            is AnimationType.Spear,
            is AnimationType.DirectionalThrust -> {
                val target  = entity.ai?.targetWorldPos() ?: Vec2.ZERO
                val handPos = handItemComponent.getHandPos()
                val dx = target.x - handPos.x
                val dy = target.y - handPos.y
                handItemRender.lockedAimAngle = atan2(dy, dx)
            }
            else -> {}
        }

        block = true
        activeAction = action
        actionTriggered = false
        onActionStart(action)
    }

    private fun onActionStart(action: UseAction) {
        when (action) {
            is UseAction.Custom -> action.onStart?.invoke(this, gameController.wGamePanel!!.mousePosition)
            else -> {}
        }
    }

    private fun onActionProgress(action: UseAction, progress: Float) {
        when (action) {
            is UseAction.Custom -> {
                action.onProgress?.invoke(this, progress)
                if (!actionTriggered && action.triggerAt != null && progress >= action.triggerAt) {
                    actionTriggered = true
                }
            }
            else -> {}
        }
    }

    private fun onActionEnd(action: UseAction) {
        when (action) {
            is UseAction.Custom -> action.onEnd?.invoke(this)
            else -> {}
        }
    }

    open fun show() { handItemRender.show() }
    open fun hide() { handItemRender.hide() }
    open fun activate() {}
    open fun deactivate() {
        leftHeld = false
        rightHeld = false
    }

    open fun physicUpdate() {
        if (block) {
            val buffSystem = entity.buffController
            val multiplier = when (descriptor.speedType) {
                SpeedMultiplierType.Melee -> buffSystem.meleeAttackSpeedMultiplier
                SpeedMultiplierType.Ranged -> buffSystem.rangedAttackSpeedMultiplier
                SpeedMultiplierType.DiggingTile -> buffSystem.diggingTileSpeedMultiplier
                SpeedMultiplierType.DiggingWall -> buffSystem.diggingWallSpeedMultiplier
                SpeedMultiplierType.PlacingTile -> buffSystem.placingBlockSpeedMultiplier
                SpeedMultiplierType.PlacingWall -> buffSystem.placingWallSpeedMultiplier

                else -> 1.0f
            }
            useStage += descriptor.useStageStep * multiplier

            val progress = useStage / descriptor.useDuration
            onActionProgress(activeAction, progress)
            if (useStage > descriptor.useDuration) {
                onActionEnd(activeAction)

                activeAction = UseAction.None
                block = false
                useStage = 0f
                actionTriggered = false

                if (descriptor.autoRepeat) {
                    when {
                        leftHeld  -> startUse(descriptor.leftAction)
                        rightHeld -> startUse(descriptor.rightAction)
                    }
                }
            }
        }
    }

    open fun renderUpdate() {
        handItemRender.ROI_pos = handItemComponent.getHandPos()
        handItemRender.facing = entity.facing
        handItemRender.useStage = useStage.toFloat()

        // обновляем угол каждый кадр для направленных анимаций
        when (descriptor.animationType) {
            is AnimationType.Spear,
            is AnimationType.DirectionalThrust -> {
                if (!block) {
                    val target = entity.ai?.targetWorldPos() ?: Vec2.ZERO
                    val handPos = handItemComponent.getHandPos()
                    val dx = target.x - handPos.x
                    val dy = target.y - handPos.y
                    handItemRender.lockedAimAngle = atan2(dy, dx)
                }
            }
            else -> {}
        }
    }
}