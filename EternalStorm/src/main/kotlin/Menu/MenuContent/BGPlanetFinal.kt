package la.vok.Menu.MenuContent

import la.vok.Core.GameContent.Layers.EffectLayer
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState

class BGPlanetFinal(
    point: LPoint,
    mp: Float = 1f,
    private val baseLayer: BGPlanetBase
) : EffectLayer(point, mp) {

    var mx = 2.8f
    var my = 2.9f

    override fun draw() {
        val core = AppState.coreController
        val shader = core.shaderLoader.getValue("menu_3d_planet.glsl")

        val pg = lg!!.pg

        pg.beginDraw()

        shader.set("resolution", pg.width.toFloat(), pg.height.toFloat())
        shader.set("u_texture", baseLayer.getImage())
        shader.set("ringMap", core.spriteLoader.getValue("menu_ringNoise.png"))
        shader.set(
            "rotation",
            mx,
            0f,
            my
        )

        pg.filter(shader)
        pg.endDraw()
    }
}
