package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.TileTypes.Crops.CropConfig

open class CropTileData(
    x: Int, y: Int, dimension: AbstractDimension,
    private val config: CropConfig
) : AbstractTileData(x, y, dimension) {
    
    override val wantsMainSecond: Boolean = true
    var growth: Int = 0
    var currentTick: Int = 0

    override fun secondUpdate() {
        if (growth < config.maxGrowthStages) {
            if (!config.canGrow(dimension, x, y)) return
            
            currentTick++
            if (currentTick >= config.baseTicksPerStage) {
                currentTick = 0
                growth++
                dimension.mapSystem.updateBlock(x, y)
            }
        }
    }
}
