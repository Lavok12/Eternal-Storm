package la.vok.Game.GameSystems.WorldSystems.Dimensions.Generators

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.State.AppState

class StoneWorldMapGenerator(dimension: AbstractDimension) : AbstractMapGenerator(dimension) {

    override fun generateTerrain() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                // Stone is the base
                var tileType = TilesList.stone_block
                
                // Randomly spawn ores
                val rand = AppState.main.random(100f)
                if (rand < 2f) {
                    tileType = TilesList.diamond_ore_block // 2% chance
                } else if (rand < 7f) {
                    tileType = TilesList.gold_ore_block // 5% chance
                }

                mapApi.generateTile(dimension, tileType, x, y)
            }
        }
    }
}
