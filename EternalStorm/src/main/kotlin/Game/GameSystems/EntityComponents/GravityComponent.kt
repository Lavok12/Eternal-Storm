package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.EntityRender.HitboxRender
import la.vok.LavokLibrary.Vectors.v

class GravityComponent(entity: Entity, var rigidBody: RigidBody, var scale: Float = -0.01f) : EntityComponent(entity) {
    fun useGravity() {
        if (scale == 0f) return
        rigidBody.speed = rigidBody.speed + (0 v scale)
    }
}