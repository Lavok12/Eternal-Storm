package la.vok.Game.GameController

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.v

class HighlightRender(var gameRender: GameRender) : Controller {
    var targetMinePoint: LPoint? = null
    var targetPlacePoint: LPoint? = null

    init {
        create()
    }

    override fun logicalTick() {

        val item = gameRender.gameController.playerControl.getPlayerItem()

        if (item == null) {
            targetMinePoint = null
            targetPlacePoint = null
            return
        }

        val mapApi = gameRender.gameController.gameCycle.mapApi
        val targetPoint = item.entity.ai?.targetMapPos() ?: LPoint.ZERO

        targetMinePoint = if (item.descriptor.renderMineHighlight) targetPoint else null

        targetPlacePoint =
            if (item.descriptor.renderPlaceHighlight && !mapApi.tileIsActive(targetPoint.x, targetPoint.y)
            ) {
                targetPoint
            } else null

        if (!gameRender.gameController.gameCycle.mapApi.isInsideMap(targetPoint.x, targetPoint.y)) targetMinePoint = null
        if (!gameRender.gameController.gameCycle.mapApi.isInsideMap(targetPlacePoint?.x ?: 100000000, targetPlacePoint?.y ?: 10000000)) targetPlacePoint = null

    }

    fun render(lg: LGraphics, camera: Camera) {
        super.renderTick()

        if (gameRender.gameController.wGamePanel?.insideUxElement(gameRender.gameController.playerControl.getTarget()) ?: true) {
            return
        }
        if (targetMinePoint != null) {

            val mapTile = gameRender.gameController.gameCycle.mapApi.getTileType(targetMinePoint!!.x, targetMinePoint!!.y)

            if (mapTile != null) {
                var tileContext = TileContext()

                tileContext.hp =
                    gameRender.gameController.gameCycle.mapApi.getTileHp(targetMinePoint!!.x, targetMinePoint!!.y)
                tileContext.positionX = targetMinePoint!!.x
                tileContext.positionY = targetMinePoint!!.y
                tileContext.tileType = mapTile

                var tilePos = gameRender.gameController.gameCycle.mapApi.getBlockPos(targetMinePoint!!)
                var tileSize = gameRender.gameController.gameCycle.mapApi.getBlockSize()
                mapTile.renderHighlight(
                    tileContext,
                    lg,
                    camera.useCamera(tilePos),
                    camera.useCameraSize(tileSize) + (1 v 1),
                    gameRender,
                )
            } else {
                var tilePos = gameRender.gameController.gameCycle.mapApi.getBlockPos(targetMinePoint!!)
                var tileSize = gameRender.gameController.gameCycle.mapApi.getBlockSize()

                lg.stroke(0f)
                lg.strokeWeight(3f)
                lg.fill(0f, 0f)
                lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize)-3f)
                lg.noStroke()
            }
        }
        if (targetPlacePoint != null) {
            var tilePos = gameRender.gameController.gameCycle.mapApi.getBlockPos(targetPlacePoint!!)
            var tileSize = gameRender.gameController.gameCycle.mapApi.getBlockSize()
            lg.fill(255f,100f)
            lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
        }
    }
}