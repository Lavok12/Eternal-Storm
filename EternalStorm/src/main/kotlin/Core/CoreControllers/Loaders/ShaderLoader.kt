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
    override fun logicalTick() {
        super.superTick()
    }

    override val pathMap: MutableMap<String, String> = mutableMapOf<String, String>()
    override val valueMap: MutableMap<String, PShader> = mutableMapOf<String, PShader>()
    override val contentMap: MutableMap<String, Content<PShader>> = mutableMapOf<String, Content<PShader>>()

    override fun loadPaths() {
        AppState.logger.debug("[ShaderLoader] Loading shaders")

        val files = LFileUtility(AppState.shadersPath, true).getAllFilesRecursive()
        val fileNames = LFileUtility(AppState.shadersPath, false).getAllFilesRecursive()

        AppState.logger.debug("[ShaderLoader] Found ${files.size} shaders")

        for (i in files.indices) {
            AppState.logger.trace("[ShaderLoader] ${fileNames[i]} -> ${files[i]}")
            pathMap[fileNames[i]] = files[i]
        }
    }

    override fun loadValue(key: String): PShader {
        AppState.logger.debug("[ShaderLoader] Loading shader '$key'")
        return AppState.main.loadShader(pathMap[key])
    }

}