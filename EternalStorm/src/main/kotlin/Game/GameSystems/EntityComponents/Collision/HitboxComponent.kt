package la.vok.Game.GameSystems.EntityComponents.Collision

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Entities.EntityRender.HitboxRender
import la.vok.Game.GameSystems.EntityComponents.EntityComponent
import la.vok.Game.GameSystems.EntityComponents.RigidBody
import la.vok.LavokLibrary.Geometry.FrameRect
import la.vok.LavokLibrary.KotlinPlus.forEachOnBorder
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class HitboxComponent(var hitboxType: HitboxTypes, entity: Entity, var rigidBody: RigidBody? = null) : EntityComponent(entity), FrameRect {
    var size = 1 v 1
    var delta = 0 v 0
    var hitboxRender = HitboxRender(this, entity.getEntityRenderContainer())

    var ignoreCollision = false

    var blocksCollision = false

    private val skinWidth = 0.05f

    fun resetBlockCollision() {
        blocksCollision = false
    }
    fun hitboxUpdate() {

        if (hitboxType == HitboxTypes.ONLY_TRIGGER) {
            checkTrigger()
            return
        }
        if (hitboxType == HitboxTypes.COLLISION) {
            if (rigidBody == null) {
                AppState.logger.error("RigidBody Is Null")
            }
            checkAxisCollision(horizontalSearch = false)
            checkAxisCollision(horizontalSearch = true)
        }
    }

    private fun checkAxisCollision(horizontalSearch: Boolean) {
        val shrink = if (horizontalSearch) (0 v skinWidth) else (skinWidth v 0)

        val p1 = mapApi.getPointFromPos(frameLeftTop + shrink)
        val p2 = mapApi.getPointFromPos(frameRightBottom - shrink)

        forEachOnBorder(p1, p2) { x, y, sideX, sideY ->
            val tile = mapApi.getTileFromMap(x, y)
            if (tile == null) return@forEachOnBorder
            if (horizontalSearch && sideX == 0) return@forEachOnBorder
            if (!horizontalSearch && sideY == 0) return@forEachOnBorder

            val isMovingTowardsX = sideX != 0 && (rigidBody!!.speed.x * sideX > 0 || rigidBody!!.deltaPosition.x * sideX > 0)
            val isMovingTowardsY = sideY != 0 && (rigidBody!!.speed.y * sideY > 0 || rigidBody!!.deltaPosition.y * sideY > 0)

            if (isMovingTowardsX || isMovingTowardsY) {
                blocksCollision = true
                resolveCollision(if (horizontalSearch) sideX else 0, if (!horizontalSearch) sideY else 0)
            }
        }
    }

    private fun checkTrigger() {
        val p1 = mapApi.getPointFromPos(frameLeftTop)
        val p2 = mapApi.getPointFromPos(frameRightBottom)
        forEachOnBorder(p1, p2) { x, y, _, _ ->
            if (mapApi.getTileFromMap(x, y) != null) {
                blocksCollision = true
            }
        }
    }

    private fun resolveCollision(sideX: Int, sideY: Int) {
        if (sideX != 0) {
            rigidBody!!.speed.x = 0f
            rigidBody!!.deltaPosition.x = 0f
        }
        if (sideY != 0) {
            rigidBody!!.speed.y = 0f
            rigidBody!!.deltaPosition.y = 0f
        }
    }

    fun renderUpdate() {
        if (hitboxRender.isShow) hitboxRender.update()
    }

    override val frameLeftTop: Vec2
        get() = entity.position + delta - size / 2f

    override val frameRightBottom: Vec2
        get() = entity.position + delta + size / 2f
}