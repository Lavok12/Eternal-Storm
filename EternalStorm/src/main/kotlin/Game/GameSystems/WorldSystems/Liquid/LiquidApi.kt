package la.vok.Game.GameSystems.WorldSystems.Liquid

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.LiquidTypes.AbstractLiquidType
import la.vok.Game.GameContent.ContentList.LiquidList

class LiquidApi(val gameCycle: GameCycle) {

    /**
     * Spawns or sets liquid at a specific position.
     * @param typeTag The registered tag of the liquid (e.g. "water").
     * @param amount Liquid level from 0 to 255.
     */
    fun setLiquid(dimension: AbstractDimension, x: Int, y: Int, typeTag: String, amount: Int) {
        val type = gameCycle.gameController.coreController.objectRegistration.liquids[typeTag] ?: return
        dimension.liquidController?.liquidSystem?.setLiquid(x, y, type.id, amount)
    }

    fun hasLiquid(dimension: AbstractDimension, x: Int, y: Int, tag: String): Boolean {
        return getType(dimension, x, y)?.tag == tag
    }

    fun setLiquid(dimension: AbstractDimension, x: Int, y: Int, type: AbstractLiquidType?, amount: Int) {
        dimension.liquidController?.liquidSystem?.setLiquid(x, y, type?.id ?: 0, amount)
    }

    fun getAmount(dimension: AbstractDimension, x: Int, y: Int): Int {
        return dimension.liquidController?.liquidSystem?.getAmount(x, y) ?: 0
    }

    fun getType(dimension: AbstractDimension, x: Int, y: Int): AbstractLiquidType? {
        val id = dimension.liquidController?.liquidSystem?.getTypeId(x, y) ?: 0
        return gameCycle.gameController.coreController.objectRegistration.getLiquidType(id)
    }

    /**
     * Wakes up the liquid at the given position and its neighbors.
     * Useful when blocks are modified.
     */
    fun activate(dimension: AbstractDimension, x: Int, y: Int) {
        dimension.liquidController?.liquidSystem?.activateNeighbors(x, y)
    }
}
