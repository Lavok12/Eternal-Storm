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

    override fun logicalTick() {
        super.superTick()
    }

    override fun loadPaths() {
        AppState.logger.debug("[LocalizedFontsLoader] Loading localized fonts for '$currentLanguage'")

        pathMap.clear()
        valueMap.clear()
        contentMap.clear()


        val langDir = "${AppState.localizedFontsPath}/$currentLanguage"

        val files = LFileUtility(langDir, true).getAllFilesRecursive()
        val fileNames = LFileUtility(langDir, false).getAllFilesRecursive()

        AppState.logger.debug("[LocalizedFontsLoader] Found ${files.size} localized font files")

        for (i in files.indices) {
            val name = fileNames[i]
            val path = files[i]

            // Фильтрация по расширениям шрифтов
            if (name.endsWith(".ttf", true) || name.endsWith(".otf", true) || name.endsWith(".vlw", true)) {
                AppState.logger.trace("[LocalizedFontsLoader] $name -> $path")
                pathMap[name] = path
            }
        }
    }

    override fun loadValue(key: String): PFont {
        val path = pathMap[key]
            ?: error("Localized font '$key' not found for language '$currentLanguage'")

        AppState.logger.debug("[LocalizedFontsLoader] Loading font '$key' from '$path'")

        return if (key.endsWith(".vlw", true)) {
            AppState.main.loadFont(path)
        } else {
            // Для векторных шрифтов (ttf/otf) используем стандартный размер 32
            // В Processing размер можно переопределить при выводе текста через textFont(font, size)
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