package la.vok.Core.GameControllers

import com.jogamp.nativewindow.util.Dimension
import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.FrameLimiter
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameController.HighlightRender
import la.vok.Game.GameSystems.RenderSystems.EffectLayers.AOTiles
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
        // PGraphics manipulation shouldn't be interlaced with main rendering
        if (la.vok.State.AppState.enableBatching && gameController.mainCamera.size <= la.vok.State.AppState.batchMaxZoom && gameController.playerDimension != null) {
            gameController.gameCycle.batchApi.tick(gameController.mainCamera, gameController.playerDimension!!)
        }

        if (aOTiles == null) {
            aOTiles = AOTiles(this, gameController.wGamePanel!!.logicalSize.toLPoint())
        }

        if (!la.vok.State.AppState.enableBatching || gameController.mainCamera.size > la.vok.State.AppState.batchMaxZoom) {
            aOTiles!!.camera = gameController.mainCamera
            if (FrameLimiter.totalRenderFrames % 2f == 0f) {
                aOTiles!!.draw = true
            }
            aOTiles!!.tick()
        }
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
        if (la.vok.State.AppState.enableBatching && camera.size <= la.vok.State.AppState.batchMaxZoom) {
            val skippedWallChunks = gameController.gameCycle.batchApi.wallBatchSystem.render(lg, camera, dim)
            forEachInArea(p1, p2, 1) { ix, iy ->
                if (!mapSystem.containsWall(ix, iy)) return@forEachInArea
                val wallType = mapSystem.getWallType(ix, iy) ?: return@forEachInArea
                
                val chunkX = kotlin.math.floor(ix.toFloat() / la.vok.State.AppState.batchChunkSize).toInt()
                val chunkY = kotlin.math.floor(iy.toFloat() / la.vok.State.AppState.batchChunkSize).toInt()
                
                if (!wallType.useBatchLayer || skippedWallChunks.contains(chunkX to chunkY)) {
                    val cx = camera.useCameraPosX(ix.toFloat())
                    val cy = camera.useCameraPosY(iy.toFloat())
                    wallType.render(ix, iy, lg, cx, cy, blockSizeX, blockSizeY, dim, gameController)
                }
            }
        } else {
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
        }

        // Draw background layers
        renderLayer.drawLayer(RenderLayers.Main.B1, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B2, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B3, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B4, lg, camera)
        renderLayer.drawLayer(RenderLayers.Main.B5, lg, camera)

        if (la.vok.State.AppState.enableBatching && camera.size <= la.vok.State.AppState.batchMaxZoom) {
            gameController.gameCycle.batchApi.aoBatchSystem.render(lg, camera, dim)
        } else {
            if (aOTiles!!.containsImage()) {
                lg.setImage(aOTiles!!.getImage(), 0f, 0f, lg.windowWidth.toFloat(), lg.windowHeight.toFloat())
            }
        }

        // 2. Render Tiles
        if (la.vok.State.AppState.enableBatching && camera.size <= la.vok.State.AppState.batchMaxZoom) {
            val skippedTileChunks = gameController.gameCycle.batchApi.tileBatchSystem.render(lg, camera, dim)
            forEachInArea(p1, p2, 1) { ix, iy ->
                if (!mapSystem.containsTile(ix, iy)) return@forEachInArea
                val tileType = mapSystem.getTileType(ix, iy) ?: return@forEachInArea

                val cx = camera.useCameraPosX(ix.toFloat() + (tileType.width - 1) / 2f)
                val cy = camera.useCameraPosY(iy.toFloat() + (tileType.height - 1) / 2f)
                
                val chunkX = kotlin.math.floor(ix.toFloat() / la.vok.State.AppState.batchChunkSize).toInt()
                val chunkY = kotlin.math.floor(iy.toFloat() / la.vok.State.AppState.batchChunkSize).toInt()

                if (!tileType.renderConfig.useBatchLayer || skippedTileChunks.contains(chunkX to chunkY)) {
                    tileType.render(ix, iy, lg, cx, cy, blockSizeX, blockSizeY, dim, gameController)
                } else if (tileType.renderConfig.renderBreakProgress && tileType.breakProgress(ix, iy, dim, gameController) > 0f) {
                    val baseW = blockSizeX * tileType.width
                    val baseH = blockSizeY * tileType.height
                    var finalW = baseW * tileType.renderConfig.sizeMultiplier
                    var finalH = baseH * tileType.renderConfig.sizeMultiplier
                    if (tileType.renderConfig.useSquareRender) {
                        val s = kotlin.math.min(finalW, finalH)
                        finalW = s
                        finalH = s
                    }
                    val offsetX = (baseW - finalW) / 2f + tileType.renderConfig.renderDelta.x * blockSizeX
                    val offsetY = (baseH - finalH) / 2f + tileType.renderConfig.renderDelta.y * blockSizeY

                    tileType.renderBreakProgress(ix, iy, lg, cx + offsetX, cy + offsetY, finalW, finalH, dim, gameController)
                }
            }
        } else {
            forEachInArea(p1, p2, 1) { ix, iy ->
                val tileType = mapSystem.getTileType(ix, iy) ?: return@forEachInArea

                if (mapSystem.containsTile(ix, iy)) {
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

        if (la.vok.State.AppState.debugBatchGrid) {
            lg.strokeWeight(1f)
            lg.noFill()
            
            val minX = kotlin.math.floor(p1.x.toFloat() / la.vok.State.AppState.batchChunkSize).toInt() - 1
            val maxX = kotlin.math.floor(p2.x.toFloat() / la.vok.State.AppState.batchChunkSize).toInt() + 1
            val minY = kotlin.math.floor(p1.y.toFloat() / la.vok.State.AppState.batchChunkSize).toInt() - 1
            val maxY = kotlin.math.floor(p2.y.toFloat() / la.vok.State.AppState.batchChunkSize).toInt() + 1
            
            for (cx in minX..maxX) {
                for (cy in minY..maxY) {
                    val isClean = gameController.gameCycle.batchApi.tileBatchSystem.isClean(cx, cy)
                    if (isClean) {
                        lg.stroke(0f, 255f, 0f, 180f) // Green outline
                        lg.fill(0f, 255f, 0f, 50f)    // Green tint
                    } else {
                        lg.stroke(255f, 0f, 0f, 180f) // Red outline
                        lg.fill(255f, 0f, 0f, 50f)    // Red tint
                    }

                    val cwX = cx * la.vok.State.AppState.batchChunkSize - 0.5f
                    val cwY = cy * la.vok.State.AppState.batchChunkSize - 0.5f
                    val scX = camera.useCameraPosX(cwX + la.vok.State.AppState.batchChunkSize / 2f)
                    val scY = camera.useCameraPosY(cwY + la.vok.State.AppState.batchChunkSize / 2f)
                    val scW = camera.useCameraSizeX(la.vok.State.AppState.batchChunkSize.toFloat())
                    val scH = camera.useCameraSizeY(la.vok.State.AppState.batchChunkSize.toFloat())
                    lg.setBlock(scX, scY, scW, scH)
                }
            }
            lg.noStroke()
        }
    }
}
