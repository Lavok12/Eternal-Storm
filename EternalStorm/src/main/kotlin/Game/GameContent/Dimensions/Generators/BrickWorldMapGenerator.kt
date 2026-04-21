package la.vok.Game.GameContent.Dimensions.Generators

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class BrickWorldMapGenerator(dimension: AbstractDimension) : AbstractMapGenerator(dimension) {

    override fun generateTerrain() {
        // 1. Create a basic floor
        for (x in 0 until width) {
            mapApi.generateTile(dimension, TilesList.stone_block, x, 99)
            mapApi.generateTile(dimension, TilesList.stone_block, x, 98)
        }

        // 2. Prepare 12 materials (11 bricks + 1 plank)
        val materials = mutableListOf(
            TilesList.wooden_brick_tile to WallList.wooden_brick_wall,
            TilesList.stone_brick_tile to WallList.stone_brick_wall,
            TilesList.copper_brick_tile to WallList.copper_brick_wall,
            TilesList.tin_brick_tile to WallList.tin_brick_wall,
            TilesList.bronze_brick_tile to WallList.bronze_brick_wall,
            TilesList.iron_brick_tile to WallList.iron_brick_wall,
            TilesList.magical_brick_tile to WallList.magical_brick_wall,
            TilesList.celestial_brick_tile to WallList.celestial_brick_wall,
            TilesList.cosmic_brick_tile to WallList.cosmic_brick_wall,
            TilesList.wind_brick_tile to WallList.wind_brick_wall,
            TilesList.solar_brick_tile to WallList.solar_brick_wall,
            TilesList.plank_block to WallList.plank_wall // Added for balance
        )

        // 3. Spawn rooms in 2 floors (6 per row) - Flush together
        val roomsPerRow = 6
        val roomW = 12
        val roomH = 8

        for (i in 0 until materials.size) {
            val (tileMat, wallMat) = materials[i]
            val row = i / roomsPerRow
            val col = i % roomsPerRow
            
            val rx = 50 + col * roomW
            val ry = 100 + row * roomH
            
            generateRoom(rx, ry, roomW, roomH, tileMat, wallMat)
        }
    }

    private fun generateRoom(x: Int, y: Int, w: Int, h: Int, tileMaterial: String, wallMaterial: String) {
        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val nx = x + dx
                val ny = y + dy
                
                // Inside walls
                if (dx > 0 && dx < w - 1 && dy > 0 && dy < h - 1) {
                    mapApi.generateWall(dimension, wallMaterial, nx, ny)
                    mapApi.deleteTile(dimension, nx, ny)
                } 
                // Outer bricks
                else {
                    mapApi.generateTile(dimension, tileMaterial, nx, ny)
                }
            }
        }
        
        // Add door gaps (3 blocks high) on both sides
        for (i in 1..3) {
            // Left door
            mapApi.deleteTile(dimension, x, y + i)
            mapApi.generateWall(dimension, wallMaterial, x, y + i)
            
            // Right door
            mapApi.deleteTile(dimension, x + w - 1, y + i)
            mapApi.generateWall(dimension, wallMaterial, x + w - 1, y + i)
        }
    }
}
