package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

/**
 * Component that deals damage to entities on contact.
 */
class ContactDamageComponent(
    entity: Entity,
    var damage: Int,
    var knockback: Float,
    var upKnockback: Float,
    var sourceId: Long,
    var targetTags: List<String>
) : EntityComponent(entity) {

    override fun onEvent(event: EntityEvent) {
        if (event is EntityEvent.ContactStart) {
            val target = event.otherHitbox.entity
            if (!target.isDead) {
                target.takeDamage(DamageData(
                    damage,
                    (entity.rigidBody?.speed?.normalized() ?: Vec2.ZERO) * knockback + (0 v upKnockback),
                    sourceId,
                    null
                ), event.otherHitbox)
                
                // Kill self after hit (typical for projectiles)
                entityApi.killInSystem(entity.dimension!!, entity.systemId)
            }
        }
    }
}
