package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Generators.MainMapGenerator
import la.vok.LavokLibrary.Vectors.LColor

class MainDimension(gameCycle: GameCycle) : AbstractDimension(gameCycle) {
    override val dimensionTag: String = DimensionsList.main
    override val width: Int = 300
    override val height: Int = 100
    override var skyColor: LColor  = LColor(100f, 160f, 220f)

    override fun generateMap() {
        MainMapGenerator(this).generateTerrain()
    }
}
