package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity

/**
 * Component that redirects all incoming damage events to another entity.
 * Useful for multi-segmented boss parts that share a single health pool.
 */
class DamageRedirectComponent(entity: Entity, var target: Entity) : EntityComponent(entity) {
    override fun onEvent(event: EntityEvent) {
        if (event is EntityEvent.Damage) {
            // Forward damage to target's takeDamage method
            target.gameCycle.entityApi.absoluteDamage(target.dimension!!, target, event.data)
        }
    }
}
