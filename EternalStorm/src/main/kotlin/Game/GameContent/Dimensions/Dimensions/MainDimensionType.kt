package la.vok.Game.GameContent.Dimensions.Dimensions

import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameController.GameCycle

class MainDimensionType : AbstractDimensionType() {
    override val tag: String = DimensionsList.main

    override fun createDimension(gameCycle: GameCycle): AbstractDimension {
        return MainDimension(gameCycle)
    }
}
