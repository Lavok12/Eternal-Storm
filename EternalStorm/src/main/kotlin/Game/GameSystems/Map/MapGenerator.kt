package la.vok.Game.GameSystems.Map

import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameContent.TilesList
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState

class MapGenerator(var mapController: MapController) {

    val mapApi: MapApi get() = mapController.mapApi

    private val width = 100
    private val height = 100

    fun create(mapSystem: MapSystem) {
        AppState.logger.debug("GenerateMap")

        generateTerrain()
        generateCaves()
        generateOre()
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
                    mapApi.placeTile(
                        mapApi.getRegisteredTile(TilesList.grass),
                        x p y
                    )
                }

                y > surfaceY - 4 -> {
                    mapApi.placeTile(
                        mapApi.getRegisteredTile(TilesList.dirt),
                        x p y
                    )
                }

                else -> {
                    mapApi.placeTile(
                        mapApi.getRegisteredTile(TilesList.stone),
                        x p y
                    )
                }
            }
        }
    }

    // --------------------------------------------------------
    // 🕳 CAVES
    // --------------------------------------------------------

    private fun generateCaves() {

        val caveCount = 15

        repeat(caveCount) {

            var cx = AppState.main.random(10f, 90f).toInt()
            var cy = AppState.main.random(40f, 90f).toInt()

            val length = AppState.main.random(20f, 60f).toInt()

            repeat(length) {

                carveCircle(cx, cy, AppState.main.random(2f, 4f).toInt())

                cx += AppState.main.random(-1f, 1f).toInt()
                cy += AppState.main.random(-1f, 1f).toInt()

                cx = cx.coerceIn(2, width - 3)
                cy = cy.coerceIn(2, height - 3)
            }
        }
    }

    private fun carveCircle(cx: Int, cy: Int, radius: Int) {
        for (x in cx - radius..cx + radius) {
            for (y in cy - radius..cy + radius) {

                if (x !in 0 until width || y !in 0 until height) continue

                val dx = x - cx
                val dy = y - cy

                if (dx * dx + dy * dy <= radius * radius) {
                    mapApi.deleteTile(x, y) // воздух
                }
            }
        }
    }

    // --------------------------------------------------------
    // ⛏ ORE
    // --------------------------------------------------------

    private fun generateOre() {

        val oreVeins = 20

        repeat(oreVeins) {

            val startX = AppState.main.random(5f, 95f).toInt()
            val startY = AppState.main.random(50f, 95f).toInt()

            val veinSize = AppState.main.random(4f, 10f).toInt()

            for (i in 0 until veinSize) {

                val x = startX + AppState.main.random(-2f, 2f).toInt()
                val y = startY + AppState.main.random(-2f, 2f).toInt()

            }
        }
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }
}