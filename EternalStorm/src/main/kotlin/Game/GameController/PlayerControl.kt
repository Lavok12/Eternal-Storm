package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.LavokLibrary.Vectors.Vec2

class PlayerControl(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = gameController.gameCycle.entityApi
    val mapApi: MapApi get() = gameController.gameCycle.mapApi
    val playerId: Long get() = gameController.playerId

    init {
        create()
    }

    override fun physicTick() {
        super.physicTick()
        if (!entityApi.containsEntityById(playerId)) return
        if (entityApi.getById(playerId) !is PlayerEntity) return
        gameController.mainCamera.setCameraPos(getPlayerEntity()!!.position)
    }
    fun getPlayerEntity() : PlayerEntity? {
        if (!entityApi.containsEntityById(playerId)) return null
        if (entityApi.getById(playerId) !is PlayerEntity) return null
        return entityApi.getById(playerId) as PlayerEntity
    }
    fun isControl() : Boolean {
        return getPlayerEntity() != null
    }

    fun tapA() {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        player.playerControlComponent.moveLeft()
    }
    fun tapD() {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        player.playerControlComponent.moveRight()
    }

    fun tapSpace() {
        if (!isControl()) return
        val player = getPlayerEntity() ?: return
        player.playerControlComponent.tryJump()
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
}