package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.Ai.BossAi
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.BossRenderEntity
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameController.GameCycle
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
    var next: BossEntity? = null
    var prev: BossEntity? = null
    var last: BossEntity? = null
    var first: BossEntity = this

    var rotate = 0f
    var bossPart = 1
    var bossParts = 15
    var number = 0
    private val segmentLength = 2f
    override var baseBackResistance = 0.2f

    init {
        gravityComponent = null
        hasCollisionDetector = true
        rigidBody?.friction = 0.01f
        baseBackResistance = 0.95f
    }

    override fun spawn() {
        super.spawn()
        var leader = this // Тот, за кем закрепляется новый сегмент
        if (bossPart == 1) {
            this.ai = BossAi(this, gameCycle)
            for (i in 0 until bossParts) {
                val entityApi: EntityApi = gameCycle.entityApi
                val entity = entityApi.getRegisteredEntityByType(this.entityType) as BossEntity

                entity.next = leader   // Сегмент смотрит вперед на лидера
                leader.prev = entity   // Лидер смотрит назад на сегмент (создаем связь prev)
                entity.first = this

                entity.bossPart = 0
                entity.number = i + 1
                if (i == bossParts - 1) {
                    entity.bossPart = -1
                    last = entity
                }

                entityApi.addInSystem(entity, position)
                entity.hpBody = this.hpBody
                entity.renderEntity?.changeLayer(RenderLayers.Main.B5, i * 2)
                entity.hide()
                entity.show()
                entity.hpRender?.hpBody = entity.hpBody!!

                leader = entity // Теперь этот сегмент станет лидером для следующего
            }
        }
    }

    // Добавь эти переменные в начало класса BossEntity
    private var wasTouchingBlocks = false
    private var lastTouchPos = Vec2.ZERO

    override fun physicUpdate() {
        if (bossPart == 1) {
            val point = gameCycle.mapApi.getPointFromPos(position)
            val isTouching = gameCycle.mapApi.tileIsActive(point.x, point.y)

            if (last?.mainHitbox?.blocksCollision == false) {
                rigidBody?.addForce(0f v -0.02f)
            }

            if (isTouching) {
                rigidBody?.addForce(0f v 0.01f)
                rigidBody?.useFriction()
                lastTouchPos = Vec2(position.x, position.y) // используем .set чтобы не плодить объекты
            }

            // Если состояние изменилось (вошел или вышел) — спавним частицы
            if (isTouching != wasTouchingBlocks) {
                spawnParticleBurst(position, 100) // используем текущую позицию для входа/выхода
            }

            wasTouchingBlocks = isTouching
            rotate = atan2(rigidBody!!.speed.x, rigidBody!!.speed.y)
            updateChainPositions()
        }

        super.physicUpdate()
        spawnSingleRandomParticle()
    }

    private fun spawnParticleBurst(pos2: Vec2, count: Int) {
        val map = gameCycle.mapApi


        for(i in 0 until count) {
            var pos = map.getPointFromPos(pos2 + (AppState.main.random(-1f, 1f) v  AppState.main.random(-1f, 1f)))
            val type = map.getTileType(pos.x, pos.y) ?: continue
            val context = map.getTileContext(pos.x, pos.y) ?: continue
            type.spawnTileParticle(pos.x, pos.y, context, gameCycle.mapController, Vec2(AppState.main.random(-0.1f, 0.1f), AppState.main.random(0.05f, 0.1f)).normalize()* AppState.main.random(0.1f, 0.3f))
        }
    }

    private fun spawnSingleRandomParticle() {
        val map = gameCycle.mapApi
        val randPos = position + Vec2(AppState.main.random(-2f, 2f), AppState.main.random(-2f, 2f))

        var pos = map.getPointFromPos(randPos)

        map.getTileType(pos.x, pos.y)?.let { type ->
            val context = map.getTileContext(pos.x, pos.y) ?: return@let
            type.spawnTileParticle(pos.x, pos.y, context, gameCycle.mapController)
        }
    }

    private fun updateChainPositions() {
        val allSegments = mutableListOf<BossEntity>()
        var current: BossEntity? = last
        while (current != null && current != this) {
            allSegments.add(current)
            current = current.next
        }

        for (i in allSegments.indices.reversed()) {
            allSegments[i].correctSegmentPosition()
        }
    }

    private fun correctSegmentPosition() {
        val target = next ?: return
        val dx = position.x - target.position.x
        val dy = position.y - target.position.y
        val distSq = dx * dx + dy * dy

        if (distSq < 0.000001f) return

        val dist = kotlin.math.sqrt(distSq)
        val nx = dx / dist
        val ny = dy / dist

        position.x = target.position.x + nx * segmentLength
        position.y = target.position.y + ny * segmentLength
        rotate = atan2(nx, ny) + PI.toFloat()
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
        repeat(1) {
            var randomDir = Vec2(AppState.main.random(-1f, 1f), AppState.main.random(-1f, 1f)).normalize()
            gameCycle.particleController.particleSystem.addParticle(
                EntityParticle(
                    gameCycle,
                    coreController.spriteLoader.getValue(
                        when(bossPart) {
                            1 -> "xHead.png"
                            0 -> "xBody.png"
                            -1 -> "xTail.png"
                            else ->  ""
                        }
                    ),
                    position + randomDir * size / 2f,
                    randomDir * AppState.main.random(0.01f, 0.03f),
                    size / 1f
                )
            )
        }
    }

    override fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean {
        if (isDead) return false
        if (invulnerabilityTicks > 0) return false
        gameCycle.entityApi.absoluteDamage(first!!, damage)
        return true
    }
    override fun die() {
        super.die()

        // Убиваем все сегменты цепочки
        var current: BossEntity? = first.prev
        while (current != null) {
            val nextSeg = current.prev
            current.isDead = true
            current?.spawnDieParticles()
            gameCycle.entityApi.hideEntity(current)
            gameCycle.entityApi.deleteInSystem(current)
            current = nextSeg
        }

        // Если мы не голова — убиваем голову
        if (first !== this && !first.isDead) {
            first.isDead = true
            current?.spawnDieParticles()
            gameCycle.entityApi.hideEntity(first)
            gameCycle.entityApi.deleteInSystem(first)
        }
    }
}