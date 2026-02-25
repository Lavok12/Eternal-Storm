package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Core.GameControllers.GameController
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.Game.GameSystems.Entities.EntitySystem
import la.vok.Game.GameSystems.EntityComponents.GravityComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.Game.GameSystems.EntityComponents.RigidBody
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class Entity(var entityType: AbstractEntityType, var gameController: GameController) {
    lateinit var entitySystem: EntitySystem
    lateinit var mapSystem: MapSystem

    val coreController: CoreController get() = entitySystem.entityController.gameController.coreController
    val entityApi: EntityApi get() = entitySystem.entityController.entityApi

    fun getEntityRenderContainer(): LayersRenderContainer<RenderLayers.Main> {
        return gameController.gameRender.getEntityContainer()
    }

    var rigidBody = RigidBody(this)
    var gravityComponent = GravityComponent(this, rigidBody, -0.035f)
    var hpBody = HpBody(this)

    val hitboxes = LinkedHashMap<String, HitboxComponent>()

    var mainHitbox: HitboxComponent?
        get() = hitboxes["main"]
        set(value) {
            if (value != null) hitboxes["main"] = value
            else hitboxes.remove("main")
        }

    var position = 0 v 0
    var size = 1 v 1
    var systemId = 0L
    var facing = 1

    fun addHitbox(name: String, type: HitboxTypes = HitboxTypes.COLLISION, rigidBody: RigidBody = this.rigidBody): HitboxComponent {
        val hb = HitboxComponent(type, this, rigidBody)
        hitboxes[name] = hb
        return hb
    }
    fun isHitboxBlockCollision(name: String) : Boolean {
        return hitboxes[name]?.blocksCollision == true
    }
    fun isAnyPhysicBlockCollision() : Boolean {
        for (i in hitboxes.values) {
            if (i.hitboxType == HitboxTypes.COLLISION) {
                if (i.blocksCollision) {
                    return true;
                }
            }
        }
        return false
    }

    open fun spawn() {
        hpBody.setMaxHp()
        size = entityType.baseSize.copy()

        createBaseHitbox()
        createCustomHitboxes()
    }
    open fun createBaseHitbox() {
        val main = addHitbox("main")
        main.size = size.copy()
    }
    open fun createCustomHitboxes() {

    }

    open var renderEntity: RenderObjectInterface<RenderLayers.Main> = BaseRenderEntity(getEntityRenderContainer())

    open fun physicUpdate() {
        hitboxes.values.forEach { it.resetBlockCollision() }
        gravityComponent.useGravity()
        updateHitboxes()
        rigidBody.useSpeed()
        rigidBody.useFriction()
        do {
            rigidBody.deltaStep()
            updateHitboxes()
        } while (rigidBody.containsSteps())
    }

    open fun updateHitboxes() {
        hitboxes.values.forEach { it.hitboxUpdate() }
    }

    open fun logicalUpdate() {

    }

    open fun renderUpdate() {
        renderEntity.ROI_pos = position.copy()
        renderEntity.ROI_size = size.copy()

        hitboxes.values.forEach { it.renderUpdate() }
    }

    open fun show() {
        renderEntity.show()
        hitboxes.values.forEach { it.hitboxRender.show() }
    }

    open fun hide() {
        renderEntity.hide()
        hitboxes.values.forEach { it.hitboxRender.hide() }
    }
}