package la.vok.Game.GameContent.Entities.Entities.Projectiles

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.VfxObjects.SinWave.SinWaveVFXWaveTrail
import la.vok.Game.GameSystems.EntityComponents.ContactDamageComponent
import la.vok.Game.GameSystems.EntityComponents.GravityComponent
import la.vok.Game.GameSystems.EntityComponents.LifetimeComponent
import la.vok.LavokLibrary.LGraphics.LBlendMode
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import processing.core.PImage
import kotlin.math.cos
import kotlin.math.sin

open class SinWave(
    gameCycle: GameCycle,
    damage: Int,
    source: Long,
) : ProjectileEntity(gameCycle, damage, source) {
    override var moveOverBlocks = true
    override var deleteProjectileOnContact: Boolean = true

    companion object {
        private const val WAVE_AMPLITUDE = 0.5f
    }

    override var renderEntity: RenderObjectInterface? = object : BaseRenderEntity(getRenderLayer()) {
        override var layerData: RenderLayerData = RenderLayerData(RenderLayers.Main.A3, 1)

        override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
            val body = rigidBody ?: return
            val lifetime = getComponent<LifetimeComponent>() ?: return
            val ticks = lifetime.ticks.toFloat()

            val moveDirection = body.speed.normalized()
            val perpendicularDir = moveDirection.perpendiculared()

            val colorPhase = ticks * 0.1f
            val pi23 = Math.PI.toFloat() * 2f / 3f

            val worldPos = camera.toWorldPos(pos)
            val worldSize = camera.toWorldSize(size)
            val waveOffset = sin(ticks * 0.25f) * WAVE_AMPLITUDE
            val finalWorldPos = worldPos + perpendicularDir * waveOffset

            val prevWaveOffset = sin((ticks - 1f) * 0.25f) * WAVE_AMPLITUDE
            val prevWorldPos = (worldPos - body.speed) + perpendicularDir * prevWaveOffset

            val finalScreenPos = camera.useCamera(finalWorldPos)
            val texture = gameController.coreController.spriteLoader.getValue("glow.png")

            val rotationSpeed = ticks * 0.1f
            val prevRotationSpeed = (ticks - 1f) * 0.1f

            val worldCoreRadius = worldSize.x * 1.5f
            val worldCoreBallSize = worldSize * 1.2f
            val coreOrbitRadius = camera.useCameraSize(Vec2(worldCoreRadius, 0f)).x
            val coreBallSize = camera.useCameraSize(worldCoreBallSize)
            val currentDimension = dimension
            val coreAngleStep = (Math.PI * 2f / 3f).toFloat()

            for (i in 0 until 3) {
                val baseAngle = coreAngleStep * i
                val startAngle = -rotationSpeed + baseAngle
                val endAngle = -prevRotationSpeed + baseAngle

                for (step in 0..2) {
                    val t = step / 2f
                    val interpPos = prevWorldPos * (1f - t) + finalWorldPos * t
                    val interpAngle = endAngle * (1f - t) + startAngle * t
                    val trailPos = interpPos + Vec2(cos(interpAngle), sin(interpAngle)) * worldCoreRadius

                    val tr = sin(colorPhase + i * pi23) * 100f + 155f
                    val tg = sin(colorPhase + i * pi23 + 1.5f) * 100f + 155f
                    val tb = sin(colorPhase + i * pi23 + 3.0f) * 100f + 155f

                    gameCycle.vfxObjectsApi.spawn(currentDimension, trailPos, worldCoreBallSize * 0.25f, Vec2.ZERO, vfxFactory = {
                        SinWaveVFXWaveTrail(gameCycle).apply {
                            setupColor(tr, tg, tb, 230f)
                            initialSize = worldCoreBallSize * 0.25f
                            this.lifetime = 4L
                        }
                    })
                }
            }

            lg.setBlendMode(LBlendMode.ADD)

            // Усиленная пульсация гало (амплитуда увеличена с 0.2/0.3 до 0.5/0.6)
            val pulse1 = sin(ticks * 0.15f) * 0.5f + 1.0f
            val pulse2 = cos(ticks * 0.2f) * 0.6f + 1.0f
            val haloSize1 = camera.useCameraSize(Vec2(worldCoreRadius * 4.0f * pulse1, 0f)).x
            val haloSize2 = camera.useCameraSize(Vec2(worldCoreRadius * 6.0f * pulse2, 0f)).x

            lg.setTint(70f, 110f, 150f, 120f)
            lg.setImage(texture, finalScreenPos, Vec2(haloSize1, haloSize1))
            lg.setTint(150f, 60f, 90f, 80f)
            lg.setImage(texture, finalScreenPos, Vec2(haloSize2, haloSize2))

            for (i in 0 until 3) {
                val angle = -rotationSpeed + coreAngleStep * i
                // Очень сильная пульсация отдельных шаров
                val pPulse = sin(ticks * 0.4f + i) * 0.6f + 1.2f

                val baseHue = i * pi23
                val cr = sin(colorPhase + baseHue) * 100f + 155f
                val cg = sin(colorPhase + baseHue + 2f) * 100f + 155f
                val cb = sin(colorPhase + baseHue + 4f) * 100f + 155f

                renderOrbitBall(lg, texture, finalScreenPos, angle, coreOrbitRadius, coreBallSize * pPulse, cr, cg, cb)
            }

            lg.resetBlendMode()
            lg.noTint()
        }

        private fun renderOrbitBall(lg: LGraphics, tex: PImage, center: Vec2, angle: Float, radius: Float, size: Vec2, r: Float, g: Float, b: Float) {
            val pos = center + Vec2(cos(angle), sin(angle)) * radius
            lg.setTint(r * 0.5f, g * 0.5f, b * 0.5f, 150f)
            lg.setImage(tex, pos, size * 1.4f)
            lg.setTint(r * 1.5f, g * 1.5f, b * 1.5f, 255f)
            lg.setImage(tex, pos, size)
            lg.setTint(255f, 255f, 255f, 255f)
            lg.setImage(tex, pos, size * 0.4f)
        }
    }

    init {
        getComponent<GravityComponent>()?.scale = 0f
    }

    override fun createCustomHitboxes() {
        super.createCustomHitboxes()
        mainHitbox!!.size = mainHitbox!!.size * 4f
    }
}