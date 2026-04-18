package la.vok.Core.CoreControllers.Loaders

import la.vok.Core.CoreContent.Resources.ResourceLocation
import la.vok.State.AppState

interface ContentLoader<T> {

    val pathMap: MutableMap<String, String>
    val valueMap: MutableMap<String, T>
    val contentMap: MutableMap<String, Content<T>>

    fun loadPaths()
    fun loadValue(key: String): T

    fun resolveKey(key: String): String {
        return ResourceLocation.parse(key).toString().intern()
    }

    fun getValue(key: String): T {
        val fullKey = resolveKey(key)
        AppState.logger.trace("[${this::class.simpleName}] getValue('$fullKey')")

        valueMap[fullKey]?.let {
            return it
        }

        val path = pathMap[fullKey]
            ?: error("Key '$fullKey' not found in pathMap (${this::class.simpleName})")

        AppState.logger.debug("[${this::class.simpleName}] loading '$fullKey' from '$path'")

        val value = loadValue(fullKey)
        valueMap[fullKey] = value
        return value
    }

    fun getContent(key: String): Content<T> {
        val fullKey = resolveKey(key)
        AppState.logger.trace("[${this::class.simpleName}] getContent('$fullKey')")

        if (!pathMap.containsKey(fullKey)) {
            error("Key '$fullKey' not found in pathMap (${this::class.simpleName})")
        }

        return contentMap.getOrPut(fullKey) {
            Content(this, fullKey)
        }
    }

    fun contains(key: String): Boolean = pathMap.containsKey(resolveKey(key))
}
