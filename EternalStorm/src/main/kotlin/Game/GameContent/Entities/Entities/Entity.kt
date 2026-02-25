package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.Game.GameSystems.Entities.EntitySystem
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class Entity(var entityType: AbstractEntityType, var gameController: GameController) {
    lateinit var entitySystem: EntitySystem
    val coreController: CoreController get() = entitySystem.entityController.gameController.coreController
    val entityApi: EntityApi get() = entitySystem.entityController.entityApi
    fun getEntityRenderContainer() : LayersRenderContainer<RenderLayers.Main> {
        return gameController.gameRender.getEntityContainer()
    }
    init {
        spawn()
    }
    var position = 0 v 0
    var entityHp = 0
    var systemId = -1L

    open fun spawn() {
        var position = 0 v  0
        var entityHp = entityType.hp
    }

    open var renderEntity: RenderObjectInterface<RenderLayers.Main> = BaseRenderEntity(getEntityRenderContainer())

    open fun physicUpdate() {

    }
    open fun logicUpdate() {

    }
    open fun renderUpdate() {
        renderEntity.ROI_pos = position
    }

    open fun show() {
        renderEntity.show()
    }
    open fun hide() {
        renderEntity.hide()
    }
}