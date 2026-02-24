package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState
import processing.core.PImage

class SpriteLoader(var coreController: CoreController) : Controller, ContentLoader<PImage> {
    init {
        create()
    }
    override fun tick() {
        super.superTick()
    }

    override val pathMap: MutableMap<String, String> = mutableMapOf<String, String>()
    override val valueMap: MutableMap<String, PImage> = mutableMapOf<String, PImage>()
    override val contentMap: MutableMap<String, Content<PImage>> = mutableMapOf<String, Content<PImage>>()

    override fun loadPaths() {
        AppState.logger.debug("[SpriteLoader] Loading sprites")

        val files = LFileUtility(AppState.imagesPath, true).getAllFilesRecursive()
        val fileNames = LFileUtility(AppState.imagesPath, false).getAllFilesRecursive()

        AppState.logger.debug("[SpriteLoader] Found ${files.size} images")

        for (i in files.indices) {
            AppState.logger.trace("[SpriteLoader] ${fileNames[i]} -> ${files[i]}")
            pathMap[fileNames[i]] = files[i]
        }
    }

    override fun loadValue(key: String): PImage {
        AppState.logger.debug("[SpriteLoader] Loading image '$key'")
        return AppState.main.loadImage(pathMap[key])
    }

}