package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity

/**
 * Component that kills the entity after a certain amount of time (ticks).
 */
class LifetimeComponent(entity: Entity, var maxLifetime: Int) : EntityComponent(entity) {
    var ticks = 0

    override fun onPhysicUpdate() {
        ticks++
        if (ticks >= maxLifetime) {
            entityApi.killInSystem(entity.dimension!!, entity.systemId)
        }
    }
}
