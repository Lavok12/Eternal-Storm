package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameController.GameCycle

class StoneWorldDimensionType : AbstractDimensionType() {
    override val tag: String = DimensionsList.stone_world
    override val width: Int = 150
    override val height: Int = 150

    override fun createDimension(gameCycle: GameCycle): AbstractDimension {
        return StoneWorldDimension(gameCycle)
    }
}
