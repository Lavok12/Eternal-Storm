package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BaseRenderEntity(layersRenderContainer: LayersRenderContainer) : BaseRenderObject(layersRenderContainer) {
    override var layerData: RenderLayerData = RenderLayerData(
        RenderLayers.Main.A1,
        1
    )

    override var ROI_pos = Vec2.ZERO
    override var ROI_size = 1 v 1
    override var ROI_delta = 0 v 0

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        lg.fill(255f, 50f, 50f)
        lg.setBlock(pos, size)
    }
}