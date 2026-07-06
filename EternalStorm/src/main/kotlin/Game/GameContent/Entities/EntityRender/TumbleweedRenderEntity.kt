package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.Entities.TumbleweedEntity
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class TumbleweedRenderEntity(
    layersRenderContainer: LayersRenderContainer,
    val tumbleweed: TumbleweedEntity
) : BaseRenderObject(layersRenderContainer) {
    override var layerData: RenderLayerData = RenderLayerData(
        RenderLayers.Main.A1,
        1
    )

    override var ROI_pos = Vec2.ZERO
    override var ROI_size = 1 v 1
    override var ROI_delta = Vec2.ZERO

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        val drawSize = size * 1.2f   // sprite 20% bigger than collision box
        try {
            val sprite = coreController.spriteLoader.getValue("tumbleweed.png")
            lg.setRotateImage(sprite, pos, drawSize, tumbleweed.rotationAngle)
        } catch (_: Exception) {
            lg.fill(139f, 90f, 43f, 180f)
            lg.setRotateBlock(pos.x, pos.y, drawSize.x, drawSize.y, tumbleweed.rotationAngle)
        }
    }
}
