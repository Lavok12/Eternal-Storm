package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Generators.StoneWorldMapGenerator
import la.vok.LavokLibrary.Vectors.LColor

class StoneWorldDimension(gameCycle: GameCycle) : AbstractDimension(gameCycle) {
    override val dimensionTag: String = DimensionsList.stone_world
    override val width: Int = 150
    override val height: Int = 150
    override var skyColor: LColor  = LColor(200f, 50f, 50f)

    override fun generateMap() {
        StoneWorldMapGenerator(this).generateTerrain()
    }
}
