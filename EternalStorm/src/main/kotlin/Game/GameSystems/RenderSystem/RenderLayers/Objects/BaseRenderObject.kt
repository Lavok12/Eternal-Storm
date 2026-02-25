package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameControllers.GameController

abstract class BaseRenderObject<T : Enum<T>>(
    override val layersRenderContainer: LayersRenderContainer<T>
) : RenderObjectInterface<T> {
    override var isShow: Boolean = false
}