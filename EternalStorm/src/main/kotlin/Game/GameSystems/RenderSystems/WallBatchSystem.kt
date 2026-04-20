package la.vok.Game.GameSystems.RenderSystems

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState

class WallBatchSystem(gameController: GameController) : BatchSystem(gameController) {
    override fun drawChunkBlocks(
        chunkX: Int,
        chunkY: Int,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        batchLg: LGraphics,
        dim: AbstractDimension,
        blockSizeX: Float,
        blockSizeY: Float
    ) {
        val mapSystem = dim.mapSystem
        val chunkCenterX = chunkX * AppState.batchChunkSize + (AppState.batchChunkSize - 1) / 2f
        val chunkCenterY = chunkY * AppState.batchChunkSize + (AppState.batchChunkSize - 1) / 2f

        AppState.isBatchRendering = true
        for (ix in startX..endX) {
            for (iy in startY..endY) {
                if (mapSystem.containsTile(ix, iy)) {
                    var tileType = mapSystem.getTileType(ix, iy)
                    if (tileType != null && tileType.isDummy) {
                        tileType = mapSystem.getTileType(ix + tileType.masterOffset.x, iy + tileType.masterOffset.y)
                    }
                    if (tileType == null || !tileType.renderConfig.renderWallsBehind) continue
                }
                val wallType = mapSystem.getWallType(ix, iy) ?: continue
                
                if (!wallType.useBatchLayer) continue

                val localX = ix - chunkCenterX
                val localY = iy - chunkCenterY

                val cx = localX * blockSizeX
                val cy = localY * blockSizeY

                wallType.render(
                    ix, iy, batchLg,
                    cx, cy,
                    blockSizeX, blockSizeY,
                    dim, gameController
                )
            }
        }
        AppState.isBatchRendering = false
    }
}
