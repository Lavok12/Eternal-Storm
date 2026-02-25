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
class TestEntity(entityType: AbstractEntityType, gameController: GameController) : Entity(entityType, gameController) {
    override var renderEntity: RenderObjectInterface<RenderLayers.Main> = BaseRenderEntity(getEntityRenderContainer())
}