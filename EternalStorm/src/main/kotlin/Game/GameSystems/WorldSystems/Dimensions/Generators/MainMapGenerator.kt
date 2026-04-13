package la.vok.Game.GameSystems.WorldSystems.Dimensions.Generators

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.State.AppState

class MainMapGenerator(dimension: AbstractDimension) : AbstractMapGenerator(dimension) {

    private val surfaceY = height / 2

    override fun generateTerrain() {
        for (x in 0 until width) {
            generateColumn(x, surfaceY)
        }
    }

    private fun generateColumn(x: Int, surfaceY: Int) {
        for (y in 0 until height) {
            when {
                y == surfaceY + 1 -> {
                    if (AppState.main.random(-10f, 100f) < 0f) {
                        for (i in 0..AppState.main.random(10f, 25f).toInt()) {
                            mapApi.generateTile(dimension, TilesList.tree_part_block, x, y + i)
                        }
                    }
                }

                y > surfaceY -> {}

                y == surfaceY -> mapApi.generateTile(dimension, TilesList.grass_block, x, y)

                y > surfaceY - 4 -> mapApi.generateTile(dimension, TilesList.dirt_block, x, y)

                else -> mapApi.generateTile(dimension, TilesList.stone_block, x, y)
            }
        }
        for (y in 0 until height) {
            when {
                y > surfaceY -> {}
                y <= surfaceY -> mapApi.generateWall(dimension, WallList.dirt_wall, x, y)
            }
        }
    }
}
