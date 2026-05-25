package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Ai.PlayerAI
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.LavokLibrary.Vectors.Vec2

class PlayerControl(var gameController: GameController) : Controller {
    val playerApi get() = gameCycle.playerApi
    val gameCycle get() = gameController.gameCycle
    val entityApi get() = gameCycle.entityApi
    val mapApi get() = gameCycle.mapApi
    val playerId get() = gameController.playerId

    var isInventoryOpen = false
        private set

    fun toggleInventory() {
        isInventoryOpen = !isInventoryOpen
    }

    init {
        create()
    }

    fun getTarget() : Vec2 {
        return gameController.coreController.mouseInput.logicalPosition
    }
    fun getPlayerItem() : HandItem? {
        if (!isControl()) return null
        val player = getPlayerEntity() ?: return null
        return player.handItemComponent.currentHandItem
    }
    override fun physicTick() {
        super.physicTick()
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        gameController.mainCamera.setCameraPos(player.position)
    }
    fun getPlayerEntity() : PlayerEntity? {
        return playerApi.getPlayer(playerId)
    }
    fun isControl() : Boolean {
        return getPlayerEntity() != null
    }

    fun tapA() {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        (player.ai as PlayerAI).moveLeft()
    }
    fun tapD() {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        (player.ai as PlayerAI).moveRight()
    }

    fun tapSpace() {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        (player.ai as PlayerAI).tryJump()
    }

    fun leftPressed(position: Vec2) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.handItemComponent.leftPressed(position)
    }

    fun rightPressed(position: Vec2) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.handItemComponent.rightPressed(position)
    }

    fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.handItemComponent.leftUpdate(position, oldPosition)
    }

    fun rightUpdate(position: Vec2, oldPosition: Vec2) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.handItemComponent.rightUpdate(position, oldPosition)
    }

    fun leftReleased(position: Vec2) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.handItemComponent.leftReleased()
    }

    fun rightReleased(position: Vec2) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.handItemComponent.rightReleased()
    }

    var heldItem: Item? = null
        set(value) {
            field = value
            syncHandItem()
        }

    var selectedSlotId: Int = 0
        private set

    fun syncHandItem() {
        val player = getPlayerEntity() ?: return
        if (heldItem != null) {
            player.handItemComponent.setItem(heldItem)
        } else {
            player.handItemComponent.setItem(player.inventory?.itemContainer?.getItem(selectedSlotId))
        }
    }

    fun chooseSlot(id: Int) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        selectedSlotId = id
        player.inventory?.choose(id, player.handItemComponent)
        syncHandItem() // Ensure heldItem takes priority if present
    }

    fun interact(position: Vec2, type: BlockInteractionType) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        
        val worldPos = gameController.mainCamera.toWorldPos(position)
        val blockPoint = mapApi.getPointFromPos(worldPos)
        
        mapApi.handleInteraction(player.dimension, blockPoint.x, blockPoint.y, type, player)
    }
}