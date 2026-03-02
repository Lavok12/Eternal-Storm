package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import kotlin.math.abs
import kotlin.math.sign

class RigidBody(entity: Entity) : EntityComponent(entity) {
    var deltaPosition = 0 v 0
    var speed = 0 v 0
    var friction = 0.025f
    var blockFriction = 0.14f

    private val maxStep: Float
        get() = AppState.maxPhysicStep

    fun move(delta: Vec2) {
        deltaPosition = deltaPosition + delta
    }
    fun moveX(delta: Float) {
        deltaPosition.x += delta
    }
    fun moveY(delta: Float) {
        deltaPosition.y += delta
    }
    fun useSpeed() {
        deltaPosition = deltaPosition + speed
    }
    fun useFriction() {
        if (entity.isAnyPhysicBlockCollision()) {
            speed = speed * (1f - blockFriction)
        } else {
            speed = speed * (1f - friction)
        }
    }
    fun containsSteps(): Boolean {
        return abs(deltaPosition.x) > 0.0001f || abs(deltaPosition.y) > 0.0001f
    }

    fun deltaStep() {
        val stepX = if (abs(deltaPosition.x) > maxStep) {
            maxStep * sign(deltaPosition.x)
        } else {
            deltaPosition.x
        }

        val stepY = if (abs(deltaPosition.y) > maxStep) {
            maxStep * sign(deltaPosition.y)
        } else {
            deltaPosition.y
        }

        entity.position.x += stepX
        entity.position.y += stepY

        deltaPosition.x -= stepX
        deltaPosition.y -= stepY
    }
    fun addDeltaPos() {
        entity.position = entity.position + deltaPosition
        deltaPosition = 0 v 0
    }

    fun addForce(force: Vec2) {
        speed = speed + force
    }
}