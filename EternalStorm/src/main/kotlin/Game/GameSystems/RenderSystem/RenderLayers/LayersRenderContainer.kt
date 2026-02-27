package la.vok.Core.GameContent.RenderSystem.RenderLayers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.LLibs.Logger.Logger
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState
import kotlin.collections.iterator

class LayersRenderContainer (
    val gameRender: GameRender,
) {
    private val logger: Logger = AppState.logger

    private val layers: Array<SublayersRenderContainer> =
        Array(RenderLayers.Main.entries.size) { index ->
            SublayersRenderContainer(this, index).also {
                logger.trace("Created SublayersRenderContainer for layer #$index")
            }
        }

    init {
        logger.info("LayersRenderContainer initialized with ${layers.size} layers of type ${RenderLayers.Main::class.simpleName}")
    }

    fun addPrint(obj: RenderObjectInterface, layer: RenderLayers.Main? = null, subLayer: Int? = null) {
        val l = layer?.ordinal ?: obj.layerData.layer.ordinal
        val s = subLayer ?: obj.layerData.sublayer

        if (l !in layers.indices) {
            logger.error("addPrint: invalid layer=$l for $obj")
            return
        }

        layers[l].addPrint(obj, s)
        logger.trace("addPrint: $obj -> layer=$l subLayer=$s")
    }

    fun removePrint(obj: RenderObjectInterface, layer: RenderLayers.Main? = null, subLayer: Int? = null) {
        val l = layer?.ordinal ?: obj.layerData.layer.ordinal
        val s = subLayer ?: obj.layerData.sublayer

        if (l !in layers.indices) {
            logger.error("removePrint: invalid layer=$l for $obj")
            return
        }

        layers[l].removePrint(obj, s)
        logger.trace("removePrint: $obj <- layer=$l subLayer=$s")
    }

    fun drawLayer(layer: RenderLayers.Main, lGraphics: LGraphics, camera: Camera) {
        val l = layer.ordinal
        if (l !in layers.indices) {
            logger.warn("drawLayer called with invalid layer=$l")
            return
        }

        logger.trace("drawLayer: starting drawing layer #$l (${layer.name}) with ${layers[l]} objects")

        layers[l].resetIterator()
        var count = 0
        for (obj in layers[l]) {
            try {
                obj.draw(lGraphics, camera.useCamera(obj.ROI_pos+obj.ROI_delta), camera.useCameraSize(obj.ROI_size), camera)
                count++
            } catch (t: Throwable) {
                logger.error("Error drawing object $obj on layer=$l", t)
            }
        }

        logger.trace("drawLayer: finished drawing layer #$l, rendered $count objects")
    }

    // Дополнительно: метод для отладки количества объектов в каждом слое
    fun logLayersState() {
        layers.forEachIndexed { index, subLayerContainer ->
            logger.debug("Layer #$index has $subLayerContainer objects")
        }
    }
}
