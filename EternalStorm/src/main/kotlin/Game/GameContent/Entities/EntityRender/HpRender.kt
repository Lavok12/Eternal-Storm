package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class HpRender(layersRenderContainer: LayersRenderContainer<RenderLayers.Main>, var hpBody: HpBody, var delta: Vec2 = 0 v -0.5f) : BaseRenderObject<RenderLayers.Main>(layersRenderContainer) {
    override var layerData: RenderLayerData<RenderLayers.Main> = RenderLayerData<RenderLayers.Main>(
        GameRender.Companion.Layers.gameObjects,
        RenderLayers.Main.A4,
        1000
    )

    override var ROI_pos = 0 v 0
    override var ROI_size = 1 v 1

    fun update() {
        ROI_pos = hpBody.entity.position
        ROI_delta = (0 v -hpBody.entity.size.y/2f)+delta
    }

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        val percentage = hpBody.percentageOfHp().coerceIn(0f, 1f)

        lg.fill(40f)
        lg.setBlock(pos, 80f v 15f)

        val maxWidth = 76f
        val height = 11f
        val currentWidth = maxWidth * percentage

        val red = (1f - percentage) * 255f
        val green = percentage * 255f
        lg.fill(red, green, 0f)

        val offsetX = (maxWidth - currentWidth) / 2f
        val hpPos = pos + (-offsetX v 0f)

        lg.setBlock(hpPos, currentWidth v height)
    }
}