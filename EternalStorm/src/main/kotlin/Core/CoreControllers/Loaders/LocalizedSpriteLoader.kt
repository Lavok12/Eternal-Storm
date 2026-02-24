package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState
import processing.core.PImage

class LocalizedSpriteLoader(
    var coreController: CoreController,
    language: String
) : Controller, ContentLoader<PImage> {

    override val pathMap: MutableMap<String, String> = mutableMapOf()
    override val valueMap: MutableMap<String, PImage> = mutableMapOf()
    override val contentMap: MutableMap<String, Content<PImage>> = mutableMapOf()

    private var currentLanguage: String = language

    init {
        create()
    }

    override fun tick() {
        super.superTick()
    }

    override fun loadPaths() {
        AppState.logger.debug("[LocalizedSpriteLoader] Loading localized sprites for '$currentLanguage'")

        pathMap.clear()
        valueMap.clear()
        contentMap.clear()

        val langDir = "${AppState.localizedImagesPath}/$currentLanguage"

        val files = LFileUtility(langDir, true).getAllFilesRecursive()
        val fileNames = LFileUtility(langDir, false).getAllFilesRecursive()

        AppState.logger.debug("[LocalizedSpriteLoader] Found ${files.size} localized images")

        for (i in files.indices) {
            val name = fileNames[i]
            val path = files[i]
            AppState.logger.trace("[LocalizedSpriteLoader] $name -> $path")
            pathMap[name] = path
        }
    }

    override fun loadValue(key: String): PImage {
        val path = pathMap[key]
            ?: error("Localized image '$key' not found for language '$currentLanguage'")

        AppState.logger.debug("[LocalizedSpriteLoader] Loading '$key' from '$path'")
        return AppState.main.loadImage(path)
    }

    fun changeLanguage(language: String) {
        if (language == currentLanguage) return

        AppState.logger.debug("[LocalizedSpriteLoader] changeLanguage '$currentLanguage' → '$language'")

        currentLanguage = language
        loadPaths()
    }
}
