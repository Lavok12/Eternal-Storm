package la.vok.Game.GameSystems.WorldSystems.Dimensions.System

import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension

class DimensionsApi(val dimensionsController: DimensionsController) {
    
    fun getDimension(tag: String): AbstractDimension? =
        dimensionsController.dimensions[tag]
        
    fun getMainDimension(): AbstractDimension =
        getDimension(DimensionsList.main)!!

    fun registerDimension(dimension: AbstractDimension) {
        dimensionsController.dimensions[dimension.dimensionTag] = dimension
        dimension.generateMap()
    }
    fun changeRenderDimension(oldDim: AbstractDimension, newDim: AbstractDimension) {
        dimensionsController.gameCycle.entityApi.hideDimensionEntity(oldDim)
        dimensionsController.gameCycle.entityApi.showDimensionEntity(newDim)
    }
}
