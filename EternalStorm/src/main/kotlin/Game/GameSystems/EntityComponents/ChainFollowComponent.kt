package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.PI
import kotlin.math.atan2

/**
 * Component that makes the entity follow another entity at a fixed distance,
 * maintaining a specific segment length.
 */
class ChainFollowComponent(
    entity: Entity, 
    var leader: Entity, 
    var segmentLength: Float
) : EntityComponent(entity) {

    var rotation = 0f

    override fun onPhysicUpdate() {
        val targetPos = leader.position
        val dx = entity.position.x - targetPos.x
        val dy = entity.position.y - targetPos.y
        val distSq = dx * dx + dy * dy

        if (distSq < 0.000001f) return

        val dist = kotlin.math.sqrt(distSq)
        val nx = dx / dist
        val ny = dy / dist

        entity.position.x = targetPos.x + nx * segmentLength
        entity.position.y = targetPos.y + ny * segmentLength
        
        rotation = atan2(nx, ny) + PI.toFloat()
    }
}
