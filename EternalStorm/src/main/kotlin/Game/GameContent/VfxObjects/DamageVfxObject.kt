package la.vok.Game.GameContent.VfxObjects

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsSystem
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import kotlin.math.min

class DamageVfxObject(gameCycle: GameCycle, var damage: Int = 0) : AbstractVfxObject(gameCycle) {

    init {
        lifetime = 100
    }

    override fun physicUpdate() {
        super.physicUpdate()
    }

    override var renderComponent: RenderObjectInterface? = object : BaseRenderObject(getRenderLayer()) {
        override var layerData: RenderLayerData = RenderLayerData(
            RenderLayers.Main.A5,
            3)
        override var ROI_pos: Vec2 = Vec2.ZERO
        override var ROI_size: Vec2 = 1 v 1

        override fun draw(
            lg: LGraphics,
            pos: Vec2,
            size: Vec2,
            camera: Camera
        ) {
            lg.setTextAlign(LPoint(0,0 ))
            lg.fill(200f, 140f, 50f, min(255f, 500f - progress*500f))
            lg.setText("$damage", pos + (0 v 20f), 40f)
        }

    }
}