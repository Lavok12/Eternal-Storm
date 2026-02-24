package la.vok.Menu.MenuContent

import la.vok.Core.GameContent.Layers.EffectLayer
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.State.AppState

class BGPlanetBase(point: LPoint, mp: Float = 1f) : EffectLayer(point, mp) {

    private var frame = 0f

    override fun draw() {

        val core = AppState.coreController
        val shader = core.shaderLoader.getValue("menu_jupiter_composite.glsl")

        val pg = lg!!.pg

        pg.beginDraw()
        pg.background(0f)

        shader.set("resolution", pg.width.toFloat(), pg.height.toFloat())
        shader.set("u_frame", frame * 0.5f)

        shader.set("plan1", core.spriteLoader.getValue("menu_planet1.jpg"))
        shader.set("plan2", core.spriteLoader.getValue("menu_planet2.jpg"))
        shader.set("plan3", core.spriteLoader.getValue("menu_planet4.png"))
        shader.set("plan4", core.spriteLoader.getValue("menu_planet3.jpg"))

        pg.noStroke()
        pg.filter(shader)
        pg.endDraw()

        frame++
    }
}
