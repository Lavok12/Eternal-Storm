package la.vok.Game.GameContent.Entities.Ai

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import kotlin.random.Random

class TumbleweedAI(entity: Entity, gameCycle: GameCycle) : AbstractAI(entity, gameCycle) {
    private var rollForce = 0.03f
    private var direction = 1
    
    private var changeDirTimer = 0
    private var jumpTimer = 0

    init { 
        resetDirectionTimer()
        resetJumpTimer()
    }

    private fun resetDirectionTimer() {
        changeDirTimer = Random.nextInt(120, 300) // 2-5 seconds
    }

    private fun resetJumpTimer() {
        jumpTimer = Random.nextInt(60, 240) // 1-4 seconds
    }

    override fun physicUpdate() {
        // Apply continuous rolling force
        entity.rigidBody?.addForce(rollForce * 0.3f * direction v 0f)
        
        changeDirTimer--
        if (changeDirTimer <= 0) {
            resetDirectionTimer()
            direction = if (Random.nextBoolean()) 1 else -1
            rollForce = Random.nextFloat() * 0.08f + 0.04f
        }

        jumpTimer--
        if (jumpTimer <= 0) {
            resetJumpTimer()
            // Only jump if touching a physical block
            if (entity.isAnyPhysicBlockCollision()) {
                val jumpHeight = Random.nextFloat() * 0.3f + 0.25f
                entity.rigidBody?.addForce(-rollForce * direction * 0.2f v jumpHeight)
            }
        }
    }

    override fun targetScreenPos(): Vec2 = worldToScreen(targetWorldPos())
    override fun targetWorldPos(): Vec2 = entity.position
    override fun targetMapPos(): LPoint = worldToMap(targetWorldPos())
}
