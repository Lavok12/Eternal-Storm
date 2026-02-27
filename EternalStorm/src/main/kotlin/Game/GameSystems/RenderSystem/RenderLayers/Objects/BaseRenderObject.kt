package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.Vectors.v

abstract class BaseRenderObject (
    override val layersRenderContainer: LayersRenderContainer
) : RenderObjectInterface {
    override var isShow: Boolean = false
    override var ROI_delta = 0 v 0
}