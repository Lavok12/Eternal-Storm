package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

class GameRender(val gameController: GameController) : Controller {
    val coreController: CoreController
        get() {return gameController.coreController}

    companion object {
        enum class Layers {
            bgObjects,
            mapObjects,
            gameObjects,
        }
    }

    init {
        create()
    }

    val bgObjects = LayersRenderContainer(this, RenderLayers.Main::class.java)
    val mapObjects = LayersRenderContainer(this, RenderLayers.Main::class.java)
    val gameObjects = LayersRenderContainer(this, RenderLayers.Main::class.java)

    fun getContainer(layer: Layers): LayersRenderContainer<*> = when(layer) {
        Layers.bgObjects -> bgObjects
        Layers.mapObjects -> mapObjects
        Layers.gameObjects -> gameObjects
    }
    fun getLayer(obj: Any) : Layers {
        return when (obj) {
            bgObjects -> {
                Layers.bgObjects
            }
            mapObjects -> {
                Layers.mapObjects
            }
            gameObjects -> {
                Layers.gameObjects
            }
            else -> {
                Layers.bgObjects
            }
        }
    }
    fun getEntityContainer(): LayersRenderContainer<RenderLayers.Main> {
        return gameObjects
    }

    fun render(lg: LGraphics, camera: Camera) {
        bgObjects.drawLayer(RenderLayers.Main.A1, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A2, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A3, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A4, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A5, lg, camera)

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

        mapObjects.drawLayer(RenderLayers.Main.A1, lg, camera)
        mapObjects.drawLayer(RenderLayers.Main.A2, lg, camera)
        mapObjects.drawLayer(RenderLayers.Main.A3, lg, camera)
        mapObjects.drawLayer(RenderLayers.Main.A4, lg, camera)
        mapObjects.drawLayer(RenderLayers.Main.A5, lg, camera)

        gameObjects.drawLayer(RenderLayers.Main.A1, lg, camera)
        gameObjects.drawLayer(RenderLayers.Main.A2, lg, camera)
        gameObjects.drawLayer(RenderLayers.Main.A3, lg, camera)
        gameObjects.drawLayer(RenderLayers.Main.A4, lg, camera)
        gameObjects.drawLayer(RenderLayers.Main.A5, lg, camera)

    }
}
