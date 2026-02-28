package la.vok.Game.GameContent.HandItems

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

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

    open fun toWorldPos(pos: Vec2): Vec2 = gameController.mainCamera.toWorldPos(pos)
    open fun toMapPos(pos: Vec2): LPoint = gameController.gameCycle.mapApi.getPointFromPos(pos)
    open fun targetWorldPos() : LPoint = toMapPos(toWorldPos(gameController.wGamePanel?.mousePosition ?: (0 v 0)))
    open fun targetPos() : Vec2 = toWorldPos(gameController.wGamePanel?.mousePosition ?: (0 v 0))


    fun getHandPos(): Vec2 = entity.position + handItemComponent.deltaWithFacing
    fun getVisualHandPos(): Vec2 = gameController.mainCamera.useCamera(getHandPos())

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
        if (targetPos().x < entity.position.x) {
            entity.changeFacing(-1)
        }
        if (targetPos().x > entity.position.x) {
            entity.changeFacing(1)
        }
        if (action is UseAction.None) return
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
            useStage += descriptor.useStageStep
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
                        leftHeld -> startUse(descriptor.leftAction)
                        rightHeld -> startUse(descriptor.rightAction)
                    }
                }
            }
        }
    }

    open fun renderUpdate() {
        handItemRender.ROI_pos = getHandPos()
        handItemRender.facing = entity.facing
        handItemRender.useStage = useStage.toFloat()
    }
}