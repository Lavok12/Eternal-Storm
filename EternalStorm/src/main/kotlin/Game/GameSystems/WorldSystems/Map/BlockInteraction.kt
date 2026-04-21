package la.vok.Game.GameSystems.WorldSystems.Map

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

enum class BlockInteractionType {
    LEFT, RIGHT, MIDDLE
}

data class BlockInteractionContext(
    val x: Int,
    val y: Int,
    val dimension: AbstractDimension,
    val interactor: Entity?,
    val mapApi: MapApi
)
