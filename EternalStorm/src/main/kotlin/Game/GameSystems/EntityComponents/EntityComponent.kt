package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.Entities.EntityApi

open class EntityComponent(var entity: Entity) {
    val mapApi: MapApi get() = entity.gameController.mapController.mapApi
    val entityApi: EntityApi get() = entity.gameController.entityController.entityApi


}