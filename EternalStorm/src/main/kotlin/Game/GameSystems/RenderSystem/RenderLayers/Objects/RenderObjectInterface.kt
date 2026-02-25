package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

interface RenderObjectInterface<T : Enum<T>> {
    val layersRenderContainer: LayersRenderContainer<T>
    var layerData: RenderLayerData<T>

    var ROI_pos: Vec2
    var ROI_size: Vec2

    fun getContainer(): LayersRenderContainer<T> {
        @Suppress("UNCHECKED_CAST")
        return layersRenderContainer
    }

    var isShow: Boolean

    fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera)

    fun show() {
        isShow = true
        getContainer().addPrint(this)
    }

    fun hide() {
        isShow = false
        getContainer().removePrint(this)
    }
}