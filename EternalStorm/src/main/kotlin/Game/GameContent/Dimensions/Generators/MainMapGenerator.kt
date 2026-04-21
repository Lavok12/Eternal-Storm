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
        val heightMap = IntArray(width) { x ->
            (surfaceLevel + (p.noise(x * noiseScaleRelief) - 0.5f) * 160f).toInt()
        }

        // 1. Основной ландшафт и пещеры
        for (x in 0 until width) {
            val surfY = heightMap[x]
            for (y in 0 until height) {
                val isCave = y < surfY - 5 && p.noise(x * noiseScaleCaves, y * noiseScaleCaves) < caveThreshold
                
                if (isCave) {
                    // В пещерах ставим только стену
                    val wallType = if (y < surfY - 40) WallList.plank_wall else WallList.dirt_wall
                    mapApi.generateWall(dimension, wallType, x, y)
                } else {
                    when {
                        y == surfY + 1 && p.random(100f) < 5f -> {
                            mapApi.generateTile(dimension, TilesList.sunflower, x, y)
                            mapApi.generateTile(dimension, MultiTileDummyType(TilesList.sunflower + "_dummy", 0 p -1), x, y + 1)
                            mapApi.generateTile(dimension, MultiTileDummyType(TilesList.sunflower + "_dummy", 0 p -2), x, y + 2)
                        }
                        y == surfY + 1 && p.random(100f) < 25f -> mapApi.generateTile(dimension, TilesList.small_grass, x, y)
                        y > surfY -> { /* Air */ }
                        y == surfY -> mapApi.generateTile(dimension, TilesList.grass_block, x, y)
                        y > surfY - 5 -> mapApi.generateTile(dimension, TilesList.dirt_block, x, y)
                        else -> mapApi.generateTile(dimension, TilesList.stone_block, x, y)
                    }
                    
                    // Стены для заполненных блоков
                    if (y <= surfY) {
                        val wallType = if (y < surfY - 40) WallList.plank_wall else WallList.dirt_wall
                        mapApi.generateWall(dimension, wallType, x, y)
                    }
                }
            }
        }

        // 2. Генерация руд (Оре clumps)
        repeat(width * height / 800) {
            val rx = p.random(width.toFloat()).toInt()
            val ry = p.random(heightMap[rx].toFloat() - 20f).toInt()
            val type = if (ry < 200 && p.random(100f) < 20f) TilesList.diamond_ore_block else TilesList.gold_ore_block
            generateClump(rx, ry, type, p.random(3f, 7f).toInt())
        }

        // 3. Подземные дома (Structures)
        repeat(width / 150) {
            val rx = p.random(50f, width - 50f).toInt()
            val ry = p.random(100f, heightMap[rx] - 100f).toInt()
            generateHouse(rx, ry)
        }

        // 4. Деревья на поверхности
        for (x in 0 until width step 12) {
            if (p.random(100f) < 40f) {
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
