package la.vok.Menu.MenuContent.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.FrameLimiter
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Menu.MenuContent.MenuAurora
import la.vok.Menu.MenuContent.MenuCommet
import la.vok.Menu.MenuController.MenuController
import la.vok.State.AppState
import kotlin.math.PI

class WMenuPanel(windowsManager: WindowsManager, var menuController: MenuController) : WStandartPanel(windowsManager) {

    private val comets = mutableListOf<MenuCommet>()
    private var spawnTimer = 0f
    private val menuAurora = MenuAurora(this)

    override var padding: Vec2
        get() = Vec2.ZERO
        set(value) {}

    override var tags: Array<String>
        get() = arrayOf("game")
        set(value) {}

    private fun createMenuButton(index: Int, yPos: Float, title: String) {
        val button = WindowElement(
            this,
            (-30) v yPos,   // Позиция по Y теперь передается
            500 v 125,
            -1 v 0,

            start = {
                setFloat("hover", 0f)
            },
            physicUpdate = {
                val h = getFloat("hover") ?: 0f
                val target = if (mouseInside) 1f else 0f
                val speed = 0.15f

                val newH = h + (target - h) * speed

                // Смещение по X при наведении
                position.x = -30 + newH * 30f
                markDirty()

                setFloat("hover", newH)
            },
            render = {
                val h = getFloat("hover") ?: 0f

                // Цвета (можно также вынести в константы или параметры)
                val baseR = 200f; val baseG = 60f; val baseB = 30f
                val greenR = 60f; val greenG = 200f; val greenB = 60f

                fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t

                val r = lerp(baseR, greenR, h)
                val g = lerp(baseG, greenG, h)
                val b = lerp(baseB, greenB, h)

                // 1. Подсветка (part3)
                lg.setTint(r, g, b, 50f)
                lg.setImage(coreController.spriteLoader.getValue("menu_button_part3.png"), positionWithCache + (0 v 10), size)

                // 2. Нейтральный слой (part2)
                lg.setTint(140f, 110f, 70f)
                lg.setImage(coreController.spriteLoader.getValue("menu_button_part2.png"), positionWithCache, size)

                // 3. Основной цветной слой (part1)
                lg.setTint(r, g, b, 255f)
                lg.setImage(coreController.spriteLoader.getValue("menu_button_part1.png"), positionWithCache, size)

                lg.noTint()

                // 4. Текст (локализованный шрифт подхватится автоматически, если использовать LocalizedFontsLoader)
                lg.pg.textFont(coreController.fontsLoader.getValue("pobeda-bold_120px.ttf"))
                lg.setTextAlign(0, 0)
                lg.fill(70 + r / 1.5f, 70 + g / 2f, 70 + b / 2.5f)
                lg.setText(title, positionWithCache + (-20 v 5), 60f)
            }
        )
        windowElements.add(button)
    }

    fun rebuildButtons() {
        windowElements.clear()

        createMenuButton(0, 120f, "Play")
        createMenuButton(1, 0f, "Settings")
        createMenuButton(2, -120f, "Exit")
    }


    override fun start() {
        rebuildButtons()
    }
    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)

        // --- фон ---
        lg.noStroke()
        lg.setTint(40f, 40f, 40f)
        lg.setImage(coreController.spriteLoader.getValue("menu_space.jpg"), Vec2(0f, 0f), logicalSize)
        lg.noTint()
        lg.setImage(menuController.planetFinal.getImage(), Vec2(0f, 0f), Vec2(1600f, 1600f))
        lg.fill(20f, 10f, 10f, 50f)
        lg.setBlock(Vec2(0f, 0f), logicalSize)

        renderComets()
        menuAurora.render() // отрисовываем сияние

        // --- вода и планеты через шейдер ---
        val shader = coreController.shaderLoader.getValue("menu_water_ssr.glsl")
        lg.restartDraw()
        shader.set("resolution", lg.pg.width.toFloat(), lg.pg.height.toFloat())
        shader.set("u_frame", menuController.frame * 3f)
        shader.set("u_waterLevel", 0.36f)
        shader.set("u_texture", lg.pg)
        shader.set("plan1", coreController.spriteLoader.getValue("menu_planet1.jpg"))
        shader.set("plan2", coreController.spriteLoader.getValue("menu_planet2.jpg"))
        shader.set("plan3", coreController.spriteLoader.getValue("menu_planet4.png"))
        shader.set("plan4", coreController.spriteLoader.getValue("menu_planet3.jpg"))
        lg.pg.shader(shader)
        lg.setBlock(Vec2(0f, 0f), Vec2(logicalSize.x, logicalSize.y))
        lg.resetShader()


        val menu_smoke = coreController.shaderLoader.getValue("menu_smoke.glsl")
        menu_smoke.set("smoke1", coreController.spriteLoader.getValue("menu_planet3.jpg"))
        menu_smoke.set("smoke2", coreController.spriteLoader.getValue("menu_tuman.jpg"))
        menu_smoke.set("smoke3", coreController.spriteLoader.getValue("menu_ringNoise.png"))
        menu_smoke.set("resolution", lg.pg.width.toFloat(), lg.pg.height.toFloat())
        menu_smoke.set("hazeStrength", 0.1f)
        menu_smoke.set("parallax", 0.02f, 0.01f)
        menu_smoke.set("time", FrameLimiter.totalPhysicsFrames.toFloat())
        menu_smoke.set("hazeColor", 0.4f, 0.4f, 0.1f)
        lg.pg.shader(menu_smoke)
        lg.setBlock(0f v 0f, logicalSize)
        lg.pg.resetShader()

        val gamma = coreController.shaderLoader.getValue("menu_gamma_correction.glsl")
        gamma.set("resolution", lg.pg.width.toFloat(), lg.pg.height.toFloat())
        lg.pg.filter(gamma)

        lg.setImage(coreController.spriteLoader.getValue("menu_darkness.png"), Vec2(0f, 0f), logicalSize * 1.1f)
    }

    override fun physicUpdate() {
        super.physicUpdate()
        tickComets()
        if (FrameLimiter.totalPhysicsFrames % 3L == 0L) {
            menuAurora.physicsUpdate()
        }
    }

    private fun tickComets() {
        spawnTimer -= 1/60f
        if (spawnTimer <= 0f) {
            spawnCometBatch()
            spawnTimer = AppState.main.random(2.8f, 4.8f)
        }

        val iterator = comets.iterator()
        while (iterator.hasNext()) {
            val c = iterator.next()
            c.tick()
            if (c.isDead()) iterator.remove()
        }
    }

    private fun renderComets() {
        comets.forEach { it.render(lg) }
    }

    private fun spawnCometBatch() {
        val baseX = AppState.main.random(-logicalSize.x / 4f, logicalSize.x / 4f)
        val baseAngle = AppState.main.random((-PI / 2f -1.2f).toFloat(), (PI / 2f -1.2f).toFloat())
        val count = (2..5).random()

        repeat(count) {
            val pos = Vec2(
                baseX + AppState.main.random(-100f, 100f),
                logicalSize.y / 2f + AppState.main.random(0f, 120f)
            )
            val angle = baseAngle + AppState.main.random(-0.12f, 0.12f)
            val size = AppState.main.random(20f, 60f)
            val color = LColor(
                AppState.main.random(180f, 255f),
                AppState.main.random(180f, 190f),
                AppState.main.random(200f, 255f)
            )
            comets.add(MenuCommet(pos, size, color, angle))
        }
    }
}
