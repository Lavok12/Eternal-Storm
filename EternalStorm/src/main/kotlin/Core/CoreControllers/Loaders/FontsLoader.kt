package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState
import processing.core.PFont

class FontsLoader(var coreController: CoreController) : Controller, ContentLoader<PFont> {

    init {
        create()
    }

    override val pathMap: MutableMap<String, String> = mutableMapOf()
    override val valueMap: MutableMap<String, PFont> = mutableMapOf()
    override val contentMap: MutableMap<String, Content<PFont>> = mutableMapOf()

    override fun loadPaths() {
        AppState.logger.debug("[FontsLoader] Loading fonts from all sources")

        pathMap.clear()
        valueMap.clear()
        contentMap.clear()

        val sortedSources = coreController.objectRegistration.resourceSources.sortedBy { it.priority }

        for (source in sortedSources) {
            val fontsPath = "${source.rootPath}/${AppState.FOLDER_FONTS}"
            val utility = LFileUtility(fontsPath, true)
            
            if (!utility.isExists()) continue

            val files = utility.getAllFilesRecursive()
            val fileNames = LFileUtility(fontsPath, false).getAllFilesRecursive()

            AppState.logger.debug("[FontsLoader] Source '${source.namespace}' found ${files.size} font files")

            for (i in files.indices) {
                val name = fileNames[i]
                val path = files[i]

                if (name.endsWith(".ttf", true) || name.endsWith(".otf", true) || name.endsWith(".vlw", true)) {
                    val fullKey = "${source.namespace}:$name".intern()
                    AppState.logger.trace("[FontsLoader] Registering $fullKey -> $path")
                    pathMap[fullKey] = path
                }
            }
        }
    }

    override fun loadValue(key: String): PFont {
        val fullKey = resolveKey(key)
        val path = pathMap[fullKey] ?: error("Font '$fullKey' not found")

        AppState.logger.debug("[FontsLoader] Loading font '$fullKey' from '$path'")

        return if (fullKey.endsWith(".vlw", true)) {
            AppState.main.loadFont(path)
        } else {
            AppState.main.createFont(path, 32f, true)
        }
    }
}
