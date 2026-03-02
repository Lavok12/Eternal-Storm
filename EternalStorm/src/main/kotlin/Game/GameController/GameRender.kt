package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameController.HighlightRender
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

class GameRender(val gameController: GameController) : Controller {
    var highlightRender = HighlightRender(this)

    val coreController: CoreController
        get() {return gameController.coreController}

    init {
        create()
    }

    val renderLayer = LayersRenderContainer(this)

    override fun logicalTick() {
        super.logicalTick()
        highlightRender.logicalTick()
    }

    fun render(lg: LGraphics, camera: Camera) {
        lg.bg(100f,150f,200f)
        renderLayer.drawLayer(RenderLayers.Main.B1, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B2, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B3, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B4, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B5, lg, camera)

        val mapApi = gameController.gameCycle.mapController.mapApi
        val mapSystem = gameController.gameCycle.mapController.mapSystem

        var p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        var p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))


        var tileContext = TileContext()
        forEachInArea(p1, p2, 1) { ix, iy ->
            val mapTile = mapSystem.getTileType(ix, iy) ?: return@forEachInArea

            if (mapSystem.containsTile(ix, iy)) {
                tileContext.hp = mapApi.getTileHp(ix, iy)
                tileContext.positionX = ix
                tileContext.positionY = iy
                tileContext.tileType = mapTile

                var tilePos = mapApi.getTilePos(ix p iy)
                var tileSize = mapApi.getTileSize()
                mapTile.render(tileContext, lg, camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1), gameController)
            }
        }

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

        if (!gameController.gameCycle.entityApi.containsEntityById(gameController.playerControl.playerId)) {
            lg.fill(250f, 50f, 50f)
            for (i in 0..255) {
                lg.setText("АХАХХАХАХА УМЕР", 0f, 0f, 100f)
            }
        }
    }
}
