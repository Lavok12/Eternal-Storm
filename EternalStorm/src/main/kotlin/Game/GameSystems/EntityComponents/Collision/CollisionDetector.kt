package la.vok.Game.GameSystems.EntityComponents.Collision

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.CollisionSystem
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.Game.GameSystems.EntityComponents.EntityComponent
import la.vok.Game.GameSystems.EntityComponents.EntityEvent

class CollisionDetector(
    entity: Entity,
    var hitboxComponent: HitboxComponent,
    var gameCycle: GameCycle,
    var tagFilter: TagFilter = TagFilter.Any,
    var onContactStart: ((HitboxComponent) -> Unit)? = null,
    var onContactEnd: ((HitboxComponent) -> Unit)? = null
) : EntityComponent(entity) {

    val collisionSystem: CollisionSystem get() = gameCycle.collisionSystem
    val activeContacts = HashSet<HitboxComponent>()

    fun clear() { activeContacts.clear() }

    fun update() { collisionSystem.updateDetector(this) }

    fun startContact(other: HitboxComponent) {
        if (activeContacts.add(other)) {
            onContactStart?.invoke(other)
            entity.sendEvent(EntityEvent.ContactStart(other))
        }
    }

    fun endContact(other: HitboxComponent) {
        if (activeContacts.remove(other)) {
            onContactEnd?.invoke(other)
            entity.sendEvent(EntityEvent.ContactEnd(other))
        }
    }
}