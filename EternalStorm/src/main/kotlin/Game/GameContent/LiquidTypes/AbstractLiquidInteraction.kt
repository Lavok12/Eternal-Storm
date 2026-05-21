package la.vok.Game.GameContent.LiquidTypes

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

abstract class AbstractLiquidInteraction {
    abstract val liquid1: Byte
    abstract val liquid2: Byte
    abstract val minAmount1: Int
    abstract val minAmount2: Int

    /**
     * Checks if this interaction applies to the given pair of liquids.
     */
    fun matches(typeA: Byte, typeB: Byte): Boolean {
        return (typeA == liquid1 && typeB == liquid2) || (typeA == liquid2 && typeB == liquid1)
    }

    /**
     * Called when liquids meet. 
     * (x1, y1) is the source of the flow, (x2, y2) is the target.
     * @return true if the reaction consumed the liquids and should stop further flow.
     */
    abstract fun onReact(
        x1: Int, y1: Int,
        x2: Int, y2: Int,
        dimension: AbstractDimension, 
        amount1: Int, 
        amount2: Int
    ): Boolean
}

/**
 * Standard reaction that creates a tile when two liquids meet.
 * Example: Water + Lava = Stone
 */
class TileReaction(
    override val liquid1: Byte,
    override val liquid2: Byte,
    override val minAmount1: Int,
    override val minAmount2: Int,
    val resultTileTag: String
) : AbstractLiquidInteraction() {
    override fun onReact(x1: Int, y1: Int, x2: Int, y2: Int, dimension: AbstractDimension, amount1: Int, amount2: Int): Boolean {
        // Only place tile if both amounts are enough
        if (amount1 >= minAmount1 && amount2 >= minAmount2) {
            dimension.gameCycle.mapApi.setTileType(dimension, resultTileTag, x2, y2)
        }
        
        // Always consume both liquids when they touch and trigger a reaction check
        dimension.gameCycle.liquidApi.setLiquid(dimension, x1, y1, null, 0)
        dimension.gameCycle.liquidApi.setLiquid(dimension, x2, y2, null, 0)
        
        return true
    }
}
