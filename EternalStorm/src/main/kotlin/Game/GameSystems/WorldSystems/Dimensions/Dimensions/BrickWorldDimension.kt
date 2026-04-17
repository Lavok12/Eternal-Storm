package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Generators.BrickWorldMapGenerator
import la.vok.LavokLibrary.Vectors.LColor

class BrickWorldDimension(gameCycle: GameCycle) : AbstractDimension(gameCycle) {
    override val dimensionTag: String = DimensionsList.brick_world
    override val width: Int = 300
    override val height: Int = 300
    override var skyColor: LColor = LColor(80f, 40f, 100f) // Magical Purple Sky

    override fun generateMap() {
        BrickWorldMapGenerator(this).generateTerrain()
    }
}
