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

    private fun parseLanguageFile(path: String, namespace: String) {
        val lines = AppState.main.loadStrings(path) ?: return

        for ((index, rawLine) in lines.withIndex()) {
            val line = rawLine.trim()

            if (line.isEmpty() || line.startsWith("//")) continue

            val splitIndex = line.indexOf(':')

            if (splitIndex == -1) {
                AppState.logger.error("Invalid language line in $path at ${index + 1}: $line")
                continue
            }

            val rawKey = line.substring(0, splitIndex).trim()
            val rawValue = line.substring(splitIndex + 1).trim()

            val keyName = rawKey.removeSurrounding("\"")
            val value = rawValue
                .removeSuffix(",")
                .removeSurrounding("\"")

            // Store as namespace:key
            val fullKey = "$namespace:$keyName".intern()
            valueMap[fullKey] = value
        }
    }

    override fun loadPaths() {
        AppState.logger.debug("[LanguageLoader] Loading translations for '$currentLanguage' from all sources")

        pathMap.clear()
        valueMap.clear()

        val sortedSources = coreController.objectRegistration.resourceSources.sortedBy { it.priority }

        for (source in sortedSources) {
            val langPath = "${source.rootPath}/${AppState.FOLDER_LANG}/$currentLanguage"
            val utility = LFileUtility(langPath, true)
            
            if (!utility.isExists()) continue

            val files = utility.getAllFilesRecursive()
            AppState.logger.debug("[LanguageLoader] Source '${source.namespace}' found ${files.size} lang files")

            for (file in files) {
                AppState.logger.trace("[LanguageLoader] Parsing $file for namespace '${source.namespace}'")
                parseLanguageFile(file, source.namespace)
                
                // Track file paths in pathMap for completeness (though not used for loading value)
                // We use a dummy key since one key can come from multiple files, but for the loader API
                // we technically need a mapping. We'll use the first file encountered or similar.
                // Actually, LanguageLoader is a bit different as it loads everything at once.
                // We'll fill pathMap with keys found so far to satisfy ContentLoader.contains()
            }
        }
        
        // Finalize pathMap so getValue() works (it checks pathMap)
        valueMap.keys.forEach { pathMap[it] = "memory" }
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
