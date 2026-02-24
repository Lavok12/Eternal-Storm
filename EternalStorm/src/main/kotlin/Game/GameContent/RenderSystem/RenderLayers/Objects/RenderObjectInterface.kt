package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

interface RenderObjectInterface<T : Enum<T>> {
    val gameController: GameController
    var layerData: RenderLayerData<T>

    var ROI_pos: Vec2
    var ROI_size: Vec2

    fun getContainer(): LayersRenderContainer<T> {
        @Suppress("UNCHECKED_CAST")
        return gameController.gameRender.getContainer(layerData.layerType) as LayersRenderContainer<T>
    }

    var isShow: Boolean

    fun draw(lg: LGraphics, camera: Camera)

    fun show() {
        isShow = true
        getContainer().addPrint(this)
    }

    fun unShow() {
        isShow = false
        getContainer().removePrint(this)
    }
}