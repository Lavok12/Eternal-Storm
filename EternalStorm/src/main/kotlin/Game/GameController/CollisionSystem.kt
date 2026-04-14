package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import kotlin.math.floor

class CollisionSystem(var gameCycle: GameCycle) : Controller {
    val spatialGrid = HashMap<Long, MutableList<HitboxComponent>>()
    val GRID_SIZE = 32f

    private val uniqueCandidates = HashSet<HitboxComponent>()
    private val current = HashSet<HitboxComponent>()
    
    private val listPool = mutableListOf<MutableList<HitboxComponent>>()
    private var poolIndex = 0

    private fun getListFromPool(): MutableList<HitboxComponent> {
        if (poolIndex >= listPool.size) listPool.add(mutableListOf())
        return listPool[poolIndex++].apply { clear() }
    }

    init { create() }

    override fun logicalTick() {}

    fun rebuildGrid(dimension: AbstractDimension) {
        spatialGrid.clear()
        poolIndex = 0
        val entities = gameCycle.entityApi.getActiveEntities(dimension)
        
        entities.forEach { entity ->
            entity.hitboxes.values.forEach { hitbox ->
                val minX = floor(hitbox.frameLeftTop.x / GRID_SIZE).toInt()
                val maxX = floor(hitbox.frameRightBottom.x / GRID_SIZE).toInt()
                val minY = floor(hitbox.frameLeftTop.y / GRID_SIZE).toInt()
                val maxY = floor(hitbox.frameRightBottom.y / GRID_SIZE).toInt()

                for (x in minX..maxX) {
                    for (y in minY..maxY) {
                        val key = (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)
                        spatialGrid.getOrPut(key) { getListFromPool() }.add(hitbox)
                    }
                }
            }
        }
    }

    fun updateDetector(detector: CollisionDetector) {
        val hitbox = detector.hitboxComponent
        val minX = floor(hitbox.frameLeftTop.x / GRID_SIZE).toInt()
        val maxX = floor(hitbox.frameRightBottom.x / GRID_SIZE).toInt()
        val minY = floor(hitbox.frameLeftTop.y / GRID_SIZE).toInt()
        val maxY = floor(hitbox.frameRightBottom.y / GRID_SIZE).toInt()

        uniqueCandidates.clear()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                val key = (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)
                spatialGrid[key]?.let { uniqueCandidates.addAll(it) }
            }
        }

        current.clear()
        uniqueCandidates.forEach { other ->
            if (other === detector.hitboxComponent) return@forEach
            if (other.entity == detector.entity) return@forEach
            if (other.ignoreCollision) return@forEach
            
            if (!detector.tagFilter.matches(other.entity.entityType.tags.asList())) return@forEach
            
            if (intersects(detector.hitboxComponent, other)) current.add(other)
        }

        val started = mutableListOf<HitboxComponent>()
        val ended = mutableListOf<HitboxComponent>()

        detector.activeContacts.forEach { if (!current.contains(it)) ended.add(it) }
        current.forEach { if (!detector.activeContacts.contains(it)) started.add(it) }

        started.forEach { detector.startContact(it) }
        ended.forEach { detector.endContact(it) }
    }

    private fun intersects(a: HitboxComponent, b: HitboxComponent): Boolean {
        return a.frameLeftTop.x < b.frameRightBottom.x &&
                a.frameRightBottom.x > b.frameLeftTop.x &&
                a.frameLeftTop.y < b.frameRightBottom.y &&
                a.frameRightBottom.y > b.frameLeftTop.y
    }
}