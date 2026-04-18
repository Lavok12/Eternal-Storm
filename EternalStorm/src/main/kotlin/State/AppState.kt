package la.vok.State

import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Loaders.Content
import la.vok.Core.CoreControllers.Loaders.LanguageLoader
import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.Sketch.ProcessingSketch

import la.vok.Core.CoreContent.Resources.ResourceLocation
import la.vok.Core.CoreContent.Resources.ResourceSource

object AppState {
    lateinit var main: ProcessingSketch
    var pixelDensityFactor = 1f;
    lateinit var coreController: CoreController
    val lg: LGraphics
        get() {return coreController.mainRender.lg}

    const val FOLDER_IMAGES = "Images"
    const val FOLDER_LANG = "Lang"
    const val FOLDER_SHADERS = "Shaders"
    const val FOLDER_FONTS = "Fonts"
    const val FOLDER_FONTS_LOCALIZED = "FontsLocalized"
    const val FOLDER_IMAGES_LOCALIZED = "ImagesLocalized"

    val imagesPath: String by lazy { main.dataPath(FOLDER_IMAGES) }
    val languagePath: String by lazy { main.dataPath(FOLDER_LANG) }
    val shadersPath: String by lazy { main.dataPath(FOLDER_SHADERS) }
    val fontsPath: String by lazy { main.dataPath(FOLDER_FONTS) }
    val localizedFontsPath: String by lazy { main.dataPath(FOLDER_FONTS_LOCALIZED) }
    val localizedImagesPath: String by lazy { main.dataPath(FOLDER_IMAGES_LOCALIZED) }

    fun res(path: String): String {
        return ResourceLocation.parse(path).toString().intern()
    }

    fun tag(name: String): String {
        return ResourceLocation.parse(name).toString().intern()
    }
    // -------------------------------

    var doubleClickDelay = 300L
    var doublePressDelay = 300L

    var maxPhysicStep = 0.1f
  
    var itemsMergeSize = 4 v 4
    var mergeCheckInterval = 80L

    var hitboxDebug: Boolean = false
        set(value)
        {
            var gameController = coreController.sceneController as GameController
            field = value
            for (i in (gameController.gameCycle.entityApi.getActiveEntities(gameController.playerDimension!!))) {
                gameController.gameCycle.entityApi.hideEntity(gameController.playerDimension!!, i)
                gameController.gameCycle.entityApi.showEntity(gameController.playerDimension!!, i)
            }
        }
    var renderDebug: Boolean = false


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

    val logger = ConsoleLogger("eternal_storm", LogLevel.DEBUG, true)
}