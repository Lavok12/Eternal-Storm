package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameController.GameCycle

class MainDimensionType : AbstractDimensionType() {
    override val tag: String = DimensionsList.main
    override val width: Int = 300
    override val height: Int = 100

    override fun createDimension(gameCycle: GameCycle): AbstractDimension {
        return MainDimension(gameCycle)
    }
}
