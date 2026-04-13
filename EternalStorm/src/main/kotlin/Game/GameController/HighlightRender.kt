package la.vok.Game.GameController

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.*

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
        
        if (targetMinePoint != null) {
            val tileType = mapApi.getTileType(dim, targetMinePoint!!.x, targetMinePoint!!.y)
            if (tileType != null && tileType.isDummy) {
                targetMinePoint = LPoint(targetPoint.x + tileType.masterOffset.x, targetPoint.y + tileType.masterOffset.y)
            }
        }

        val handItem = gameRender.gameController.playerControl.getPlayerEntity()?.handItemComponent?.currentHandItem
        val currentTileType = (handItem?.item?.itemType?.usingVariants as? UsingVariants.PlaceTile)?.let { mapApi.getRegisteredTileType(it.tileTag) }

        targetPlacePoint =
            if (item.descriptor.renderPlaceHighlight) {
                val offset = currentTileType?.placeOffset ?: (0 p 0)
                val pt = LPoint(targetPoint.x + offset.x, targetPoint.y + offset.y)
                if (!mapApi.tileIsActive(dim, pt.x, pt.y)) pt else null
            } else null

        targetItem = handItem

        if (!mapApi.isInsideMap(dim, targetMinePoint?.x ?: 0, targetMinePoint?.y ?: 0)) targetMinePoint = null
        if (!mapApi.isInsideMap(dim, targetPlacePoint?.x ?: 100000000, targetPlacePoint?.y ?: 10000000)) targetPlacePoint = null

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
                var tileSize = gameRender.gameController.gameCycle.mapApi.getBlockSize().copy()
                tileSize.x *= mapTile.width
                tileSize.y *= mapTile.height

                val offset = Vec2((mapTile.width / 2).toFloat(), (mapTile.height / 2).toFloat())
                
                mapTile.renderHighlight(
                    tileContext,
                    lg,
                    camera.useCamera(tilePos + offset),
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
                    val tileType = mapApi.getRegisteredTileType(e.tileTag)
                    val displaySize = tileSize.copy()
                    displaySize.x *= tileType.width
                    displaySize.y *= tileType.height

                    val offset = Vec2((tileType.width / 2).toFloat(), (tileType.height / 2).toFloat())

                    if (mapApi.canPlaceTile(dim, tileType, targetPlacePoint!!.x, targetPlacePoint!!.y)) {
                        lg.fill(255f, 150f)
                        lg.setBlock(camera.useCamera(tilePos + offset), camera.useCameraSize(displaySize) + (1 v 1))
                    } else {
                        lg.fill(255f, 50f, 50f, 150f)
                        lg.setBlock(camera.useCamera(tilePos + offset), camera.useCameraSize(displaySize) + (1 v 1))
                    }
                }
                is UsingVariants.PlaceWall -> {
                    if (mapApi.canPlaceWall(dim, mapApi.getRegisteredWallType(e.wallTag), targetPlacePoint!!.x, targetPlacePoint!!.y)) {
                        lg.fill(255f, 150f)
                        lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
                    } else {
                        if (!mapApi.wallIsActive(dim, targetPlacePoint!!.x, targetPlacePoint!!.y)) {
                            lg.fill(255f, 50f, 50f, 150f)
                            lg.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
                        }
                    }
                }
                null -> {}
            }
        }
    }
}