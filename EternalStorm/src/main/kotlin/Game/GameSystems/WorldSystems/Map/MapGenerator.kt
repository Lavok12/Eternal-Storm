package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameContent.TilesList
import la.vok.State.AppState

class MapGenerator(var mapController: MapController) {

    val mapApi: MapApi get() = mapController.mapApi

    private val width = 100
    private val height = 100

    fun create(mapSystem: MapSystem) {
        AppState.logger.debug("GenerateMap")

        generateTerrain()
    }

    // --------------------------------------------------------
    // 🌍 TERRAIN
    // --------------------------------------------------------

    private fun generateTerrain() {

        val baseHeight = 60f
        val amplitude = 10f
        val segmentSize = 10

        var previousHeight = baseHeight

        for (segment in 0 until width step segmentSize) {

            val nextHeight =
                baseHeight + AppState.main.random(-amplitude, amplitude)

            for (i in 0 until segmentSize) {

                val x = segment + i
                if (x >= width) break

                val t = i / segmentSize.toFloat()
                val surfaceY = lerp(previousHeight, nextHeight, t).toInt()

                generateColumn(x, surfaceY)
            }

            previousHeight = nextHeight
        }
    }

    private fun generateColumn(x: Int, surfaceY: Int) {

        for (y in 0 until height) {

            when {
                y > surfaceY -> {
                    // воздух
                }

                y == surfaceY -> {
                    mapApi.generateTile(
                        TilesList.grass_block,
                        x, y
                    )
                }

                y > surfaceY - 4 -> {
                    mapApi.generateTile(
                        TilesList.dirt_block,
                        x, y
                    )
                }

                else -> {
                    mapApi.generateTile(
                        TilesList.stone_block,
                        x, y
                    )
                }
            }
        }
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }
}