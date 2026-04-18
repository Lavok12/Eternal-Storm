package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState
import processing.core.PFont

class LocalizedFontsLoader(
    var coreController: CoreController,
    language: String
) : Controller, ContentLoader<PFont> {

    override val pathMap: MutableMap<String, String> = mutableMapOf()
    override val valueMap: MutableMap<String, PFont> = mutableMapOf()
    override val contentMap: MutableMap<String, Content<PFont>> = mutableMapOf()

    private var currentLanguage: String = language

    init {
        create()
    }

    override fun loadPaths() {
        AppState.logger.debug("[LocalizedFontsLoader] Loading localized fonts for '$currentLanguage' from all sources")

        pathMap.clear()
        valueMap.clear()
        contentMap.clear()

        val sortedSources = coreController.objectRegistration.resourceSources.sortedBy { it.priority }

        for (source in sortedSources) {
            val langDir = "${source.rootPath}/${AppState.FOLDER_FONTS_LOCALIZED}/$currentLanguage"
            val utility = LFileUtility(langDir, true)
            
            if (!utility.isExists()) continue

            val files = utility.getAllFilesRecursive()
            val fileNames = LFileUtility(langDir, false).getAllFilesRecursive()

            AppState.logger.debug("[LocalizedFontsLoader] Source '${source.namespace}' found ${files.size} localized font files")

            for (i in files.indices) {
                val name = fileNames[i]
                val path = files[i]

                if (name.endsWith(".ttf", true) || name.endsWith(".otf", true) || name.endsWith(".vlw", true)) {
                    val fullKey = "${source.namespace}:$name".intern()
                    AppState.logger.trace("[LocalizedFontsLoader] Registering $fullKey -> $path")
                    pathMap[fullKey] = path
                }
            }
        }
    }

    override fun loadValue(key: String): PFont {
        val fullKey = resolveKey(key)
        val path = pathMap[fullKey]
            ?: error("Localized font '$fullKey' not found for language '$currentLanguage'")

        AppState.logger.debug("[LocalizedFontsLoader] Loading font '$fullKey' from '$path'")

        return if (fullKey.endsWith(".vlw", true)) {
            AppState.main.loadFont(path)
        } else {
            AppState.main.createFont(path, 32f, true)
        }
    }

    /**
     * Позволяет сменить язык и обновить карту путей к шрифтам
     */
    fun changeLanguage(language: String) {
        if (language == currentLanguage) return

        AppState.logger.debug("[LocalizedFontsLoader] changeLanguage '$currentLanguage' → '$language'")

        currentLanguage = language
        loadPaths()
    }
}