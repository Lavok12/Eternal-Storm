package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.GameControllers.GameController

abstract class BaseRenderObject<T : Enum<T>>(
    override val gameController: GameController
) : RenderObjectInterface<T> {
    override var isShow: Boolean = false
}