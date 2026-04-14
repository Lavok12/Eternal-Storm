package la.vok.Game.GameContent.Tiles.System

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Map.MapController
import la.vok.LavokLibrary.Vectors.*
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.State.AppState

class BigTestBlockType : AbstractTileType() {
    override val tag: String = TilesList.big_test_block
    override val texture: String = "3x3.png"
    override val maxHp: Int = 100
    override val blockStrength: Int = 1
    
    override val width: Int = 3 
    override val height: Int = 3
    override val placeOffset: LPoint = -1 p -1

    override fun place(x: Int, y: Int, item: Item, mapController: MapController) {
        val reg = mapController.dimension.gameCycle.gameController.coreController.objectRegistration
        
        for (dx in 0 until width) {
            for (dy in 0 until height) {
                if (dx == 0 && dy == 0) continue
                
                val nx = x + dx
                val ny = y + dy
                
                if (mapController.mapSystem.isInside(nx, ny)) {
                    val dummyTag = AppState.addNamespace("multi_tile_dummy_${-dx}_${-dy}")
                    val dummyType = reg.tiles[dummyTag] ?: MultiTileDummyType(dummyTag, LPoint(-dx, -dy)).also {
                        reg.registrationTileType(it)
                    }
                    
                    mapController.mapSystem.setTileType(nx, ny, dummyType)
                    mapController.mapSystem.setTileHp(nx, ny, 1)
                }
            }
        }
    }
    
    override fun onRemoved(
        x: Int,
        y: Int,
        dimension: AbstractDimension,
        mapController: MapController,
        reason: Any?
    ) {
        // Spawn particles across the whole 3x3 area
        for (dx in 0 until width) {
            for (dy in 0 until height) {
                spawnTileParticle(x + dx, y + dy, dimension, mapController)
            }
        }
    }
}
