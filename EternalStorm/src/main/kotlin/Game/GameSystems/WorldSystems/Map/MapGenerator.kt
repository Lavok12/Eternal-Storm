package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState

class MapGenerator(var mapController: MapController) {

    val mapApi: MapApi get() = mapController.mapApi

    private val width = 300
    private val height = 100

    fun create(mapSystem: MapSystem) {
        AppState.logger.debug("GenerateMap")
        generateTerrain()
    }

    // --------------------------------------------------------
    // 🌍 TERRAIN
    // --------------------------------------------------------

    private val surfaceY = height / 2

    private fun generateTerrain() {
        for (x in 0 until width) {
            generateColumn(x, surfaceY)
        }
    }

    private fun generateColumn(x: Int, surfaceY: Int) {
        for (y in 0 until height) {
            when {
                y > surfaceY -> {}

                y == surfaceY -> mapApi.generateTile(TilesList.grass_block, x, y)

                y > surfaceY - 4 -> mapApi.generateTile(TilesList.dirt_block, x, y)

                else -> mapApi.generateTile(TilesList.stone_block, x, y)
            }
        }
        for (y in 0 until height) {
            when {
                y > surfaceY -> {}

                y <= surfaceY -> mapApi.generateWall(WallList.dirt_wall, x, y)
            }
        }
    }
}