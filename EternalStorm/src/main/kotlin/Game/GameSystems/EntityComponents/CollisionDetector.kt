package la.vok.Game.GameSystems.EntityComponents

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameController.CollisionSystem
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.Entities.TagFilter

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
        if (activeContacts.add(other)) onContactStart?.invoke(other)
    }

    fun endContact(other: HitboxComponent) {
        if (activeContacts.remove(other)) onContactEnd?.invoke(other)
    }
}