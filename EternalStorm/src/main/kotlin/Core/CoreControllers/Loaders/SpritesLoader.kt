package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState
import processing.core.PImage

class SpritesLoader(var coreController: CoreController) : Controller, ContentLoader<PImage> {
    init {
        create()
    }
    override val pathMap: MutableMap<String, String> = mutableMapOf<String, String>()
    override val valueMap: MutableMap<String, PImage> = mutableMapOf<String, PImage>()
    override val contentMap: MutableMap<String, Content<PImage>> = mutableMapOf<String, Content<PImage>>()

    override fun loadPaths() {
        AppState.logger.debug("[SpriteLoader] Loading sprites from all sources")
        pathMap.clear()

        val sortedSources = coreController.objectRegistration.resourceSources.sortedBy { it.priority }

        for (source in sortedSources) {
            val spritePath = "${source.rootPath}/${AppState.FOLDER_IMAGES}"
            val utility = LFileUtility(spritePath, true)
            
            if (!utility.isExists()) {
                AppState.logger.trace("[SpriteLoader] Source '${source.namespace}' has no Images directory at $spritePath")
                continue
            }

            val files = utility.getAllFilesRecursive()
            val fileNames = LFileUtility(spritePath, false).getAllFilesRecursive()

            AppState.logger.debug("[SpriteLoader] Source '${source.namespace}' found ${files.size} images")

            for (i in files.indices) {
                val fullKey = "${source.namespace}:${fileNames[i]}".intern()
                AppState.logger.trace("[SpriteLoader] Registering $fullKey -> ${files[i]}")
                pathMap[fullKey] = files[i]
            }
        }
    }

    override fun loadValue(key: String): PImage {
        val fullKey = resolveKey(key)
        val path = pathMap[fullKey] ?: error("Sprite '$fullKey' path not found")
        AppState.logger.debug("[SpriteLoader] Loading image '$fullKey' from '$path'")
        return AppState.main.loadImage(path)
    }

}