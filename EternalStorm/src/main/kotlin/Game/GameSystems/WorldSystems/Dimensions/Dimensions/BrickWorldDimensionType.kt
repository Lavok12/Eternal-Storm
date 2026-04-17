package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameController.GameCycle

class BrickWorldDimensionType : AbstractDimensionType() {
    override val tag: String = DimensionsList.brick_world

    override fun createDimension(gameCycle: GameCycle): AbstractDimension {
        return BrickWorldDimension(gameCycle)
    }
}
