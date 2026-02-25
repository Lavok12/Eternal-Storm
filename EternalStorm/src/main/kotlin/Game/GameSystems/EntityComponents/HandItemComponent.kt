package la.vok.Game.GameSystems.EntityComponents

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class HandItemComponent(
    entity: Entity,
    var delta: Vec2 = 0 v 0
) : EntityComponent(entity) {

    val gameController: GameController get() = entity.gameController
    val gameRender: GameRender get() = gameController.gameRender

    val deltaWithFacing: Vec2
        get() = delta.inverted(entity.facing == -1, false)

    private var currentHandItem: HandItem? = null
    private var pendingHandItem: HandItem? = null
    private var changeRequested = false


    fun setHandItem(handItem: HandItem?) {
        if (handItem === currentHandItem) return
        pendingHandItem = handItem
        changeRequested = true
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


    open fun renderUpdate() {
        currentHandItem?.renderUpdate()
    }

    open fun physicUpdate() {
        tryApplyChange()
        currentHandItem?.physicUpdate()
    }


    open fun leftPressed(position: Vec2) {
        currentHandItem?.leftPressed(position)
    }

    open fun rightPressed(position: Vec2) {
        currentHandItem?.rightPressed(position)
    }

    open fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        currentHandItem?.leftUpdate(position, oldPosition)
    }

    open fun rightUpdate(position: Vec2, oldPosition: Vec2) {
        currentHandItem?.rightUpdate(position, oldPosition)
    }
}