package la.vok.Game.GameSystems.WorldSystems.Dimensions.Generators

import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

abstract class AbstractMapGenerator(var dimension: AbstractDimension) {
    val mapApi get() = dimension.gameCycle.mapApi
    val width get() = dimension.width
    val height get() = dimension.height
    
    abstract fun generateTerrain()
}
