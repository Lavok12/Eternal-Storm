package la.vok.Game.GameController

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.v

class HighlightRender(var gameRender: GameRender) : Controller {
    var targetMinePoint: LPoint? = null
    var targetPlacePoint: LPoint? = null
    var targetItem: HandItem? = null

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
        val dim = item.entity.dimension!!

        targetMinePoint = if (item.descriptor.renderMineHighlight) targetPoint else null

        targetPlacePoint =
            if (item.descriptor.renderPlaceHighlight && !mapApi.tileIsActive(dim, targetPoint.x, targetPoint.y)
            ) {
                targetPoint
            } else null

        targetItem = gameRender.gameController.playerControl.getPlayerEntity()?.handItemComponent?.currentHandItem

        if (!gameRender.gameController.gameCycle.mapApi.isInsideMap(dim, targetPoint.x, targetPoint.y)) targetMinePoint = null
        if (!gameRender.gameController.gameCycle.mapApi.isInsideMap(dim, targetPlacePoint?.x ?: 100000000, targetPlacePoint?.y ?: 10000000)) targetPlacePoint = null

    }

    fun render(lg: LGraphics, camera: Camera) {
        super.renderTick()

        if (gameRender.gameController.wGamePanel?.insideUxElement(gameRender.gameController.playerControl.getTarget()) ?: true) {
            return
        }
        if (targetMinePoint != null) {
            val player = gameRender.gameController.playerControl.getPlayerEntity()!!
            val dim = player.dimension!!
            val mapTile = gameRender.gameController.gameCycle.mapApi.getTileType(dim, targetMinePoint!!.x, targetMinePoint!!.y)

            if (mapTile != null) {
                var tileContext = TileContext(dimension = dim)

                tileContext.hp =
                    gameRender.gameController.gameCycle.mapApi.getTileHp(dim, targetMinePoint!!.x, targetMinePoint!!.y)
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

            val mapApi = gameRender.gameController.gameCycle.mapApi

            val player = gameRender.gameController.playerControl.getPlayerEntity()!!
            val dim = player.dimension!!
            lg.fill(255f,100f)
            var e = targetItem?.item?.itemType?.usingVariants
            when (e) {
                UsingVariants.Custom -> {}
                is UsingVariants.PlaceTile -> {
                    if (mapApi.canPlaceTile(dim, mapApi.getRegisteredTileType(e.tileTag), targetPlacePoint!!.x, targetPlacePoint!!.y)) {
                        lg.fill(255f,100f)
                        lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
                    } else {
                        lg.fill(200f, 100f, 100f,100f)
                        lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
                    }
                }
                is UsingVariants.PlaceWall -> {
                    if (mapApi.canPlaceWall(dim, mapApi.getRegisteredWallType(e.wallTag), targetPlacePoint!!.x, targetPlacePoint!!.y)) {
                        lg.fill(255f, 100f)
                        lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
                    } else {
                        if (!mapApi.wallIsActive(dim, targetPlacePoint!!.x, targetPlacePoint!!.y)) {
                            lg.fill(200f, 100f, 100f, 100f)
                            lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
                        }
                    }
                }
                null -> {}
            }
        }
    }
}