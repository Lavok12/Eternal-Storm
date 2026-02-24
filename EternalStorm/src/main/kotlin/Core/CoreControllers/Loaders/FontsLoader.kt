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

    override fun tick() {
        super.superTick()
    }

    override fun loadPaths() {
        AppState.logger.debug("[FontsLoader] Loading fonts")

        pathMap.clear()
        valueMap.clear()
        contentMap.clear()

        val files = LFileUtility(AppState.fontsPath, true).getAllFilesRecursive()
        val fileNames = LFileUtility(AppState.fontsPath, false).getAllFilesRecursive()

        AppState.logger.debug("[FontsLoader] Found ${files.size} font files")

        for (i in files.indices) {
            val name = fileNames[i]
            val path = files[i]

            // фильтруем по расширению
            if (name.endsWith(".ttf", true) || name.endsWith(".otf", true) || name.endsWith(".vlw", true)) {
                AppState.logger.trace("[FontsLoader] $name -> $path")
                pathMap[name] = path
            }
        }
    }

    override fun loadValue(key: String): PFont {
        val path = pathMap[key] ?: error("Font '$key' not found")

        AppState.logger.debug("[FontsLoader] Loading font '$key' from '$path'")

        return if (key.endsWith(".vlw", true)) {
            AppState.main.loadFont(path)
        } else {
            // Для ttf/otf используем createFont
            AppState.main.createFont(path, 32f, true)
        }
    }
}
