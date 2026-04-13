package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent

class CollisionSystem(var gameCycle: GameCycle) : Controller {
    init { create() }

    override fun logicalTick() {}

    fun updateDetector(detector: CollisionDetector) {
        val entityApi = gameCycle.entityApi
        val dim = detector.entity.dimension!!

        val candidates = entityApi.getActiveEntities(dim).filter { entity ->
            detector.tagFilter.matches(entity.entityType.tags.toList())
        }

        val current = HashSet<HitboxComponent>()

        candidates.forEach { entity ->
            entity.hitboxes.values.forEach { other ->
                if (other === detector.hitboxComponent) return@forEach
                if (other.entity == detector.entity) return@forEach
                if (other.ignoreCollision) return@forEach
                if (intersects(detector.hitboxComponent, other)) current.add(other)
            }
        }

        val started = current - detector.activeContacts
        val ended = detector.activeContacts - current

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