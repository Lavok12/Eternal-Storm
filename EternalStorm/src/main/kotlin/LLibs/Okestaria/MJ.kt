package la.vok.LLibs.Okestaria

import la.vok.Core.CoreControllers.CoreController
import la.vok.State.AppState
import processing.core.PConstants
import processing.core.PImage

object MJ {
    var pg = AppState.main.createGraphics(4000, 2000, PConstants.P2D)
    var pg2 = AppState.main.createGraphics(4000, 4000, PConstants.P2D)
    val coreController: CoreController get() {return AppState.coreController }

    var frame = 0.0f
    fun tick() {
        AppState.main.frameRate(60f)

        generate(frame)
        frame++

        render()
    }

    fun generate(frame: Float) {
        var shader = coreController.shaderLoader.getValue("jupiter.glsl")
        var shader2 = coreController.shaderLoader.getValue("3dPlanet.glsl")
        pg.beginDraw()
        pg.background(0f)
        shader.set("resolution", pg.width.toFloat(), pg.height.toFloat())
        shader.set("u_frame", frame*2f)

        shader.set("plan1", coreController.spriteLoader.getValue("plan1.jpg"))
        shader.set("plan2", coreController.spriteLoader.getValue("plan2.jpg"))
        shader.set("plan3", coreController.spriteLoader.getValue("plan3.png"))
        shader.set("plan4", coreController.spriteLoader.getValue("plan4.jpg"))

        pg.noStroke()
        pg.filter(shader)
        pg.endDraw()

        pg2.beginDraw()
        shader2.set("resolution", pg.width.toFloat(), pg.height.toFloat())
        shader2.set("u_texture", pg)
        shader2.set("ringMap", coreController.spriteLoader.getValue("ringNoise.png"))
        shader2.set("rotation", AppState.main.mouseY/100f, 0f, AppState.main.mouseX/100f)

        pg2.filter(shader2)
        pg2.endDraw()

    }
    fun render() {
        AppState.main.noStroke()
        AppState.main.tint(70f, 60f, 50f)
        AppState.main.image(coreController.spriteLoader.getValue("space.jpg"), 0f, 0f, AppState.main.width.toFloat(), AppState.main.height.toFloat())
        AppState.main.noTint()
        /*for (i in 0..50 step 5) {
            AppState.main.fill(28f,7f,7f,23f)
            AppState.main.ellipse(AppState.main.width/2f, AppState.main.height/2f, 550f+i, 550f+i)
        }*/
        AppState.main.image(pg as PImage, 0f, 0f, 450f, 225f)
        AppState.main.image(pg2 as PImage, AppState.main.width/2f-900, AppState.main.height/2f-900, 1800f, 1800f)
    }
}