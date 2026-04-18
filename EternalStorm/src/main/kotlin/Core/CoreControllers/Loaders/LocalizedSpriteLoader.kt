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

    override fun loadPaths() {
        AppState.logger.debug("[LocalizedSpriteLoader] Loading localized sprites for '$currentLanguage' from all sources")

        pathMap.clear()
        valueMap.clear()
        contentMap.clear()

        val sortedSources = AppState.resourceSources.sortedBy { it.priority }

        for (source in sortedSources) {
            val langDir = "${source.rootPath}/${AppState.FOLDER_IMAGES_LOCALIZED}/$currentLanguage"
            val utility = LFileUtility(langDir, true)
            
            if (!utility.isExists()) continue

            val files = utility.getAllFilesRecursive()
            val fileNames = LFileUtility(langDir, false).getAllFilesRecursive()

            AppState.logger.debug("[LocalizedSpriteLoader] Source '${source.namespace}' found ${files.size} localized images")

            for (i in files.indices) {
                val fullKey = "${source.namespace}:${fileNames[i]}".intern()
                AppState.logger.trace("[LocalizedSpriteLoader] Registering $fullKey -> ${files[i]}")
                pathMap[fullKey] = files[i]
            }
        }
    }

    override fun loadValue(key: String): PImage {
        val fullKey = resolveKey(key)
        val path = pathMap[fullKey]
            ?: error("Localized image '$fullKey' not found for language '$currentLanguage'")

        AppState.logger.debug("[LocalizedSpriteLoader] Loading '$fullKey' from '$path'")
        return AppState.main.loadImage(path)
    }

    fun changeLanguage(language: String) {
        if (language == currentLanguage) return

        AppState.logger.debug("[LocalizedSpriteLoader] changeLanguage '$currentLanguage' → '$language'")

        currentLanguage = language
        loadPaths()
    }
}
