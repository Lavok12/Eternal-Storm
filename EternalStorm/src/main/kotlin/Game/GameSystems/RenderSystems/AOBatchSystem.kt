package la.vok.Game.GameSystems.RenderSystems

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState

class AOBatchSystem(gameController: GameController) : BatchSystem(gameController) {
    override fun getResolution(): Float = AppState.batchAoResolution
    
    override fun getPadding(): Int = 1

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
        
        // World coordinates of the chunk center
        val chunkCenterX = chunkX * AppState.batchChunkSize + (AppState.batchChunkSize - 1) / 2f
        val chunkCenterY = chunkY * AppState.batchChunkSize + (AppState.batchChunkSize - 1) / 2f

        batchLg.noStroke()
        val drawnMasters = HashSet<Long>()
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
                if (masterTile == null || !masterTile.renderConfig.AOShadow) continue

                // 3. Draw shadow for the whole block area
                for (dx in 0 until masterTile.width) {
                    for (dy in 0 until masterTile.height) {
                        val tx = mx + dx
                        val ty = my + dy
                        
                        // Local coordinates relative to chunk center
                        val localX = tx - chunkCenterX
                        val localY = ty - chunkCenterY
                        val cx = localX * blockSizeX
                        val cy = localY * blockSizeY

                        batchLg.fill(0f, AppState.batchAoAlpha)
                        batchLg.setBlock(cx, cy, blockSizeX + 1f, blockSizeY + 1f)
                    }
                }
                drawnMasters.add(mHash)
            }
        }
        
        val shader = gameController.coreController.shaderLoader.getValue("blur.glsl")
        val pg = batchLg.pg
        shader.set("dis", pg.width.toFloat(), pg.height.toFloat())
        
        val p1 = (AppState.batchAoResolution / 4f) * AppState.batchAoBlurMultiplier
        val p2 = (AppState.batchAoResolution / 8f) * AppState.batchAoBlurMultiplier
        
        shader.set("power", p1)
        pg.filter(shader)
        shader.set("power", p2)
        pg.filter(shader)
    }
}
