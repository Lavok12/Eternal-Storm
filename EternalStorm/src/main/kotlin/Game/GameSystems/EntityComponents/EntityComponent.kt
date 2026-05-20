package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi

open class EntityComponent(var entity: Entity) : IEntityComponent {
    val mapApi: MapApi get() = entity.gameCycle.mapApi
    val entityApi: EntityApi get() = entity.gameCycle.entityApi

    override fun onAttach(entity: Entity) {
        this.entity = entity
    }
}