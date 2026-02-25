package la.vok.Core.CoreControllers

import Core.CoreControllers.Loaders.MainContentRegistration
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
import Core.CoreControllers.ObjectRegistration
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
    var mainContentRegistration = MainContentRegistration(this)

    var sceneController: SceneController? = null

    var objectRegistration = ObjectRegistration(this)

    fun start() {
        spriteLoader.loadPaths()
        localizedSpriteLoader.loadPaths()
        shaderLoader.loadPaths()
        languageLoader.loadPaths()
        fontsLoader.loadPaths()
        localizedFontsLoader.loadPaths()
    }

    fun setScene(scene: SceneController) {
        sceneController?.deactivate(this)
        sceneController = scene
        scene.activate(this)
        scene.start()
    }
    fun removeScene() {
        sceneController?.deactivate(this)
        sceneController = null
    }

    override fun logicalTick() {
        super.superTick()

        objectRegistration.logicalTick()

        spriteLoader.logicalTick()
        localizedSpriteLoader.logicalTick()
        shaderLoader.logicalTick()
        languageLoader.logicalTick()
        fontsLoader.logicalTick()
        localizedFontsLoader.logicalTick()

        mouseInput.logicalTick()
        mouseInputProcessing.logicalTick()
        keyboardInput.logicalTick()
        keyboardProcessing.logicalTick()
        tooltipController.logicalTick()

        if (FrameLimiter.shouldUpdatePhysics()) physicTick()
        sceneController?.logicalTick()

        windowsManager.logicalTick()

        renderTick()
    }

    fun renderTick() {
        if (FrameLimiter.shouldRender()) {
            mainRender.logicalTick()
            mainRender.useLG()
        }
    }

    override fun physicTick() {
        windowsManager.physicTick()

        objectRegistration.physicTick()

        spriteLoader.physicTick()
        localizedSpriteLoader.physicTick()
        shaderLoader.physicTick()
        languageLoader.physicTick()
        fontsLoader.physicTick()
        localizedFontsLoader.physicTick()

        mouseInput.physicTick()
        mouseInputProcessing.physicTick()
        keyboardInput.physicTick()
        keyboardProcessing.physicTick()
        tooltipController.physicTick()

        sceneController?.physicTick()
    }
}