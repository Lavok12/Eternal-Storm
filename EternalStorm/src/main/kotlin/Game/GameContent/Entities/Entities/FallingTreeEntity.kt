package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import processing.core.PImage
import kotlin.math.*
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.EntityParticle
import la.vok.State.AppState

class FallingTreeSegmentEntity(
    gameCycle: GameCycle,
    val texture: PImage,
    val bgTextures: List<PImage>,
    val topLeafTexture: PImage?,    // только для верхнего сегмента
    val segmentIndex: Int,
    val sharedPivot: Vec2,
    val fallRight: Boolean,
    val treeHeight: Int,
    val siblings: MutableList<FallingTreeSegmentEntity>
) : Entity(AbstractEntityType.EmptyEntityType, gameCycle) {

    private var fallProgress = 0f
    private val fallProgressSpeed = 0.018f
    private val maxFallAngle = PI.toFloat() / 2f
    var fallAngle = 0f
        private set

    private var isLying = false
    private var dropProgress = 0f
    private val dir get() = if (fallRight) 1f else -1f

    private var segWorldPos = Vec2.ZERO

    // Задержка перед началом проверки коллизий — чтобы не сработало сразу
    private var collisionDelay = 10

    init {
        hpBody = null
        hpRender = null
        rigidBody = null
        gravityComponent = null
        hasCollisionDetector = false
        hasDownTrigger = false
    }

    inner class SegmentRender(layer: LayersRenderContainer) : BaseRenderObject(layer) {
        override var layerData = RenderLayerData(RenderLayers.Main.A1, 2)
        override var ROI_pos = Vec2.ZERO
        override var ROI_size = 4 v 4
        override var ROI_delta = 0 v 0

        override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
            val drawAngle = fallAngle * dir * -1f
            val camPos = camera.useCamera(segWorldPos)
            val camSize = camera.useCameraSize(1f v 1f)

            for (bg in bgTextures) {
                lg.setRotateImage(bg, camPos, camSize * 5f, -drawAngle)
            }

            topLeafTexture?.let {
                lg.setRotateImage(it, camPos, camSize * 6f, -drawAngle)
            }

            lg.setRotateImage(texture, camPos, camSize, -drawAngle)
        }
    }

    override fun spawn() {
        size = 1f v 1f
        updateSegWorldPos()
        position = segWorldPos.copy()
        renderEntity = null
        hpRender = null
        val render = SegmentRender(getRenderLayer())
        renderEntity = render
    }


    override fun physicUpdate() {
        physicTicks++
        if (isDead) return

        if (isLying) {
            dropProgress = (dropProgress + 0.008f).coerceAtMost(1f)
            val dropSpeed = sin(dropProgress * PI.toFloat() / 2f) * 0.25f
            sharedPivot.y -= dropSpeed
        } else {
            fallProgress = (fallProgress + fallProgressSpeed).coerceAtMost(1f)
            fallAngle = -sin(fallProgress * PI.toFloat() / 2f) * maxFallAngle
            if (fallProgress >= 1f) {
                fallAngle = -maxFallAngle
                isLying = true
                dropProgress = 0f
            }
        }

        updateSegWorldPos()
        position = segWorldPos.copy()

        if (collisionDelay > 0) collisionDelay-- else checkCollisionManual()
    }

    private fun checkCollisionManual() {
        if (isDead) return
        val mapApi = gameCycle.mapApi

        if (isLying) {
            val sx = segWorldPos.x
            val sy = segWorldPos.y
            if (isSolid(mapApi, floor(sx).toInt(), floor(sy - 0.6f).toInt())) {
                die(); return
            }
        } else {
            val tipX = sharedPivot.x + sin(fallAngle) * dir * (segmentIndex + 1f)
            val tipY = sharedPivot.y + cos(fallAngle) * (segmentIndex + 1f)
            if (isSolid(mapApi, floor(tipX).toInt(), floor(tipY).toInt())) {
                die()
            }
        }
    }

    private fun isSolid(mapApi: la.vok.Game.GameContent.Map.MapApi, x: Int, y: Int): Boolean {
        val tile = mapApi.getTileType(x, y) ?: return false
        return tile.collisionType == la.vok.Game.GameController.CollisionType.FULL
    }

    override fun die() {
        if (isDead) return
        isDead = true

        spawnTextureParticles(texture)

        for (bg in bgTextures) {
            spawnTextureParticles(bg)
        }

        topLeafTexture?.let { spawnTextureParticles(it) }

        gameCycle.entityApi.killInSystem(this)
    }

    private fun spawnTextureParticles(tex: PImage) {
        repeat(5) {
            val randomDir = Vec2(
                AppState.main.random(-1f, 1f),
                AppState.main.random(0f, 1f)   // больше вверх
            ).normalize()
            val speed = randomDir * AppState.main.random(0.02f, 0.06f)

            gameCycle.particleController.particleSystem.addParticle(
                EntityParticle(
                    gameCycle,
                    tex,
                    segWorldPos + Vec2(
                        AppState.main.random(-0.3f, 0.3f),
                        AppState.main.random(-0.3f, 0.3f)
                    ),
                    speed,
                    Vec2(AppState.main.random(0.3f, 0.7f), AppState.main.random(0.3f, 0.7f))
                )
            )
        }
    }

    private fun updateSegWorldPos() {
        val segOffset = segmentIndex + 0.5f
        segWorldPos = Vec2(
            sharedPivot.x + sin(fallAngle) * dir * segOffset,
            sharedPivot.y + cos(fallAngle) * segOffset
        )
    }

    private fun killAll() {
        siblings.forEach { seg ->
            if (!seg.isDead) {
                seg.isDead = true
                gameCycle.entityApi.killInSystem(seg)
            }
        }
    }

    override fun renderUpdate() {
        renderFrames++
        renderEntity?.let {
            it.ROI_pos = segWorldPos.copy()
            it.ROI_size = 4f v 4f
        }
        updateRenderHitboxes()
    }

    override fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean = false
}