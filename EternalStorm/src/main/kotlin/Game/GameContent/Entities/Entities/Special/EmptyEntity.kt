package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.HpRender
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.GravityComponent
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.Game.GameSystems.EntityComponents.PhysicsComponent

open class EmptyEntity(gameCycle: GameCycle) : Entity(AbstractEntityType.EmptyEntityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = null
    override var hpRender: HpRender? = null
    init {
        removeComponent<PhysicsComponent>()
        removeComponent<GravityComponent>()
        removeComponent<HpBody>()
        hasDownTrigger = false
        hasCollisionDetector = false
    }

    override fun spawnDieParticles() {}
    override fun spawn() { size = entityType.baseSize.copy() }
    override fun physicUpdate() {}
    override fun renderUpdate() {}
}