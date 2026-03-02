package la.vok.LavokLibrary.Particles

import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState
import processing.core.PImage

data class ParticleSplitInfo(
    val sourceImage: PImage, // исходная текстура (например, земли)
    val gridX: Int,          // на сколько частей делить по горизонтали (например, 5)
    val gridY: Int,          // на сколько частей делить по вертикали (например, 5)
    val resolution: LPoint,  // целевое разрешение каждой частицы (например, 20x20)
    val save: Boolean = true // сохранять ли в кэш
) {
    fun generate(): List<PImage> {
        return ParticleImageGenerator.generate(this, AppState.main)
    }
}