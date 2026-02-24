package la.vok.Core.CoreControllers

import la.vok.Core.CoreControllers.Loaders.LanguageLoader
import la.vok.Core.CoreControllers.Loaders.ShaderLoader
import la.vok.Core.CoreControllers.Loaders.SpriteLoader
import la.vok.Core.FrameLimiter
import la.vok.Core.CoreContent.Input.KeyboardInput
import la.vok.Core.CoreContent.Input.MouseInput
import la.vok.Core.CoreControllers.Interfaces.SceneController
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.Loaders.FontsLoader
import la.vok.Core.CoreControllers.Loaders.LocalizedFontsLoader
import la.vok.Core.CoreControllers.Loaders.LocalizedSpriteLoader
import la.vok.State.AppState

class CoreController : Controller {
    init {
        create()
    }

    var spriteLoader = SpriteLoader(this)
    var localizedSpriteLoader = LocalizedSpriteLoader(this, AppState.lang)
    var shaderLoader = ShaderLoader(this)
    var languageLoader = LanguageLoader(this, AppState.lang)
    var fontsLoader = FontsLoader(this)
    var localizedFontsLoader = LocalizedFontsLoader(this, AppState.lang)

    var mainRender = MainRender(this)
    var windowsManager = WindowsManager(this)
    var mouseInput = MouseInput(this)
    var mouseInputProcessing = MouseInputProcessing(this)
    var keyboardInput = KeyboardInput(this)
    var keyboardProcessing = KeyboardProcessing(this)
    var tooltipController = TooltipController(this)

    var sceneController: SceneController? = null

    fun start() {
        spriteLoader.loadPaths()
        localizedSpriteLoader.loadPaths()
        shaderLoader.loadPaths()
        languageLoader.loadPaths()
        fontsLoader.loadPaths()
        localizedFontsLoader.loadPaths()
    }

    fun setScene(scene: SceneController) {
        sceneController?.deactivate()
        sceneController = scene
        scene.activate()
    }
    fun removeScene() {
        sceneController?.deactivate()
        sceneController = null
    }

    override fun tick() {
        super.superTick()

        spriteLoader.tick()
        localizedSpriteLoader.tick()
        shaderLoader.tick()
        languageLoader.tick()
        fontsLoader.tick()
        localizedFontsLoader.tick()

        mouseInput.tick()
        mouseInputProcessing.tick()
        keyboardInput.tick()
        keyboardProcessing.tick()
        tooltipController.tick()

        if (FrameLimiter.shouldUpdatePhysics()) physicTick()
        sceneController?.tick()

        windowsManager.tick()

        renderTick()
    }

    fun renderTick() {
        if (FrameLimiter.shouldRender()) {
            mainRender.tick()
            mainRender.useLG()
        }
    }

    fun physicTick() {
        windowsManager.physicTick()
    }
}