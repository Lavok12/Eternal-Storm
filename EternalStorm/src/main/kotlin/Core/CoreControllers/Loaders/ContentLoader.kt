package la.vok.Core.CoreControllers.Loaders

import la.vok.State.AppState

interface ContentLoader<T> {

    val pathMap: MutableMap<String, String>
    val valueMap: MutableMap<String, T>
    val contentMap: MutableMap<String, Content<T>>

    fun loadPaths()
    fun loadValue(key: String): T

    fun getValue(key: String): T {
        AppState.logger.trace("[${this::class.simpleName}] getValue('$key')")

        valueMap[key]?.let {
            AppState.logger.trace("[${this::class.simpleName}] cache hit for '$key'")
            return it
        }

        val path = pathMap[key]
            ?: error("Key '$key' not found in pathMap (${this::class.simpleName})")

        AppState.logger.debug("[${this::class.simpleName}] loading '$key' from '$path'")

        val value = loadValue(key)
        valueMap[key] = value
        return value
    }

    fun getContent(key: String): Content<T> {
        AppState.logger.trace("[${this::class.simpleName}] getContent('$key')")

        if (!pathMap.containsKey(key)) {
            error("Key '$key' not found in pathMap (${this::class.simpleName})")
        }

        return contentMap.getOrPut(key) {
            AppState.logger.trace("[${this::class.simpleName}] creating Content for '$key'")
            Content(this, key)
        }
    }

    fun contains(key: String): Boolean = pathMap.containsKey(key)
}
