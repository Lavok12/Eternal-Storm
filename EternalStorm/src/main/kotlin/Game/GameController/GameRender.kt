package la.vok.Core.GameControllers

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.LavokLibrary.LGraphics.LGraphics

class GameRender(val gameController: GameController) : Controller {
    val coreController: CoreController
        get() {return gameController.coreController}

    companion object {
        enum class Layers {
            gameObjects,
            redLightMap,
            greenLightMap,
            blueLightMap,
            globalLight,
            backgroundMap,
            backgroundLightMap,
        }
    }

    init {
        create()
    }

    val gameObjects = LayersRenderContainer(this, RenderLayers.Main::class.java)

    val redLightMap = LayersRenderContainer(this, RenderLayers.ColorMap::class.java)
    val greenLightMap = LayersRenderContainer(this, RenderLayers.ColorMap::class.java)
    val blueLightMap = LayersRenderContainer(this, RenderLayers.ColorMap::class.java)

    val globalLight = LayersRenderContainer(this, RenderLayers.GlobalLight::class.java)

    val backgroundMap = LayersRenderContainer(this, RenderLayers.BackGround::class.java)
    val backgroundLightMap = LayersRenderContainer(this, RenderLayers.BackGroundColorMap::class.java)

    fun getContainer(layer: Layers): LayersRenderContainer<*> = when(layer) {
        Layers.gameObjects -> gameObjects
        Layers.redLightMap -> redLightMap
        Layers.greenLightMap -> greenLightMap
        Layers.blueLightMap -> blueLightMap
        Layers.globalLight -> globalLight
        Layers.backgroundMap -> backgroundMap
        Layers.backgroundLightMap -> backgroundLightMap
    }

    override fun tick() {
        superTick()
    }

    fun render(lg: LGraphics) {

    }
}
