package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity

/**
 * Component that adds physics processing (movement, friction) to an entity.
 */
class PhysicsComponent(entity: Entity, val rigidBody: RigidBody) : EntityComponent(entity) {
    var hasCollisionDetector = true

    override fun onPhysicUpdate() {
        entity.updateHitboxes()
        rigidBody.useSpeed()
        rigidBody.useFriction()
        if (hasCollisionDetector) {
            entity.collisionDetector?.update()
        }
        
        do { 
            entity.moveStep(hasCollisionDetector)
        } while (rigidBody.containsSteps())
    }
}
