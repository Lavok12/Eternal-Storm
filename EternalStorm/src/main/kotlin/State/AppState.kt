package la.vok.State

import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Loaders.Content
import la.vok.Core.CoreControllers.Loaders.LanguageLoader
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.Sketch.ProcessingSketch

object AppState {
    lateinit var main: ProcessingSketch
    var pixelDensityFactor = 1f;
    lateinit var coreController: CoreController
    val lg: LGraphics
        get() {return coreController.mainRender.lg}

    val imagesPath: String by lazy { main.dataPath("Content/Images") }
    val languagePath: String by lazy { main.dataPath("Content/Lang") }
    val shadersPath: String by lazy { main.dataPath("Content/Shaders") }
    val fontsPath: String by lazy { main.dataPath("Content/Fonts") }
    val localizedFontsPath: String by lazy { main.dataPath("Content/FontsLocalized") }
    val localizedImagesPath: String by lazy { main.dataPath("Content/ImagesLocalized") }

    var doubleClickDelay = 300L
    var doublePressDelay = 300L

    var maxPhysicStep = 0.1f

    var hitboxDebug: Boolean = false
        private set

    var lang = "ru"
        set(value)
            {
                field = value
                languageLoader.changeLanguage(lang)
            }


    val languageLoader: LanguageLoader get() = coreController.languageLoader

    fun getLanguageValue(key: String): String {
        return languageLoader.getValue(key)
    }
    fun getLanguageContent(key: String) : Content<String> {
        return languageLoader.getContent("key")
    }

    fun addNamespace(text: String) : String {
        return "Main:$text"
    }


    val logger = ConsoleLogger("Main", LogLevel.DEBUG, true)
}