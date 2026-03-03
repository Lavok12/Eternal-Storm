package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BossRenderEntity(layersRenderContainer: LayersRenderContainer) : BaseRenderObject(layersRenderContainer) {
    override var layerData: RenderLayerData = RenderLayerData(
        RenderLayers.Main.B5,
        1
    )

    override var ROI_pos = Vec2.Companion.ZERO
    override var ROI_size = 1 v 1
    override var ROI_delta = Vec2.Companion.ZERO
    var rotate = 0f
    var type = 1

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        lg.fill(50f, 120f, 200f, 120f)
        when (type) {
            1 -> {
                lg.setRotateImage(coreController.spriteLoader.getValue("xHead.png"), pos, size*2.4f, rotate)
            }
            0 -> {
                lg.setRotateImage(coreController.spriteLoader.getValue("xBody.png"), pos, size*2.0f, rotate)
            }
            -1 -> {
                lg.setRotateImage(coreController.spriteLoader.getValue("xTail.png"), pos, size*1.8f, rotate)
            }
        }
    }
}