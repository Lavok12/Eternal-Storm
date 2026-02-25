package la.vok.Core.GameControllers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.LGraphics.LGraphics
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

    override fun logicalTick() {
        superTick()
    }


    fun render(lg: LGraphics, camera: Camera) {
        bgObjects.drawLayer(RenderLayers.Main.A1, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A2, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A3, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A4, lg, camera)
        bgObjects.drawLayer(RenderLayers.Main.A5, lg, camera)

        val mapApi = gameController.mapController.mapApi
        val mapSystem = gameController.mapController.mapSystem

        var p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        var p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))


        forEachInArea(p1, p2, 1) { ix, iy ->
            val mapTile = mapSystem.getMapTile(ix, iy) ?: return@forEachInArea

            if (mapTile.isActive) {
                var tilePos = mapApi.getTilePos(mapTile.tile!!.position)
                var tileSize = mapApi.getTileSize()
                mapTile.tile!!.render(lg, camera.useCamera(tilePos), camera.useCameraSize(tileSize) + (1 v 1))
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
