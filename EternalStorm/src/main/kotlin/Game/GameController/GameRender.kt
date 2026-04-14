package la.vok.Core.GameControllers

import com.jogamp.nativewindow.util.Dimension
import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.FrameLimiter
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameController.HighlightRender
import la.vok.Game.GameSystems.EffectLayers.AOTiles
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

class GameRender(val gameController: GameController) : Controller {
    var highlightRender = HighlightRender(this)

    var aOTiles: AOTiles? = null

    val coreController: CoreController
        get() = gameController.coreController

    init {
        create()
    }

    val renderLayer = LayersRenderContainer(this)

    override fun renderTick() {
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
        highlightRender.logicalTick()
    }

    lateinit var dim: AbstractDimension

    fun render(lg: LGraphics, camera: Camera) {
        if (gameController.playerControl.getPlayerEntity() != null) {
            dim = gameController.playerDimension!!
        }

        lg.bg(dim.skyColor)
        val mapApi = gameController.gameCycle.mapApi
        val mapSystem = dim.mapController.mapSystem

        // Pre-calculate visible area in world coordinates
        val p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        val p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))

        // Pre-calculate block scale once per frame
        val blockSizeX = camera.useCameraSizeX(1f)
        val blockSizeY = camera.useCameraSizeY(1f)

        // 1. Render Walls
        forEachInArea(p1, p2, 1) { ix, iy ->
            if (mapSystem.containsTile(ix, iy)) return@forEachInArea
            val wallType = mapSystem.getWallType(ix, iy) ?: return@forEachInArea

            if (mapSystem.containsWall(ix, iy)) {
                val cx = camera.useCameraPosX(ix.toFloat())
                val cy = camera.useCameraPosY(iy.toFloat())
                
                wallType.render(
                    ix, iy, lg,
                    cx, cy,
                    blockSizeX, blockSizeY,
                    dim, gameController
                )
            }
        }

        // Draw background layers
        renderLayer.drawLayer(RenderLayers.Main.B1, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B2, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B3, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B4, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B5, lg, camera)

        if (aOTiles!!.containsImage()) {
            lg.setImage(aOTiles!!.getImage(), 0f, 0f, lg.windowWidth.toFloat(), lg.windowHeight.toFloat())
        }

        // 2. Render Tiles
        forEachInArea(p1, p2, 1) { ix, iy ->
            val tileType = mapSystem.getTileType(ix, iy) ?: return@forEachInArea

            if (mapSystem.containsTile(ix, iy)) {
                // Offset calculation for centered tiles (handling multi-tiles if needed)
                val cx = camera.useCameraPosX(ix.toFloat() + (tileType.width - 1) / 2f)
                val cy = camera.useCameraPosY(iy.toFloat() + (tileType.height - 1) / 2f)

                tileType.render(
                    ix, iy, lg,
                    cx, cy,
                    blockSizeX, blockSizeY,
                    dim, gameController
                )
            }
        }

        dim.particleSystem.render(lg, camera)
        highlightRender.render(lg, camera)

        // Draw foreground layers
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
            lg.setTextAlign(0, 0)
            lg.setText("GAME OVER - YOU DIED", 0f, 0f, 100f)
        }
    }
}
