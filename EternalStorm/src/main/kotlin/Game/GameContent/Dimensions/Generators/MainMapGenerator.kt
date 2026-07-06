package la.vok.Game.GameContent.Dimensions.Generators

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Tiles.System.MultiTileDummyType
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState

class MainMapGenerator(dimension: AbstractDimension) : AbstractMapGenerator(dimension) {

    private val p = AppState.main
    private val noiseScaleRelief = 0.012f
    private val noiseScaleCaves = 0.035f
    private val surfaceLevel = height * 0.7f // Базовый уровень поверхности (снизу вверх)
    private val caveThreshold = 0.38f

    override fun generateTerrain() {
        val smoothDesertFactors = FloatArray(width) { x ->
            val biomeNoise = p.noise(x * 0.003f)
            val desertFactor = ((biomeNoise - 0.4f) / 0.2f).coerceIn(0f, 1f)
            desertFactor * desertFactor * (3f - 2f * desertFactor)
        }

        val heightMap = IntArray(width) { x ->
            val factor = smoothDesertFactors[x]
            val normalHeight = surfaceLevel + (p.noise(x * noiseScaleRelief) - 0.5f) * 160f
            val desertHeight = surfaceLevel + (p.noise(x * (noiseScaleRelief * 0.5f) + 1000f) - 0.5f) * 70f
            (normalHeight * (1f - factor) + desertHeight * factor).toInt()
        }

        // 1. Основной ландшафт и пещеры
        for (x in 0 until width) {
            val surfY = heightMap[x]
            val factor = smoothDesertFactors[x]
            val soilDepth = (5 * (1f - factor) + 8 * factor).toInt()
            val isDesertColumn = factor > 0.5f

            for (y in 0 until height) {
                val isCave = y < surfY - soilDepth && p.noise(x * noiseScaleCaves, y * noiseScaleCaves) < caveThreshold
                
                if (isCave) {
                    // В пещерах ставим только стену
                    val wallType = when {
                        isDesertColumn && y < surfY - soilDepth -> WallList.sandstone_wall
                        isDesertColumn -> WallList.sand_wall
                        y < surfY - 40 -> WallList.plank_wall
                        else -> WallList.dirt_wall
                    }
                    mapApi.generateWall(dimension, wallType, x, y)
                } else {
                    when {
                        !isDesertColumn && y == surfY + 1 && p.random(100f) < 5f -> {
                            mapApi.generateTile(dimension, TilesList.sunflower, x, y)
                            mapApi.generateTile(dimension, MultiTileDummyType(TilesList.sunflower + "_dummy", 0 p -1), x, y + 1)
                            mapApi.generateTile(dimension, MultiTileDummyType(TilesList.sunflower + "_dummy", 0 p -2), x, y + 2)
                        }
                        !isDesertColumn && y == surfY + 1 && p.random(100f) < 25f -> mapApi.generateTile(dimension, TilesList.small_grass, x, y)
                        y > surfY -> { /* Air */ }
                        y == surfY -> {
                            val tileType = if (isDesertColumn) TilesList.sand_block else TilesList.grass_block
                            mapApi.generateTile(dimension, tileType, x, y)
                        }
                        y > surfY - soilDepth -> {
                            val tileType = if (isDesertColumn) TilesList.sand_block else TilesList.dirt_block
                            mapApi.generateTile(dimension, tileType, x, y)
                        }
                        else -> {
                            val tileType = if (isDesertColumn) TilesList.sandstone_block else TilesList.stone_block
                            mapApi.generateTile(dimension, tileType, x, y)
                        }
                    }
                    
                    // Стены для заполненных блоков
                    if (y <= surfY) {
                        val wallType = when {
                            isDesertColumn && y < surfY - soilDepth -> WallList.sandstone_wall
                            isDesertColumn -> WallList.sand_wall
                            y < surfY - 40 -> WallList.plank_wall
                            else -> WallList.dirt_wall
                        }
                        mapApi.generateWall(dimension, wallType, x, y)
                    }
                }
            }
        }

        // 2. Генерация руд (Оре clumps) — только вне пустыни
        repeat(width * height / 800) {
            val rx = p.random(width.toFloat()).toInt()
            if (smoothDesertFactors[rx] > 0.5f) return@repeat
            val ry = p.random(heightMap[rx].toFloat() - 20f).toInt()
            val rand = p.random(100f)
            val type = when {
                ry < 200 && rand < 20f -> TilesList.diamond_ore_block
                ry > heightMap[rx] * 0.6f && rand < 60f -> TilesList.copper_ore_block
                ry > heightMap[rx] * 0.3f && rand < 50f -> TilesList.iron_ore_block
                else -> TilesList.gold_ore_block
            }
            generateClump(rx, ry, type, p.random(3f, 7f).toInt())
        }

        // 3. Подземные дома (Structures)
        repeat(width / 150) {
            val rx = p.random(50f, width - 50f).toInt()
            val ry = p.random(100f, heightMap[rx] - 100f).toInt()
            generateHouse(rx, ry)
        }

        // 4. Деревья на поверхности (только вне пустыни)
        for (x in 0 until width step 12) {
            val isDesert = smoothDesertFactors[x] > 0.5f
            if (!isDesert && p.random(100f) < 40f) {
                generateTree(x, heightMap[x] + 1)
            }
        }
    }

    private fun generateClump(x: Int, y: Int, type: String, size: Int) {
        for (dx in -size..size) {
            for (dy in -size..size) {
                if (dx * dx + dy * dy <= size * size) {
                    val nx = x + dx
                    val ny = y + dy
                    if (mapApi.getTileType(dimension, nx, ny)?.tag?.contains("stone") == true) {
                        mapApi.generateTile(dimension, type, nx, ny)
                    }
                }
            }
        }
    }

    private fun generateHouse(x: Int, y: Int) {
        val w = p.random(8f, 16f).toInt()
        val h = p.random(6f, 10f).toInt()
        
        // Очистка и стены
        for (dx in 0 until w) {
            for (dy in 0 until h) {
                val nx = x + dx
                val ny = y + dy
                mapApi.deleteTile(dimension, nx, ny)
                mapApi.generateWall(dimension, WallList.plank_wall, nx, ny)
                
                // Пол, потолок, стены
                if (dx == 0 || dx == w - 1 || dy == 0 || dy == h - 1) {
                    mapApi.generateTile(dimension, TilesList.plank_block, nx, ny)
                }
            }
        }
    }

    private fun generateTree(x: Int, y: Int) {
        val h = p.random(5f, 12f).toInt()
        for (i in 0 until h) {
            mapApi.generateTile(dimension, TilesList.tree_part_block, x, y + i)
        }
        // Простая крона
        for (dx in -2..2) {
            for (dy in -2..2) {
                if (p.random(100f) < 70f) {
                    mapApi.generateTile(dimension, TilesList.tree_part_block, x + dx, y + h + dy)
                }
            }
        }
    }
}
