package la.vok.Game.GameSystems.EntityComponents

import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class HandItemComponent(
    entity: Entity,
    var delta: Vec2 = 0 v 0
) : EntityComponent(entity) {

    val gameCycle: GameCycle get() = entity.gameCycle
    val gameRender: GameRender get() = gameCycle.gameController.gameRender

    val deltaWithFacing: Vec2
        get() = delta.inverted(entity.facing == -1, false)

    var currentHandItem: HandItem? = null
    var pendingHandItem: HandItem? = null
    private var changeRequested = false


    private fun setHandItem(handItem: HandItem?) {
        if (handItem === currentHandItem) return
        pendingHandItem = handItem
        changeRequested = true
    }
    fun setItem(item: Item?) {
        setHandItem(item?.getHandItem(this))
    }

    fun clearHandItem() {
        setHandItem(null)
    }


    private fun tryApplyChange() {
        if (!changeRequested) return

        val canChange = currentHandItem == null || currentHandItem?.block == false
        if (!canChange) return

        currentHandItem?.hide()
        currentHandItem?.deactivate()

        currentHandItem = pendingHandItem
        pendingHandItem = null

        currentHandItem?.show()
        currentHandItem?.activate()

        changeRequested = false
    }

    open fun checkItemsCount() {
        if (currentHandItem?.item?.count == 0) {
            currentHandItem?.hide()
            currentHandItem?.deactivate()
            currentHandItem = null
        }
        if (pendingHandItem?.item?.count == 0) {
            pendingHandItem = null
            changeRequested = false
        }
    }

    open fun renderUpdate() {
        checkItemsCount()
        currentHandItem?.renderUpdate()
    }

    open fun physicUpdate() {
        checkItemsCount()
        tryApplyChange()
        currentHandItem?.physicUpdate()
        updateFacingToTarget()
    }

    private fun updateFacingToTarget() {
        val handItem = currentHandItem ?: return

        if (!(handItem.descriptor.changeFacingToTarget)) return

        entity.facing =
            if (targetWorldPos().x > entity.position.x) 1
            else -1
    }

    open fun leftReleased() { currentHandItem?.leftReleased() }
    open fun rightReleased() { currentHandItem?.rightReleased() }

    open fun leftPressed(position: Vec2) {
        checkItemsCount()
        currentHandItem?.leftPressed(position)
    }

    open fun rightPressed(position: Vec2) {
        checkItemsCount()
        currentHandItem?.rightPressed(position)
    }

    open fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        checkItemsCount()
        currentHandItem?.leftUpdate(position, oldPosition)
    }

    open fun rightUpdate(position: Vec2, oldPosition: Vec2) {
        checkItemsCount()
        currentHandItem?.rightUpdate(position, oldPosition)
    }

    fun isFacingBlocked(): Boolean {
        if ((currentHandItem?.descriptor?.changeFacingToTarget?: false)) return true
        if (currentHandItem?.block ?: false && (currentHandItem?.descriptor?.blockFacing?: false)) {
            return true
        }
        return false
    }

    fun screenToWorld(screenPos: Vec2): Vec2 =
        gameCycle.gameController.mainCamera.toWorldPos(screenPos)

    fun worldToMap(worldPos: Vec2): LPoint =
        gameCycle.gameController.gameCycle.mapApi.getPointFromPos(worldPos)

    fun screenToMap(screenPos: Vec2): LPoint =
        worldToMap(screenToWorld(screenPos))

    fun targetScreenPos(): Vec2 =
        gameCycle.gameController.playerControl.getTarget()

    fun targetWorldPos(): Vec2 =
        screenToWorld(targetScreenPos())

    fun targetMapPos(): LPoint =
        worldToMap(targetWorldPos())

    fun entityWorldPos(entity: Entity): Vec2 =
        entity.position

    fun entityMapPos(entity: Entity): LPoint =
        worldToMap(entity.position)

    fun targetDirection(): Vec2 =
        (targetWorldPos() - getHandPos()).normalized()

    fun getHandPos(): Vec2 = entity.position + deltaWithFacing
    fun getVisualHandPos(): Vec2 = gameCycle.gameController.mainCamera.useCamera(getHandPos())

}