package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.GameControllers.GameRender

data class RenderLayerData<T : Enum<T>>(
    var layerType: GameRender.Companion.Layers,
    var layer: T,
    var sublayer: Int
)