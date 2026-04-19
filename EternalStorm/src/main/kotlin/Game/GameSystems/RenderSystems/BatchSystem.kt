package la.vok.Game.GameSystems.RenderSystems

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState
import kotlin.math.floor

abstract class BatchSystem(val gameController: GameController) {

    private var batchGrid: Array<Array<RenderBatch>> = Array(0) { Array(0) { RenderBatch(0, 0) } }
    private var currentGridW = 0
    private var currentGridH = 0

    open fun getResolution(): Float = AppState.batchBlockResolution
    open fun getPadding(): Int = 0

    private fun checkGridSize() {
        if (currentGridW != AppState.batchGridWidth || currentGridH != AppState.batchGridHeight) {
            batchGrid = Array(AppState.batchGridWidth) { Array(AppState.batchGridHeight) { RenderBatch(-Int.MAX_VALUE, -Int.MAX_VALUE) } }
            currentGridW = AppState.batchGridWidth
            currentGridH = AppState.batchGridHeight
            markAllDirty()
        }
    }

    fun markDirty(blockX: Int, blockY: Int) {
        if (currentGridW == 0 || currentGridH == 0) return
        val cx = floor(blockX.toFloat() / AppState.batchChunkSize).toInt()
        val cy = floor(blockY.toFloat() / AppState.batchChunkSize).toInt()
        
        val arrX = (cx % currentGridW + currentGridW) % currentGridW
        val arrY = (cy % currentGridH + currentGridH) % currentGridH
        
        val batch = batchGrid[arrX][arrY]
        if (batch.chunkX == cx && batch.chunkY == cy) {
            batch.isDirty = true
        }
    }

    fun markAllDirty() {
        if (currentGridW == 0 || currentGridH == 0) return
        for (x in 0 until currentGridW) {
            for (y in 0 until currentGridH) {
                batchGrid[x][y].isDirty = true
            }
        }
    }

    fun clear() {
        if (currentGridW == 0 || currentGridH == 0) return
        for (x in 0 until currentGridW) {
            for (y in 0 until currentGridH) {
                batchGrid[x][y].clear()
                batchGrid[x][y].chunkX = -Int.MAX_VALUE
                batchGrid[x][y].chunkY = -Int.MAX_VALUE
            }
        }
    }

    fun isClean(cx: Int, cy: Int): Boolean {
        if (currentGridW == 0 || currentGridH == 0) return false
        val arrX = (cx % currentGridW + currentGridW) % currentGridW
        val arrY = (cy % currentGridH + currentGridH) % currentGridH
        val batch = batchGrid[arrX][arrY]
        return batch.chunkX == cx && batch.chunkY == cy && !batch.isDirty && batch.lg != null
    }

    protected abstract fun drawChunkBlocks(
        chunkX: Int, chunkY: Int,
        startX: Int, startY: Int,
        endX: Int, endY: Int,
        batchLg: LGraphics,
        dim: AbstractDimension,
        blockSizeX: Float, blockSizeY: Float
    )

