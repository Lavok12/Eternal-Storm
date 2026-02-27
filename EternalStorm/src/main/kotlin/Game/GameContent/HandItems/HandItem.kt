package la.vok.Game.GameContent.HandItems

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.Vec2

open class HandItem(
    var handItemComponent: HandItemComponent,
    open val descriptor: HandItemDescriptor = HandItemDescriptor(spriteName = "default.png")
) {
    val gameCycle: GameCycle get() = handItemComponent.gameCycle
    val gameController: GameController get() = gameCycle.gameController
    val gameRender: GameRender get() = gameController.gameRender
    val mapApi: MapApi get() = gameCycle.mapApi
    val entityApi: EntityApi get() = gameCycle.entityApi

    val entity: Entity = handItemComponent.entity

    open var handItemRender = HandItemRender(this, gameRender.renderLayer)

    var useStage = 0f
    var block = false
    private var activeAction: UseAction = UseAction.None
    private var actionTriggered = false

    fun toWorldPos(pos: Vec2): Vec2 = gameController.mainCamera.toWorldPos(pos)
    fun getHandPos(): Vec2 = entity.position + handItemComponent.deltaWithFacing
    fun getVisualHandPos(): Vec2 = gameController.mainCamera.useCamera(getHandPos())

    open fun leftPressed(pos: Vec2) {
        if (!block) startUse(descriptor.leftAction)
    }

    open fun rightPressed(pos: Vec2) {
        if (!block) startUse(descriptor.rightAction)
    }

    open fun leftUpdate(pos: Vec2, oldPosition: Vec2) {}
    open fun rightUpdate(pos: Vec2, oldPosition: Vec2) {}

    private fun startUse(action: UseAction) {
        if (action is UseAction.None) return
        block = true
        activeAction = action
        actionTriggered = false
        onActionStart(action)
    }

    private fun onActionStart(action: UseAction) {
        when (action) {
            is UseAction.PrintOnStart -> println(action.text)
            is UseAction.Custom -> action.onStart?.invoke(this)
            else -> {}
        }
    }

    private fun onActionProgress(action: UseAction, progress: Float) {
        when (action) {
            is UseAction.PrintText -> if (!actionTriggered) {
                println(action.text)
                actionTriggered = true
            }
            is UseAction.PrintAtProgress -> if (!actionTriggered && progress >= action.atProgress) {
                println(action.text)
                actionTriggered = true
            }
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
            is UseAction.PrintOnEnd -> println(action.text)
            is UseAction.Custom -> action.onEnd?.invoke(this)
            else -> {}
        }
    }

    open fun show() { handItemRender.show() }
    open fun hide() { handItemRender.hide() }
    open fun activate() {}
    open fun deactivate() {}

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
            }
        }
    }

    open fun renderUpdate() {
        handItemRender.ROI_pos = getHandPos()
        handItemRender.facing = entity.facing
        handItemRender.useStage = useStage.toFloat()
    }
}