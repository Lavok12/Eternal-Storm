package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.Ai.BossAi
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.BossRenderEntity
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.*
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.EntityParticle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import kotlin.math.PI
import kotlin.math.atan2

@Suppress("UNCHECKED_CAST")
class BossEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = BossRenderEntity(getRenderLayer())
    override var bodyDamage = 25
    override var bodyKnockBack = 0.3f
    
    // Links (still useful to keep as references, but logic moved to components)
    var first: BossEntity = this

    var rotate = 0f
    var bossPart = 1 // 1=Head, 0=Body, -1=Tail
    var bossPartsCount = 15
    var number = 0
    private val segmentLength = 2f

    init {
        removeComponent<GravityComponent>()
        hasCollisionDetector = true
        rigidBody?.friction = 0.01f
        baseBackResistance = 0.95f
    }

    override fun spawn() {
        super.spawn()
        if (bossPart == 1) {
            setupAsHead()
        }
    }

    private fun setupAsHead() {
        this.ai = BossAi(this, gameCycle)
        val deathLink = LinkedDeathComponent(this)
        addComponent(deathLink)

        var leader: Entity = this
        for (i in 0 until bossPartsCount) {
            val segment = gameCycle.entityApi.getRegisteredEntity(this.entityType.tag) as BossEntity
            segment.number = i + 1
            segment.first = this
            
            // Shared HP pool
            segment.hpBody = this.hpBody
            segment.hpRender = null // Hide HP for segments
            
            // Boss Part identification
            segment.bossPart = if (i == bossPartsCount - 1) -1 else 0 
            
            // Visuals
            segment.renderEntity?.changeLayer(RenderLayers.Main.B5, i * 2)
            
            // Positioning & Logic Components
            segment.addComponent(ChainFollowComponent(segment, leader, segmentLength))
            segment.addComponent(DamageRedirectComponent(segment, this))
            
            deathLink.linkedEntities.add(segment)
            
            gameCycle.entityApi.spawnEntity(dimension!!, segment, position)
            leader = segment
        }
    }

    private var wasTouching = false

    override fun physicUpdate() {
        super.physicUpdate()
        
        if (bossPart == 1) {
            handleHeadPhysics()
        }
        
        // Rotation sync for render
        getComponent<ChainFollowComponent>()?.let { 
            this.rotate = it.rotation 
        } ?: run {
            if (rigidBody!!.speed.length() > 0.01f) {
                rotate = atan2(rigidBody!!.speed.x, rigidBody!!.speed.y)
            }
        }
    }

    private fun handleHeadPhysics() {
        val point = gameCycle.mapApi.getPointFromPos(position)
        val isTouching = gameCycle.mapApi.tileIsActive(dimension!!, point.x, point.y)

        // Find linked death component to get segments (lazy way to find segments)
        val linkedDeath = getComponent<LinkedDeathComponent>()
        val lastSegment = linkedDeath?.linkedEntities?.lastOrNull()
        
        if (lastSegment?.mainHitbox?.blocksCollision == false) {
            rigidBody?.addForce(0f v -0.02f)
        }

        if (isTouching) {
            rigidBody?.addForce(0f v 0.01f)
            rigidBody?.useFriction()
        }

        // Particle burst on Enter/Exit (Splash effect)
        if (isTouching != wasTouching) {
            val api = gameCycle.mapApi
            // Search for tile to use for particles
            val tile = api.getTileType(dimension!!, point.x, point.y)
                ?: api.getTileType(dimension!!, point.x, point.y + 1)
                ?: api.getTileType(dimension!!, point.x, point.y - 1)
                ?: api.getTileType(dimension!!, point.x + 1, point.y)
                ?: api.getTileType(dimension!!, point.x - 1, point.y)
            
            if (tile != null) {
                val moveDir = rigidBody?.speed ?: (0f v 0f)
                val splashDir = if (isTouching) -moveDir else moveDir
                
                gameCycle.particlesApi.buildTile(dimension!!, tile)
                    .at(position)
                    .count(60) // Even more particles
                    .randomOffset(2.0f) // Wider side spread
                    .speed(splashDir * 0.1f + (0f v 0.2f))
                    .randomSpeed(4.0f) // More chaotic spread
                    .spawn()
            }
            wasTouching = isTouching
        }
    }

    override fun renderUpdate() {
        super.renderUpdate()
        (renderEntity as BossRenderEntity).rotate = rotate
        (renderEntity as BossRenderEntity).type = bossPart
    }

    override fun createCustomHitboxes() {
        super.createCustomHitboxes()
        collisionDetector?.tagFilter = TagFilter.HasAny(listOf(EntityTags.player))
        collisionDetector?.onContactStart = { bodyDamage(it) }
        mainHitbox?.hitboxType = HitboxTypes.ONLY_TRIGGER
    }

    override fun spawnDieParticles() {
        val sprite = when(bossPart) {
            1 -> "xHead.png"
            0 -> "xBody.png"
            -1 -> "xTail.png"
            else -> ""
        }
        if (sprite.isEmpty()) return
        
        repeat(1) {
            val randomDir = Vec2(AppState.main.random(-1f, 1f), AppState.main.random(-1f, 1f)).normalize()
            gameCycle.particlesApi.addParticle(
                dimension!!,
                EntityParticle(
                    gameCycle,
                    coreController.spriteLoader.getValue(sprite),
                    position + randomDir * size / 2f,
                    randomDir * AppState.main.random(0.01f, 0.03f),
                    size / 1f
                )
            )
        }
    }

    // takeDamage is now handled by DamageRedirectComponent for segments, 
    // but the head still needs its logic to apply to first (self)
    override fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean {
        if (isDead || invulnerabilityTicks > 0) return false
        
        if (bossPart == 1) {
            return super.takeDamage(damage, hitboxComponent)
        } else {
            // Segments only notify components (to trigger redirection) 
            // but return false to prevent base class from applying damage again
            sendEvent(EntityEvent.Damage(damage, hitboxComponent))
            return false
        }
    }

    override fun isAnyPhysicBlockCollision() : Boolean = false
}