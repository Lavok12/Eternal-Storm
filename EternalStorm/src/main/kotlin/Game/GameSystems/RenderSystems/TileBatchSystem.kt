package la.vok.Game.GameSystems.RenderSystems

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState

class TileBatchSystem(gameController: GameController) : BatchSystem(gameController) {
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

        val drawnMasters = HashSet<Long>()
        
        AppState.isBatchRendering = true
        for (ix in startX..endX) {
            for (iy in startY..endY) {
                if (!mapSystem.containsTile(ix, iy)) continue
                val tile = mapSystem.getTileType(ix, iy) ?: continue
                
                // 1. Find the master for this tile
                val mx = ix + tile.masterOffset.x
                val my = iy + tile.masterOffset.y
                
                // 2. Hash the master credentials to avoid redundant draws
                val mHash = (mx.toLong() shl 32) or (my.toLong() and 0xFFFFFFFFL)
                if (drawnMasters.contains(mHash)) continue
                
                val masterTile = if (tile.isDummy) mapSystem.getTileType(mx, my) else tile
                if (masterTile == null || !masterTile.renderConfig.useBatchLayer) continue

                // 3. Render the MASTER at its origin position
                val localX = mx + (masterTile.width - 1) / 2f - chunkCenterX
                val localY = my + (masterTile.height - 1) / 2f - chunkCenterY

                val cx = localX * blockSizeX
                val cy = localY * blockSizeY

                masterTile.render(
                    mx, my, batchLg,
                    cx, cy,
                    blockSizeX, blockSizeY,
                    dim, gameController
                )
                
                drawnMasters.add(mHash)
            }
        }
        AppState.isBatchRendering = false
    }
}
