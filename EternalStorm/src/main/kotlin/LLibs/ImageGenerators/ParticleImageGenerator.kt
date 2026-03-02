package la.vok.LavokLibrary.Particles

import processing.core.PApplet
import processing.core.PImage
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel

object ParticleImageGenerator {

    private val logger = ConsoleLogger("ParticleImageGenerator", LogLevel.DEBUG)
    private val cache = HashMap<Key, List<PImage>>()

    // Ключ для кэша содержит хэш картинки, параметры сетки и итоговое разрешение частицы
    private data class Key(
        val imageHash: Int,
        val gridX: Int, val gridY: Int,
        val resX: Int, val resY: Int
    )

    fun generate(info: ParticleSplitInfo, app: PApplet): List<PImage> {
        // Мы используем info.sourceImage.hashCode() чтобы различать исходные текстуры.
        // В Processing это обычно ссылка на объект, так что для разных PImage ключи будут разными.
        val key = Key(
            info.sourceImage.hashCode(),
            info.gridX, info.gridY,
            info.resolution.x, info.resolution.y
        )

        cache[key]?.let {
            logger.trace("Cache hit for particles: $key")
            return it
        }

        logger.debug("Generating ${info.gridX}x${info.gridY} particles with resolution ${info.resolution.x}x${info.resolution.y}")

        val resultList = mutableListOf<PImage>()
        val src = info.sourceImage

        // Считаем размер "кусочка" на оригинальном изображении (дробный, для точности)
        val chunkWidth = src.width.toFloat() / info.gridX
        val chunkHeight = src.height.toFloat() / info.gridY

        for (y in 0 until info.gridY) {
            for (x in 0 until info.gridX) {
                // Рассчитываем координаты
                val px = (x * chunkWidth).toInt()
                val py = (y * chunkHeight).toInt()

                // Рассчитываем размеры, гарантируя минимум 1 пиксель
                var w = if (x == info.gridX - 1) src.width - px else chunkWidth.toInt()
                var h = if (y == info.gridY - 1) src.height - py else chunkHeight.toInt()

                // ЗАЩИТА: Если из-за деления размер стал 0, ставим 1
                if (w <= 0) w = 1
                if (h <= 0) h = 1

                // 1) Вырезаем частицу (get безопасен, если размеры > 0)
                val chunk = src.get(px, py, w, h)

                // 2) Ресайз (только если целевое разрешение > 0)
                val targetW = if (info.resolution.x > 0) info.resolution.x else 1
                val targetY = if (info.resolution.y > 0) info.resolution.y else 1

                if (chunk.width != targetW || chunk.height != targetY) {
                    chunk.resize(targetW, targetY)
                }

                resultList.add(chunk)
            }
        }

        if (info.save) {
            logger.info("Particles cached: $key")
            // Сохраняем в кэш иммутабельный список для надежности
            cache[key] = resultList.toList()
        }

        return resultList
    }
}