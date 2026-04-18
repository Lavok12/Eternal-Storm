package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Utils.LFileUtility
import la.vok.State.AppState
import processing.opengl.PShader

class ShaderLoader(var coreController: CoreController) : Controller, ContentLoader<PShader> {
    init {
        create()
    }

    override val pathMap: MutableMap<String, String> = mutableMapOf<String, String>()
    override val valueMap: MutableMap<String, PShader> = mutableMapOf<String, PShader>()
    override val contentMap: MutableMap<String, Content<PShader>> = mutableMapOf<String, Content<PShader>>()

    override fun loadPaths() {
        AppState.logger.debug("[ShaderLoader] Loading shaders from all sources")
        pathMap.clear()

        val sortedSources = AppState.resourceSources.sortedBy { it.priority }

        for (source in sortedSources) {
            val shaderPath = "${source.rootPath}/${AppState.FOLDER_SHADERS}"
            val utility = LFileUtility(shaderPath, true)
            
            if (!utility.isExists()) continue

            val files = utility.getAllFilesRecursive()
            val fileNames = LFileUtility(shaderPath, false).getAllFilesRecursive()

            AppState.logger.debug("[ShaderLoader] Source '${source.namespace}' found ${files.size} shaders")

            for (i in files.indices) {
                val fullKey = "${source.namespace}:${fileNames[i]}".intern()
                AppState.logger.trace("[ShaderLoader] Registering $fullKey -> ${files[i]}")
                pathMap[fullKey] = files[i]
            }
        }
    }

    override fun loadValue(key: String): PShader {
        val fullKey = resolveKey(key)
        val path = pathMap[fullKey] ?: error("Shader '$fullKey' path not found")
        AppState.logger.debug("[ShaderLoader] Loading shader '$fullKey' from '$path'")
        return AppState.main.loadShader(path)
    }

}