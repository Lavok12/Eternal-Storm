package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import kotlin.math.atan2

/**
 * Component that automatically rotates the entity's facing or rotation based on its velocity.
 */
class AutoRotationComponent(entity: Entity) : EntityComponent(entity) {
    override fun onPhysicUpdate() {
        val speed = entity.rigidBody?.speed ?: return
        if (speed.length() > 0.01f) {
            // Update facing if needed, or some rotation property
            if (speed.x > 0) entity.facing = 1 else if (speed.x < 0) entity.facing = -1
        }
    }
}
