package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.Entities.ItemEntity
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class ItemRenderEntity(layersRenderContainer: LayersRenderContainer) : BaseRenderObject(layersRenderContainer) {
    override var layerData: RenderLayerData = RenderLayerData(
        RenderLayers.Main.A2,
        10
    )

    override var ROI_pos = 0 v 0
    override var ROI_size = 1 v 1
    override var ROI_delta = 0 v 0

    var item: Item? = null

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        if (item != null) {
            item!!.worldRender(lg, pos + (0 v size.y/5f), size * 2f, camera)
        }
    }
}