    fun tick(camera: Camera, dim: AbstractDimension) {
        checkGridSize()
        val mapApi = gameController.gameCycle.mapApi

        val p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        val p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))
        
        val minX = minOf(p1.x, p2.x) - 1
        val maxX = maxOf(p1.x, p2.x) + 1
        val minY = minOf(p1.y, p2.y) - 1
        val maxY = maxOf(p1.y, p2.y) + 1

        var startChunkX = floor(minX.toFloat() / AppState.batchChunkSize).toInt()
        var endChunkX = floor(maxX.toFloat() / AppState.batchChunkSize).toInt()
        var startChunkY = floor(minY.toFloat() / AppState.batchChunkSize).toInt()
        var endChunkY = floor(maxY.toFloat() / AppState.batchChunkSize).toInt()
        
        val viewW = endChunkX - startChunkX + 1
        val viewH = endChunkY - startChunkY + 1
        
        if (viewW > currentGridW) {
            val centerCX = startChunkX + viewW / 2
            startChunkX = centerCX - currentGridW / 2
            endChunkX = startChunkX + currentGridW - 1
        }
        
        if (viewH > currentGridH) {
            val centerCY = startChunkY + viewH / 2
            startChunkY = centerCY - currentGridH / 2
            endChunkY = startChunkY + currentGridH - 1
        }

        val currentTime = System.currentTimeMillis()

        for (cx in startChunkX..endChunkX) {
            for (cy in startChunkY..endChunkY) {
                
                val arrX = (cx % currentGridW + currentGridW) % currentGridW
                val arrY = (cy % currentGridH + currentGridH) % currentGridH
                
                val batch = batchGrid[arrX][arrY]

                if (batch.chunkX != cx || batch.chunkY != cy) {
                    batch.chunkX = cx
                    batch.chunkY = cy
                    batch.isDirty = true
                }

                batch.lastUsedTime = currentTime

                if (batch.lg == null || batch.isDirty) {
                    if (gameController.gameCycle.batchApi.batchesUpdatedThisFrame < AppState.maxBatchUpdatesPerFrame) {
                        gameController.gameCycle.batchApi.batchesUpdatedThisFrame++
                        
                        val padding = getPadding()
                        val res = getResolution()
                        val pixelSize = ((AppState.batchChunkSize + padding * 2) * res).toInt()

                        if (batch.lg == null || batch.lg!!.windowWidth != pixelSize) {
                            batch.lg = LGraphics(pixelSize, pixelSize, pixelSize.toFloat(), pixelSize.toFloat())
                        }
                        
                        batch.lg!!.beginDraw()
                        batch.lg!!.pg.clear()
                        
                        val startDrawX = cx * AppState.batchChunkSize - padding
                        val startDrawY = cy * AppState.batchChunkSize - padding
                        val endDrawX = startDrawX + AppState.batchChunkSize + padding * 2 - 1
                        val endDrawY = startDrawY + AppState.batchChunkSize + padding * 2 - 1

                        drawChunkBlocks(cx, cy, startDrawX, startDrawY, endDrawX, endDrawY, batch.lg!!, dim, res, res)
                        
                        batch.lg!!.endDraw()
                        batch.isDirty = false
                    }
                }
            }
        }
    }

    fun render(lg: LGraphics, camera: Camera, dim: AbstractDimension): HashSet<Pair<Int, Int>> {
        checkGridSize()
        val mapApi = gameController.gameCycle.mapApi
        val skippedChunks = HashSet<Pair<Int, Int>>()

        val p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        val p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))
        
        val minX = minOf(p1.x, p2.x) - 1
        val maxX = maxOf(p1.x, p2.x) + 1
        val minY = minOf(p1.y, p2.y) - 1
        val maxY = maxOf(p1.y, p2.y) + 1

        var startChunkX = floor(minX.toFloat() / AppState.batchChunkSize).toInt()
        var endChunkX = floor(maxX.toFloat() / AppState.batchChunkSize).toInt()
        var startChunkY = floor(minY.toFloat() / AppState.batchChunkSize).toInt()
        var endChunkY = floor(maxY.toFloat() / AppState.batchChunkSize).toInt()
        
        val viewW = endChunkX - startChunkX + 1
        val viewH = endChunkY - startChunkY + 1
        
        if (viewW > currentGridW) {
            val centerCX = startChunkX + viewW / 2
            startChunkX = centerCX - currentGridW / 2
            endChunkX = startChunkX + currentGridW - 1
        }
        
        if (viewH > currentGridH) {
            val centerCY = startChunkY + viewH / 2
            startChunkY = centerCY - currentGridH / 2
            endChunkY = startChunkY + currentGridH - 1
        }

        for (cx in startChunkX..endChunkX) {
            for (cy in startChunkY..endChunkY) {
                
                val arrX = (cx % currentGridW + currentGridW) % currentGridW
                val arrY = (cy % currentGridH + currentGridH) % currentGridH
                
                val batch = batchGrid[arrX][arrY]

                // If chunk is dirty or not allocated, we shouldn't draw its old texture. It'll be drawn dynamically by GameRender instead!
                if (batch.isDirty || batch.lg == null || batch.chunkX != cx || batch.chunkY != cy) {
                    skippedChunks.add(cx to cy)
                    continue
                }

                val chunkWorldX = cx * AppState.batchChunkSize + (AppState.batchChunkSize - 1) / 2f
                val chunkWorldY = cy * AppState.batchChunkSize + (AppState.batchChunkSize - 1) / 2f
                
                val screenCx = camera.useCameraPosX(chunkWorldX)
                val screenCy = camera.useCameraPosY(chunkWorldY)
                
                val padding = getPadding()
                val res = getResolution()
                
                // Draw only the internal chunk part (crop out the padding)
                val screenW = camera.useCameraSizeX(AppState.batchChunkSize.toFloat())
                val screenH = camera.useCameraSizeY(AppState.batchChunkSize.toFloat())
                
                val sx = (padding * res).toInt()
                val sy = (padding * res).toInt()
                val sw = (AppState.batchChunkSize * res).toInt()
                val sh = (AppState.batchChunkSize * res).toInt()

                lg.setImage(batch.lg!!.pg, screenCx, screenCy, screenW, screenH, sx, sy, sw, sh)
            }
        }
        
        return skippedChunks
    }
}
