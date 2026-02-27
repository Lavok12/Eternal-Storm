package la.vok.Game.GameContent.VfxObjects

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsApi
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsSystem
import la.vok.LavokLibrary.Vectors.v

abstract class AbstractVfxObject(var vfxObjectsSystem: VfxObjectsSystem, var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController
    val coreController: CoreController get() = gameController.coreController
    val entityApi: EntityApi get() = gameCycle.entityApi
    val mapApi: MapApi = gameCycle.mapApi
    val vfxObjectsApi: VfxObjectsApi = gameCycle.vfxObjectsApi

    fun getVfxObjectContainer(): LayersRenderContainer<RenderLayers.Main> =
        gameController.gameRender.getEntityContainer()

    open var renderComponent: RenderObjectInterface<RenderLayers.Main>? = null

    var physicTicks = -1L
    var logicalTicks = -1L
    var renderFrames = -1L

    var lifetime = 100L
    var progress = 0f

    var isDead = false

    var position = 0 v 0
    var size = 1 v 1

    open fun physicUpdate() {
        physicTicks++
        if (lifetime != -1L) {
            progress = physicTicks / lifetime.toFloat()
            if (physicTicks > lifetime) {
                vfxObjectsApi.killInSystem(this)
            }
        }
    }

    open fun logicalUpdate() {
        logicalTicks++
    }

    open fun renderUpdate() {
        renderFrames++
        renderComponent?.ROI_pos = position
        renderComponent?.ROI_size = size
    }

    open fun show() {
        renderComponent?.show()
    }
    open fun hide() {
        renderComponent?.hide()
    }
    open fun kill() {
        hide()
        isDead = true
    }
}