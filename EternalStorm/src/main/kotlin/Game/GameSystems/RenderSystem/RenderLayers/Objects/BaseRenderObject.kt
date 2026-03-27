package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

abstract class BaseRenderObject (
    override val layersRenderContainer: LayersRenderContainer
) : RenderObjectInterface {
    val coreController: CoreController get() = layersRenderContainer.gameRender.coreController

    override var ROI_pos = Vec2.ZERO
    override var ROI_size = 1 v 1
    override var ROI_delta = Vec2.ZERO

    override var isShow: Boolean = false
}