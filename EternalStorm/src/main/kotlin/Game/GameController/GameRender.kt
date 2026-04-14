package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.FrameLimiter
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameController.HighlightRender
import la.vok.Game.GameSystems.EffectLayers.AOTiles
import la.vok.Game.GameSystems.WorldSystems.Map.WallContext
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

class GameRender(val gameController: GameController) : Controller {
    var highlightRender = HighlightRender(this)

    var aOTiles: AOTiles? = null


    val coreController: CoreController
        get() {return gameController.coreController}

    init {
        create()
    }

    val renderLayer = LayersRenderContainer(this)

    override fun renderTick() {
        super.renderTick()

        if (aOTiles == null) {
            aOTiles = AOTiles(this, gameController.wGamePanel!!.logicalSize.toLPoint())
        }

        aOTiles!!.camera = gameController.mainCamera
        if (FrameLimiter.totalRenderFrames % 2f == 0f) {
            aOTiles!!.draw = true
        }
        aOTiles!!.tick()
    }
    override fun logicalTick() {
        super.logicalTick()
        highlightRender.logicalTick()
    }

    fun render(lg: LGraphics, camera: Camera) {
        if (gameController.playerControl.getPlayerEntity() == null) return
        val dim = gameController.playerDimension!!
        lg.bg(dim.skyColor)
        val mapApi = gameController.gameCycle.mapApi
        val mapSystem = dim.mapController.mapSystem

        var p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        var p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))

        var wallContext = WallContext(dimension = dim)
        forEachInArea(p1, p2, 1) { ix, iy ->
            if (mapSystem.containsTile(ix, iy)) return@forEachInArea
            val mapTile = mapSystem.getWallType(ix, iy) ?: return@forEachInArea

            if (mapSystem.containsWall(ix, iy)) {
                wallContext.hp = mapApi.getWallHp(dim, ix, iy)
                wallContext.positionX = ix
                wallContext.positionY = iy
                wallContext.wallType = mapTile

                var tilePos = mapApi.getBlockPos(ix p iy)
                var tileSize = mapApi.getBlockSize()
                mapTile.render(wallContext, lg, camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1), gameController)
            }
        }

        renderLayer.drawLayer(RenderLayers.Main.B1, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B2, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B3, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B4, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B5, lg, camera)

        if (aOTiles!!.containsImage()) {
            lg.setImage(aOTiles!!.getImage(), 0 v 0, lg.windowWidth v lg.windowHeight)
        }

        var tileContext = TileContext(dimension = dim)
        forEachInArea(p1, p2, 1) { ix, iy ->
            val mapTile = mapSystem.getTileType(ix, iy) ?: return@forEachInArea

            if (mapSystem.containsTile(ix, iy)) {
                tileContext.hp = mapApi.getTileHp(dim, ix, iy)
                tileContext.positionX = ix
                tileContext.positionY = iy
                tileContext.tileType = mapTile

                var tilePos = mapApi.getBlockPos(ix p iy)
                var tileSize = mapApi.getBlockSize()
                mapTile.render(tileContext, lg, camera.useCamera(tilePos + Vec2(
                    (tileContext.tileType!!.width/2) v (tileContext.tileType!!.height/2)
                    )
                ), camera.useCameraSize(tileSize) + (1 v 1), gameController)
            }
        }

        dim.particleSystem.render(lg, camera)
        highlightRender.render(lg, camera)

        renderLayer.drawLayer(RenderLayers.Main.A1, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.A2, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.A3, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.A4, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.A5, lg, camera)

        renderLayer.drawLayer(RenderLayers.Main.C1, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.C2, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.C3, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.C4, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.C5, lg, camera)

        if (!gameController.gameCycle.entityApi.containsEntityById(dim, gameController.playerControl.playerId)) {
            lg.fill(250f, 50f, 50f)
            for (i in 0..255) {
                lg.setText("АХАХХАХАХА УМЕР", 0f, 0f, 100f)
            }
        }
    }
}
