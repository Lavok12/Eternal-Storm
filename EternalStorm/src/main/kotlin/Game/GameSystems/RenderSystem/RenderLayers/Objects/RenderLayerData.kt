package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers

data class RenderLayerData (
    var layerType: GameRender.Companion.Layers,
    var layer: RenderLayers.Main,
    var sublayer: Int
)