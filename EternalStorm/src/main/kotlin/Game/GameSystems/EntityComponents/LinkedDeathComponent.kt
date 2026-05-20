package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity

/**
 * Component that kills specified entities when this entity dies.
 */
class LinkedDeathComponent(entity: Entity, val linkedEntities: MutableList<Entity> = mutableListOf()) : EntityComponent(entity) {
    override fun onEvent(event: EntityEvent) {
        if (event is EntityEvent.Death) {
            linkedEntities.forEach { 
                if (!it.isDead) {
                    it.isDead = true
                    it.die()
                    entityApi.hideEntity(it.dimension!!, it)
                    entityApi.deleteInSystem(it.dimension!!, it)
                }
            }
        }
    }
}
