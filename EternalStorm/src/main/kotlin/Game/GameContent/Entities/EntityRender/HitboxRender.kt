package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class HitboxRender(var hitboxComponent: HitboxComponent, layersRenderContainer: LayersRenderContainer) : BaseRenderObject(layersRenderContainer) {
    override var layerData: RenderLayerData = RenderLayerData(
        RenderLayers.Main.A5,
        1000
    )

    override var ROI_pos = 0 v 0
    override var ROI_size = 1 v 1
    override var ROI_delta = Vec2.ZERO

    fun update() {
        ROI_pos = hitboxComponent.entity.position
        ROI_size = hitboxComponent.size
        ROI_delta = hitboxComponent.delta
    }

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        when (hitboxComponent.hitboxType) {
            HitboxTypes.COLLISION -> {
                lg.fill(50f, 200f, 50f, 50f)
                lg.stroke(50f, 200f, 50f)
            }
            HitboxTypes.ONLY_TRIGGER -> {
                lg.fill(200f, 50f, 50f, 50f)
                lg.stroke(200f, 50f, 50f)
            }
        }

        lg.setBlock(pos, size)
        lg.noStroke()
    }

}

