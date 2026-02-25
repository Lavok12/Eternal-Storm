package la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects

class RenderGroup {
    private val objects = mutableListOf<RenderObjectInterface<*>>()

    fun add(vararg renderObjects: RenderObjectInterface<*>) {
        objects.addAll(renderObjects)
    }

    fun showAll() {
        objects.forEach { it.show() }
    }

    fun hideAll() {
        objects.forEach { it.hide() }
    }

    fun clear() {
        hideAll()
        objects.clear()
    }
}