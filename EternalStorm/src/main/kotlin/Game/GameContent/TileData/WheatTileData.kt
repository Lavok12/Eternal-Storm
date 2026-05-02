package la.vok.Game.GameContent.TileData

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class WheatTileData(
    x: Int,
    y: Int,
    dimension: AbstractDimension
) : AbstractTileData(x, y, dimension) {
    
    override val wantsMainSecond: Boolean = true
    
    var growth: Int = 0
    val maxGrowth: Int = 3

    var ticksUntilNextStage: Int = 5 
    var currentTick: Int = 0

    override fun secondUpdate() {
        if (growth < maxGrowth) {
            currentTick++
            if (currentTick >= ticksUntilNextStage) {
                currentTick = 0
                growth++
                dimension.mapSystem.updateBlock(x, y)
            }
        }
    }
}
