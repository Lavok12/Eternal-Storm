package la.vok.Game.GameContent.Dimensions.Dimensions

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameContent.Dimensions.Generators.MainMapGenerator
import la.vok.LavokLibrary.Gradient.GradientInfo
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

class MainDimension(gameCycle: GameCycle) : AbstractDimension(gameCycle) {
    override val dimensionTag: String = DimensionsList.main
    override val width: Int = 15000
    override val height: Int = 1000
    override var skyColor: LColor  = LColor(100f, 160f, 220f)

    override fun backgroundRender(lGraphics: LGraphics, camera: Camera) {
        lGraphics.setImage(GradientInfo(skyColor*0.75f, skyColor, 0 p 0, 0 p 200, 1 p 200).generate(), 0 v 0, lGraphics.disW v lGraphics.disH)
    }
    override fun generateMap() {
        MainMapGenerator(this).generateTerrain()
    }
}
