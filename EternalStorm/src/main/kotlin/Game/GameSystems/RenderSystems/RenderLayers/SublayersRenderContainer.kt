package la.vok.Core.GameContent.RenderSystem.RenderLayers

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface

class SublayersRenderContainer (
    val layersRenderContainer: LayersRenderContainer,
    val layerIndex: Int,
    name: String = ""
) : Iterator<RenderObjectInterface> {

    private val prints = HashMap<Int, HashSet<RenderObjectInterface>>()

    private var layerIterator: Iterator<HashSet<RenderObjectInterface>> = emptyList<HashSet<RenderObjectInterface>>().iterator()
    private var currentSetIterator: Iterator<RenderObjectInterface> = emptyList<RenderObjectInterface>().iterator()

    fun initSubLayer(subLayer: Int) {
        if (!prints.containsKey(subLayer)) {
            prints[subLayer] = HashSet()
        }
    }

    fun addPrint(obj: RenderObjectInterface, subLayer: Int) {
        initSubLayer(subLayer)
        prints[subLayer]!!.add(obj)
    }

    fun removePrint(obj: RenderObjectInterface, subLayer: Int) {
        prints[subLayer]?.remove(obj)
    }

    fun removePrint(obj: RenderObjectInterface) {
        for (i in prints.keys) {
            removePrint(obj, i)
        }
    }

    fun resetIterator() {
        layerIterator = prints.values.iterator()
        currentSetIterator = emptyList<RenderObjectInterface>().iterator()
    }

    override fun hasNext(): Boolean {
        while (true) {
            if (currentSetIterator.hasNext()) return true
            if (!layerIterator.hasNext()) return false
            currentSetIterator = layerIterator.next().iterator()
        }
    }

    override fun next(): RenderObjectInterface {
        if (!hasNext()) throw NoSuchElementException()
        return currentSetIterator.next()
    }
}
