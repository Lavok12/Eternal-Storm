package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Entities.EntityRender.HpRender
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class ProjectileEntity(
    gameCycle: GameCycle,
    var damage: Int,
    var source: Long,
) : Entity(AbstractEntityType.ProjectileEntityType, gameCycle) {
    open var knockBack = 0.1f
    open var upKnockBack = 0.1f
    open var moveOverBlocks = false
    open var maxLifeTime = 200f

    open var targetTags: List<String> = listOf(EntityTags.enemy)
    override var renderEntity: RenderObjectInterface? = object : BaseRenderEntity(getRenderLayer()) {
        override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
            lg.fill(255f, 30f)
            lg.setEps(pos - rigidBody!!.speed * size * 0.1f, size)
            lg.setEps(pos - rigidBody!!.speed * size * 0.2f, size*0.8f)
            lg.setEps(pos - rigidBody!!.speed * size * 0.3f, size*0.6f)
            lg.setEps(pos - rigidBody!!.speed * size * 0.4f, size*0.4f)
            lg.setEps(pos - rigidBody!!.speed * size * 0.5f, size*0.2f)

            lg.fill(255f)
            lg.setEps(pos, size)
        }
    }

    override var hpRender: HpRender?
        get() = null
        set(value) {}

    init {
        rigidBody?.friction = 0f
        rigidBody?.blockFriction = 0f
        hasDownTrigger = false
        hasCollisionDetector = true
    }


    override fun createCustomHitboxes() {
        mainHitbox!!.ignoreCollision = true
        mainHitbox!!.hitboxType = HitboxTypes.ONLY_TRIGGER

        collisionDetector!!.tagFilter = TagFilter.HasAny(targetTags)
        collisionDetector!!.onContactStart = { hitbox -> onHit(hitbox) }
    }

    override fun moveStep(updateDetector: Boolean) {
        super.moveStep(updateDetector)
        if (!moveOverBlocks) {
            if (mainHitbox!!.blocksCollision) {
                blockCollision()
            }
        }
    }

    open fun blockCollision() {
        gameCycle.entityApi.killInSystem(dimension!!, systemId)
    }


    override fun physicUpdate() {
        super.physicUpdate()
        if (physicTicks > maxLifeTime) {
            gameCycle.entityApi.killInSystem(dimension!!, systemId)
        }
    }

    open fun onHit(hitbox: HitboxComponent) {
        if (isDead) return
        val target = hitbox.entity
        if (!target.isDead) {
            target.takeDamage(DamageData(
                damage,
                (rigidBody?.speed?.normalized() ?: (Vec2.ZERO)) * knockBack + (0 v upKnockBack),
                source,
                null
                ), hitbox)
            gameCycle.entityApi.killInSystem(dimension!!, systemId)
        }
    }

    override fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean {
        return false
    }

    fun launch(direction: Vec2, speed: Float) {
        rigidBody?.speed = direction.normalized() * speed
    }
}
