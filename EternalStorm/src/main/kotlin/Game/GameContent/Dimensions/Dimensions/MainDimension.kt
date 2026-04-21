package la.vok.Game.GameContent.Dimensions.Dimensions

import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameContent.Dimensions.Generators.MainMapGenerator
import la.vok.LavokLibrary.Vectors.LColor

class MainDimension(gameCycle: GameCycle) : AbstractDimension(gameCycle) {
    override val dimensionTag: String = DimensionsList.main
    override val width: Int = 15000
    override val height: Int = 1000
    override var skyColor: LColor  = LColor(100f, 160f, 220f)

    override fun generateMap() {
        MainMapGenerator(this).generateTerrain()
    }
}
