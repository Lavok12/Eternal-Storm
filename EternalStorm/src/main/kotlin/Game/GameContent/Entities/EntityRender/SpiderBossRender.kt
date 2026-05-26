package la.vok.Game.GameContent.Entities.EntityRender

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Game.GameContent.Entities.Entities.SpiderBossEntity
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import kotlin.math.*

class SpiderBossRender(layer: LayersRenderContainer, val spider: SpiderBossEntity) : BaseRenderEntity(layer) {

    override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: la.vok.Core.CoreContent.Camera.Camera) {
        // Draw Legs FIRST
        spider.legs.filter { !it.isDead }.forEach { leg ->
            drawLeg(lg, leg, pos, camera) 
        }

        // Draw Body
        lg.fill(20f, 22f, 25f)
        lg.stroke(255f, 40f)
        lg.strokeWeight(0.15f)
        lg.setEps(pos, size)
        
        // Draw Eyes
        val eyeOffset = size.x / 4f
        lg.fill(255f, 20f, 20f)
        lg.setEps(pos + ((-0.6f v 0.4f) * eyeOffset), size.x/10f v size.x/10f)
        lg.setEps(pos + ((0.6f v 0.4f) * eyeOffset), size.x/10f v size.x/10f)
    }

    private fun drawLeg(lg: LGraphics, leg: la.vok.Game.GameContent.Entities.Entities.SpiderLegEntity, bodyPosOnScreen: Vec2, camera: la.vok.Core.CoreContent.Camera.Camera) {
        // Essential conversion: Body center on screen + offset translated to screen scale
        val shoulderPos = bodyPosOnScreen + camera.useCameraSize(leg.shoulderOffset)
        
        // Foot position: Directly convert its world visual anchor to screen
        val footPos = camera.useCamera(leg.visualAnchorPoint)
        
        // Safety check for uninitialized world positions
        if (leg.visualAnchorPoint.length() < 1f) return

        val L1 = camera.useCameraSize(8.0f) // Shortened segments
        val L2 = camera.useCameraSize(8.0f) 
        
        val rawDiff = footPos - shoulderPos
        val rawDist = rawDiff.length()
        // If overstretched, clamp foot along direction so IK still works (leg just looks extended)
        val maxDist = L1 + L2
        val diff = if (rawDist > maxDist && rawDist > 0.001f) rawDiff.normalized() * maxDist else rawDiff
        val dist = diff.length().coerceAtLeast(0.001f)

        // IK Angle (law of cosines)
        val cosAlpha = (L1 * L1 + dist * dist - L2 * L2) / (2 * L1 * dist)
        val alpha = if (cosAlpha.isNaN()) 0f else Math.acos(cosAlpha.toDouble()).toFloat()
        
        val baseAngle = diff.angle()
        val kneeAngle = baseAngle + alpha * (if (leg.shoulderOffset.x > 0) 1f else -1f)
        val kneePos = shoulderPos + Vec2(cos(kneeAngle), sin(kneeAngle)) * L1
        
        // Final screen-space drawing
        lg.stroke(120f, 130f, 150f)
        lg.strokeWeight(camera.useCameraSize(0.6f)) // Wider, camera-aware
        lg.setLine(shoulderPos, kneePos)
        lg.stroke(80f, 90f, 110f)
        lg.setLine(kneePos, footPos)
        
        lg.noStroke()
        lg.fill(60f, 70f, 90f)
        lg.setEps(shoulderPos, camera.useCameraSize(2f) v camera.useCameraSize(2f))
        lg.setEps(kneePos, camera.useCameraSize(1.5f) v camera.useCameraSize(1.5f))
        lg.fill(40f, 45f, 55f)
        lg.setEps(footPos, camera.useCameraSize(2.5f) v camera.useCameraSize(1.5f))
    }
}
