package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Ai.PlayerAI
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.LavokLibrary.Vectors.Vec2

class PlayerControl(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = gameController.gameCycle.entityApi
    val mapApi: MapApi get() = gameController.gameCycle.mapApi
    val playerId: Long get() = gameController.playerId

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
        if (!entityApi.containsEntityByIdAcrossDimensions(playerId)) return null
        if (entityApi.getByIdAcrossDimensions(playerId) !is PlayerEntity) return null
        return entityApi.getByIdAcrossDimensions(playerId) as PlayerEntity
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

    fun chooseSlot(id: Int) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return

        player.inventory?.choose(id, player.handItemComponent)
    }

    fun interact(position: Vec2, type: BlockInteractionType) {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        
        val worldPos = gameController.mainCamera.toWorldPos(position)
        val blockPoint = mapApi.getPointFromPos(worldPos)
        
        mapApi.handleInteraction(player.dimension, blockPoint.x, blockPoint.y, type, player)
    }
}