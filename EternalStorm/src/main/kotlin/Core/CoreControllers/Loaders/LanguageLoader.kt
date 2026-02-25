package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState

class LanguageLoader(
    var coreController: CoreController,
    language: String
) : Controller, ContentLoader<String> {

    private var currentLanguage: String = language

    override val pathMap: MutableMap<String, String> = mutableMapOf()
    override val valueMap: MutableMap<String, String> = mutableMapOf()
    override val contentMap: MutableMap<String, Content<String>> = mutableMapOf()

    init {
        create()
        loadPaths()
    }
    override fun logicalTick() {
        super.superTick()
    }


    private fun parseLanguageFile(path: String) {
        val lines = AppState.main.loadStrings(path)

        for ((index, rawLine) in lines.withIndex()) {
            val line = rawLine.trim()

            // пустые строки и комментарии
            if (line.isEmpty() || line.startsWith("//")) continue

            // ожидаемый формат: "key": "value"
            val splitIndex = line.indexOf(':')

            if (splitIndex == -1) {
                error("Invalid language line in $path at ${index + 1}: $line")
            }

            val rawKey = line.substring(0, splitIndex).trim()
            val rawValue = line.substring(splitIndex + 1).trim()

            val key = rawKey.removeSurrounding("\"")
            val value = rawValue
                .removeSuffix(",")
                .removeSurrounding("\"")

            valueMap[key] = value
        }
    }

    override fun loadPaths() {
        AppState.logger.debug("[LanguageLoader] Loading language '$currentLanguage'")

        pathMap.clear()
        valueMap.clear()

        val langPath = "${AppState.languagePath}/$currentLanguage"
        val files = LFileUtility(langPath, true).getAllFilesRecursive()

        AppState.logger.debug("[LanguageLoader] Found ${files.size} language files")

        for (file in files) {
            AppState.logger.trace("[LanguageLoader] Parsing $file")
            parseLanguageFile(file)
        }
    }

    override fun loadValue(key: String): String {
        AppState.logger.trace("[LanguageLoader] loadValue('$key')")
        return valueMap[key]
            ?: error("Language key '$key' not found for language '$currentLanguage'")
    }

    fun changeLanguage(language: String) {
        AppState.logger.debug("[LanguageLoader] changeLanguage '$currentLanguage' → '$language'")

        if (language == currentLanguage) return

        currentLanguage = language
        valueMap.clear()
        contentMap.clear()
        loadPaths()
    }


    fun getCurrentLanguage(): String = currentLanguage
}
