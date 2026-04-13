package la.vok.Game.GameSystems.EntityComponents.Collision

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.HitboxRender
import la.vok.Game.GameSystems.EntityComponents.EntityComponent
import la.vok.Game.GameSystems.EntityComponents.RigidBody
import la.vok.Game.GameController.CollisionType
import la.vok.LavokLibrary.Geometry.FrameRect
import la.vok.LavokLibrary.KotlinPlus.forEachOnBorder
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class HitboxComponent(var hitboxType: HitboxTypes, entity: Entity, var rigidBody: RigidBody? = null) : EntityComponent(entity), FrameRect {
    var size = 1 v 1
    var delta = Vec2.ZERO

    var hitboxRender: HitboxRender? = null

    fun initRender() {
        hitboxRender = HitboxRender(this, entity.getRenderLayer())
    }

    var ignoreCollision = false
    var blocksCollision = false

    private val skinWidth = 0.05f
    private val triggerMargin = 0.0

    fun resetBlockCollision() {
        blocksCollision = false
    }

    fun hitboxUpdate() {
        when (hitboxType) {
            HitboxTypes.ONLY_TRIGGER -> checkTrigger()
            HitboxTypes.COLLISION -> {
                if (rigidBody == null) {
                    AppState.logger.error("RigidBody Is Null")
                    return
                }
                checkAxisCollision(horizontalSearch = false) // Y
                checkAxisCollision(horizontalSearch = true)  // X
            }
        }
    }

    private fun isSolid(x: Int, y: Int): Boolean {
        val tile = mapApi.getTileType(entity.dimension!!, x, y) ?: return false
        return tile.collisionType == CollisionType.FULL
    }

    private fun checkAxisCollision(horizontalSearch: Boolean) {
        val rb = rigidBody!!
        val shrink = if (horizontalSearch) (0f v skinWidth) else (skinWidth v 0f)

        val p1 = mapApi.getPointFromPos(frameLeftTop + shrink)
        val p2 = mapApi.getPointFromPos(frameRightBottom - shrink)

        for (x in p1.x..p2.x) {
            for (y in p1.y..p2.y) {
                if (!isSolid(x, y)) continue

                val tileCenter = mapApi.getBlockPos(x, y)
                val tileSize = mapApi.getBlockSize()
                val hitboxCenter = entity.position + delta

                if (horizontalSearch) {
                    val sideX = if (tileCenter.x > hitboxCenter.x) 1 else -1
                    val movingTowards = rb.speed.x * sideX > 0 || rb.deltaPosition.x * sideX > 0
                    val penetration = (size.x / 2f + tileSize.x / 2f) - Math.abs(hitboxCenter.x - tileCenter.x)

                    if (penetration > -triggerMargin) blocksCollision = true

                    if (penetration > 0f && (movingTowards || penetration > skinWidth)) {
                        rb.speed.x = 0f
                        rb.deltaPosition.x = 0f
                        entity.position.x -= sideX * penetration
                        return
                    }
                } else {
                    val sideY = if (tileCenter.y > hitboxCenter.y) 1 else -1
                    val movingTowards = rb.speed.y * sideY > 0 || rb.deltaPosition.y * sideY > 0
                    val penetration = (size.y / 2f + tileSize.y / 2f) - Math.abs(hitboxCenter.y - tileCenter.y)

                    if (penetration > -triggerMargin) blocksCollision = true

                    if (penetration > 0f && (movingTowards || penetration > skinWidth)) {
                        rb.speed.y = 0f
                        rb.deltaPosition.y = 0f
                        entity.position.y -= sideY * penetration
                        return
                    }
                }
            }
        }
    }

    private fun checkTrigger() {
        val p1 = mapApi.getPointFromPos(frameLeftTop)
        val p2 = mapApi.getPointFromPos(frameRightBottom)
        forEachOnBorder(p1, p2) { x, y, _, _ ->
            if (isSolid(x, y)) {
                blocksCollision = true
            }
        }
    }

    fun renderUpdate() {
        if (hitboxRender?.isShow ?: false) hitboxRender?.update()
    }

    override val frameLeftTop: Vec2
        get() = entity.position + delta - size / 2f

    override val frameRightBottom: Vec2
        get() = entity.position + delta + size / 2f
}