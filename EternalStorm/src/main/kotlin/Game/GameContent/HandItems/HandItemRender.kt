package la.vok.Game.GameContent.HandItems

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.FrameLimiter
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.BaseRenderObject
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderLayerData
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.ClientContent.RenderSystem.RenderLayers.RenderLayers
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import kotlin.math.cos
import kotlin.math.sin

open class HandItemRender(
    var handItem: HandItem,
    layersRenderContainer: LayersRenderContainer
) : BaseRenderObject(layersRenderContainer) {

    private val desc get() = handItem.descriptor

    override var layerData: RenderLayerData = RenderLayerData(
        RenderLayers.Main.A3,
        desc.renderLayer
    )

    override var ROI_pos = 0 v 0
    override var ROI_size = 1 v 1
    override var ROI_delta = 0 v 0

    open var facing = 1
    open var useStage = 0f

    var lockedAimAngle: Float = 0f

    val progress: Float get() = useStage / desc.useDuration

    private val sprite get() = AppState.coreController.spriteLoader.getValue(desc.spriteName)
    private val visualHandPos get() = handItem.handItemComponent.getVisualHandPos()
    private val handPos get() = handItem.handItemComponent.getHandPos()

    private val isFlipped get() = facing < 0

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
        when (val anim = desc.animationType) {
            is AnimationType.Swing             -> drawSwing(lg, anim, camera)
            is AnimationType.Thrust            -> drawThrust(lg, anim, camera)
            is AnimationType.Spear             -> drawSpear(lg, anim, camera)
            is AnimationType.DirectionalThrust -> drawDirectionalThrust(lg, anim, camera)
            is AnimationType.Idle              -> drawIdle(lg, camera)
            is AnimationType.Custom            -> anim.draw(this, lg, pos, size, camera)
        }
    }

    private fun directionalAngle(flipped: Boolean): Float {
        return if (flipped) {
            -(Math.PI.toFloat() + lockedAimAngle)
        } else {
            -lockedAimAngle
        }
    }

    private fun drawSwing(lg: LGraphics, anim: AnimationType.Swing, camera: Camera) {
        val swingProgress = if (progress < 0.6f) {
            easeOut(progress / 0.6f)
        } else {
            easeIn(1f - (progress - 0.6f) / 0.4f)
        }

        val angle = (anim.startAngle + swingProgress * anim.swingAngle) * facing + (desc.spriteAngle * facing)

        val peakScale = if (progress in 0.45f..0.65f) {
            1f + anim.peakScale * sin((progress - 0.45f) / 0.2f * Math.PI.toFloat())
        } else 1f
        val scaledSize = desc.spriteSize * peakScale

        val lunge = swingProgress * anim.lungeDistance * facing
        val swingHandPos = handPos + Vec2(lunge, 0f)

        val shake = if (progress > 0.62f) {
            val t = (progress - 0.62f) / 0.38f
            sin(t * Math.PI.toFloat() * 3f) * anim.shakeAmplitude * (1f - t)
        } else 0f

        lg.setRotateImageAround(
            sprite,
            camera.useCamera(swingHandPos + desc.renderDelta + (scaledSize * 0.5f) + Vec2(shake, shake * 0.5f)),
            camera.useCameraSize(scaledSize),
            angle,
            camera.useCamera(swingHandPos),
            isFlipped
        )
    }

    private fun drawThrust(lg: LGraphics, anim: AnimationType.Thrust, camera: Camera) {
        val thrustProgress = if (progress < 0.5f) {
            easeOut(progress / 0.5f)
        } else {
            easeIn(1f - (progress - 0.5f) / 0.5f)
        }

        val offset = (anim.startOffset + thrustProgress * (anim.maxOffset - anim.startOffset)) * facing
        val worldPos = handPos + (desc.renderDelta * (facing v 1f)) + Vec2(offset, 0f)

        val peakScale = if (progress in 0.4f..0.6f) {
            1f + anim.peakScale * sin((progress - 0.4f) / 0.2f * Math.PI.toFloat())
        } else 1f
        val scaledSize = desc.spriteSize * peakScale

        val shake = if (progress > 0.52f) {
            val t = (progress - 0.52f) / 0.48f
            sin(t * Math.PI.toFloat() * 4f) * anim.shakeAmplitude * (1f - t)
        } else 0f

        lg.setRotateImageAround(
            sprite,
            camera.useCamera(worldPos + Vec2(0f, shake)),
            camera.useCameraSize(scaledSize),
            desc.spriteAngle * facing,
            camera.useCamera(worldPos),
            isFlipped
        )
    }

    private fun drawSpear(lg: LGraphics, anim: AnimationType.Spear, camera: Camera) {
        val cursorX = handItem.handItemComponent.targetScreenPos().x
        val entityX = handItem.entity.position.x
        val flipped = cursorX < entityX

        val thrustProgress = if (progress < 0.5f) {
            easeOut(progress / 0.5f)
        } else {
            easeIn(1f - (progress - 0.5f) / 0.5f)
        }

        val offsetDist = anim.startOffset + thrustProgress * (anim.maxOffset - anim.startOffset)
        val offsetVec = Vec2(
            offsetDist * cos(lockedAimAngle),
            offsetDist * sin(lockedAimAngle)
        )
        val worldPos = handPos + offsetVec

        val peakScale = if (progress in 0.4f..0.6f) {
            1f + anim.peakScale * sin((progress - 0.4f) / 0.2f * Math.PI.toFloat())
        } else 1f
        val scaledSize = desc.spriteSize * peakScale

        val shake = if (progress > 0.52f) {
            val t = (progress - 0.52f) / 0.48f
            sin(t * Math.PI.toFloat() * 4f) * anim.shakeAmplitude * (1f - t)
        } else 0f

        val drawAngle = directionalAngle(flipped) + anim.restAngleOffset

        lg.setRotateImageAround(
            sprite,
            camera.useCamera(worldPos + Vec2(0f, shake)),
            camera.useCameraSize(scaledSize),
            drawAngle,
            camera.useCamera(worldPos),
            flipped
        )
    }

    private fun drawDirectionalThrust(lg: LGraphics, anim: AnimationType.DirectionalThrust, camera: Camera) {
        val cursorX = handItem.handItemComponent.targetWorldPos().x
        val entityX = handItem.entity.position.x
        val flipped = cursorX < entityX

        val thrustProgress = if (progress < 0.5f) {
            easeOut(progress / 0.5f)
        } else {
            easeIn(1f - (progress - 0.5f) / 0.5f)
        }

        val offsetDist = anim.startOffset + thrustProgress * (anim.maxOffset - anim.startOffset)
        val offsetVec = Vec2(
            offsetDist * cos(lockedAimAngle),
            offsetDist * sin(lockedAimAngle)
        )
        val worldPos = handPos + desc.renderDelta + offsetVec

        val peakScale = if (progress in 0.4f..0.6f) {
            1f + anim.peakScale * sin((progress - 0.4f) / 0.2f * Math.PI.toFloat())
        } else 1f
        val scaledSize = desc.spriteSize * peakScale

        val shake = if (progress > 0.52f) {
            val t = (progress - 0.52f) / 0.48f
            sin(t * Math.PI.toFloat() * 4f) * anim.shakeAmplitude * (1f - t)
        } else 0f

        val drawAngle = directionalAngle(flipped) + desc.spriteAngle

        lg.setRotateImageAround(
            sprite,
            camera.useCamera(worldPos + Vec2(0f, shake)),
            camera.useCameraSize(scaledSize),
            drawAngle,
            camera.useCamera(worldPos),
            flipped
        )
    }

    private fun drawDirectionalIdle(lg: LGraphics, anim: AnimationType.DirectionalThrust, camera: Camera) {
        val cursorX = handItem.handItemComponent.targetScreenPos().x
        val entityX = handItem.entity.position.x
        val flipped = cursorX < entityX

        val worldPos = handPos + desc.renderDelta
        val drawAngle = directionalAngle(flipped) + desc.spriteAngle

        lg.setRotateImageAround(
            sprite,
            camera.useCamera(worldPos),
            camera.useCameraSize(desc.spriteSize),
            drawAngle,
            camera.useCamera(worldPos),
            flipped
        )
    }

    private fun drawIdle(lg: LGraphics, camera: Camera) {
        val worldPos = handPos + (desc.renderDelta * (facing v 1f))
        val bob = sin(FrameLimiter.totalPhysicsFrames * 0.05f) * 0.03f

        lg.setRotateImageAround(
            sprite,
            camera.useCamera(worldPos + Vec2(0f, bob)),
            camera.useCameraSize(desc.spriteSize),
            desc.spriteAngle * facing,
            camera.useCamera(worldPos),
            isFlipped
        )
    }

    private fun easeOut(t: Float): Float = 1f - (1f - t) * (1f - t)
    private fun easeIn(t: Float): Float = t * t
}
