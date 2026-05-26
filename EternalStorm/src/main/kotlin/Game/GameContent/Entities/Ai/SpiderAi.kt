package la.vok.Game.GameContent.Entities.Ai

import la.vok.Game.GameContent.Entities.Entities.SpiderBossEntity
import la.vok.Game.GameContent.Entities.Entities.SpiderLegEntity
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameSystems.WorldSystems.Entities.toTagFilter
import la.vok.LavokLibrary.Vectors.LPoint
import kotlin.math.*

class SpiderAi(val spider: SpiderBossEntity, gameCycle: GameCycle) : AbstractAI(spider, gameCycle) {
    
    private var moveCooldown = 0
    private var activeLegIndex = 0
    private var gaitTimer = 0
    private val maxReach = 16f 
    private val minStepDist = 7f

    override fun physicUpdate() {
        super.physicUpdate()
        
        if (moveCooldown > 0) {
            moveCooldown--
            return
        }

        val aliveLegs = spider.legs.filter { !it.isDead }
        if (aliveLegs.isEmpty()) return

        // Sequential rhythmic loop
        val leg = aliveLegs[activeLegIndex % aliveLegs.size]
        gaitTimer++

        if (shouldMoveLeg(leg) || gaitTimer > 60) {
            if (tryMoveLeg(leg)) {
                activeLegIndex++
                moveCooldown = 8 // Rhythmic pause
                gaitTimer = 0
            } else {
                activeLegIndex++
                moveCooldown = 2
            }
        }
    }

    private fun shouldMoveLeg(leg: SpiderLegEntity): Boolean {
        val shoulderPos = spider.position + leg.shoulderOffset
        val dist = (leg.anchorPoint - shoulderPos).length()
        
        if (dist > maxReach * 0.9f) return true
        if (dist < 3f) return true
        
        // Reposition if behind
        val target = targetWorldPos()
        val moveDir = (target - spider.position).normalized()
        val toAnchor = (leg.anchorPoint - spider.position).normalized()
        if (moveDir.dot(toAnchor) < -0.3f && (target - spider.position).length() > 6f) return true

        return false
    }

    private fun tryMoveLeg(leg: SpiderLegEntity): Boolean {
        val target = targetWorldPos()
        val shoulderPos = spider.position + leg.shoulderOffset
        val targetDir = (target - shoulderPos).normalized()
        val baseAngle = (leg.legIndex.toFloat() / 6f) * 2f * PI.toFloat()
        
        // Sector check: leg should stay within its 60-degree slice relative to the body's center
        // to prevent clumping and clashing.
        fun isPointInSector(p: Vec2): Boolean {
            val rel = (p - spider.position).normalized()
            val angle = rel.angle()
            var diff = abs(angle - baseAngle)
            if (diff > PI) diff = 2f * PI.toFloat() - diff
            return diff < (PI.toFloat() / 3f) // ~60 degrees
        }

        fun isPointGood(p: Vec2): Boolean {
            // Spacing from others
            for (other in spider.legs) {
                if (other == leg) continue
                if ((p - other.anchorPoint).length() < 9f) return false
            }
            // Length safety (Anti-Glitch)
            if ((p - shoulderPos).length() < 5f) return false
            
            // Preferred sector
            if (!isPointInSector(p)) return false
            
            return true
        }

        // 1. Preferred Search (Towards target + In Sector)
        for (dist in (maxReach.toInt() downTo minStepDist.toInt())) {
            for (angleOffset in arrayOf(0f, 0.4f, -0.4f, 0.8f, -0.8f, 1.2f, -1.2f)) {
                val angle = targetDir.angle() + angleOffset
                val checkPos = shoulderPos + Vec2(cos(angle), sin(angle)) * dist.toFloat()
                val p = gameCycle.mapApi.getPointFromPos(checkPos)
                
                if (gameCycle.mapApi.tileIsActive(spider.dimension!!, p.x, p.y) || gameCycle.mapApi.wallIsActive(spider.dimension!!, p.x, p.y)) {
                    val worldP = Vec2(p.x.toFloat(), p.y.toFloat()) + (0.5f v 0.5f)
                    if (isPointGood(worldP)) {
                        leg.moveTo(worldP)
                        return true
                    }
                }
            }
        }

        // 2. Relaxed Search (Ignore target direction, only Sector + Environment)
        for (dist in (maxReach.toInt() downTo minStepDist.toInt())) {
            for (angleOffset in arrayOf(0f, 0.6f, -0.6f, 1.2f, -1.2f, 2.0f, -2.0f)) {
                val angle = baseAngle + angleOffset
                val checkPos = shoulderPos + Vec2(cos(angle), sin(angle)) * dist.toFloat()
                val p = gameCycle.mapApi.getPointFromPos(checkPos)
                if (gameCycle.mapApi.tileIsActive(spider.dimension!!, p.x, p.y) || gameCycle.mapApi.wallIsActive(spider.dimension!!, p.x, p.y)) {
                    val worldP = Vec2(p.x.toFloat(), p.y.toFloat()) + (0.5f v 0.5f)
                    if (isPointGood(worldP)) {
                        leg.moveTo(worldP)
                        return true
                    }
                }
            }
        }

        // 3. Last Resort: Downward
        for (dist in (maxReach.toInt() downTo 6)) {
            val checkPos = shoulderPos + (0f v dist.toFloat())
            val p = gameCycle.mapApi.getPointFromPos(checkPos)
            if (gameCycle.mapApi.tileIsActive(spider.dimension!!, p.x, p.y) || gameCycle.mapApi.wallIsActive(spider.dimension!!, p.x, p.y)) {
                val worldP = Vec2(p.x.toFloat(), p.y.toFloat()) + (0.5f v 0.5f)
                leg.moveTo(worldP) // Move even if sector is bad to stay grounded
                return true
            }
        }

        // 4. Air Fallback
        val airTarget = spider.position + Vec2(cos(baseAngle), sin(baseAngle)) * (maxReach * 0.7f)
        leg.moveToAir(airTarget)
        return true
    }

    override fun targetScreenPos(): Vec2 = worldToScreen(targetWorldPos())
    override fun targetMapPos(): la.vok.LavokLibrary.Vectors.LPoint = worldToMap(targetWorldPos())

    fun getTarget(): la.vok.Game.GameContent.Entities.Entities.Special.Entity? {
        return gameCycle.entityApi.getNearestEntity(
            spider.dimension!!, 
            spider.position, 
            600f, 
            EntityTags.player.toTagFilter()
        )
    }

    override fun targetWorldPos(): Vec2 {
        val player = getTarget()
        return player?.position ?: (spider.position + Vec2(0f, 40f))
    }
}
