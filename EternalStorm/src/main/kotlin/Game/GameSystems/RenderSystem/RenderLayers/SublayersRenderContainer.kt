package la.vok.Core.GameContent.RenderSystem.RenderLayers

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface

class SublayersRenderContainer<T : Enum<T>>(
    val layersRenderContainer: LayersRenderContainer<T>,
    val layerIndex: Int,
    name: String = ""
) : Iterator<RenderObjectInterface<T>> {

    private val prints = HashMap<Int, HashSet<RenderObjectInterface<T>>>()

    private var layerIterator: Iterator<HashSet<RenderObjectInterface<T>>> = emptyList<HashSet<RenderObjectInterface<T>>>().iterator()
    private var currentSetIterator: Iterator<RenderObjectInterface<T>> = emptyList<RenderObjectInterface<T>>().iterator()

    fun initSubLayer(subLayer: Int) {
        if (!prints.containsKey(subLayer)) {
            prints[subLayer] = HashSet()
        }
    }

    fun addPrint(obj: RenderObjectInterface<T>, subLayer: Int) {
        initSubLayer(subLayer)
        prints[subLayer]!!.add(obj)
    }

    fun removePrint(obj: RenderObjectInterface<T>, subLayer: Int) {
        prints[subLayer]?.remove(obj)
    }

    fun removePrint(obj: RenderObjectInterface<T>) {
        for (i in prints.keys) {
            removePrint(obj, i)
        }
    }

    fun resetIterator() {
        layerIterator = prints.values.iterator()
        currentSetIterator = emptyList<RenderObjectInterface<T>>().iterator()
    }

    override fun hasNext(): Boolean {
        while (true) {
            if (currentSetIterator.hasNext()) return true
            if (!layerIterator.hasNext()) return false
            currentSetIterator = layerIterator.next().iterator()
        }
    }

    override fun next(): RenderObjectInterface<T> {
        if (!hasNext()) throw NoSuchElementException()
        return currentSetIterator.next()
    }
}
