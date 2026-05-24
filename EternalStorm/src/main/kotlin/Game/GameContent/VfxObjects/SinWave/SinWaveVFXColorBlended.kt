package la.vok.Game.GameContent.VfxObjects.SinWave

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.VfxObjects.AbstractVfxObject
import la.vok.LavokLibrary.LGraphics.LBlendMode
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

abstract class SinWaveVFXColorBlended(gameCycle: GameCycle) : AbstractVfxObject(gameCycle) {
    protected var r = 255f
    protected var g = 255f
    protected var b = 255f
    protected var baseAlpha = 255f

    fun setupColor(red: Float, green: Float, blue: Float, alpha: Float = 255f) {
        r = red * 0.5f + 168f
        g = green * 0.5f + 168f
        b = blue * 0.5f + 168f
        baseAlpha = alpha
    }

    override fun create() {
        val texture = gameController.coreController.spriteLoader.getValue("glow.png")

        renderComponent = object : BaseRenderEntity(getRenderLayer()) {
            override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
                val currentAlpha = baseAlpha * (1f - progress)
                if (currentAlpha <= 0f) return

                //lg.setBlendMode(LBlendMode.ADD)
                lg.setTint(r, g, b, currentAlpha)

                renderParticle(lg, pos, size)

                //lg.resetBlendMode()
                lg.noTint()
            }
        }
    }

    abstract fun renderParticle(lg: LGraphics, pos: Vec2, size: Vec2)
}