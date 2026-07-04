package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.LiquidTypes.AbstractLiquidType
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent

class LiquidDetectorComponent(
    entity: Entity,
    var targetHitbox: HitboxComponent? = null
) : EntityComponent(entity) {
    val touchedLiquids = mutableSetOf<AbstractLiquidType>()
    
    var maxViscosity = 0f
        private set
        
    var isHeadInLiquid = false
        private set
        
    var headLiquidType: AbstractLiquidType? = null
        private set

    // Dynamically depends on the target/main hitbox size, falling back to entity size
    var headDelta: Vec2 = 0f v ((targetHitbox ?: entity.mainHitbox)?.size?.y?.let { it * 0.4f } ?: (entity.size.y * 0.4f))
        get() = 0f v ((targetHitbox ?: entity.mainHitbox)?.size?.y?.let { it * 0.4f } ?: (entity.size.y * 0.4f))

    override fun onPhysicUpdate() {
        val currentTouched = mutableSetOf<AbstractLiquidType>()
        var headSubmerged = false
        var headType: AbstractLiquidType? = null

        val hitbox = targetHitbox ?: entity.mainHitbox
        val left = hitbox?.frameLeftTop?.x ?: (entity.position.x - entity.size.x / 2f)
        val right = hitbox?.frameRightBottom?.x ?: (entity.position.x + entity.size.x / 2f)
        val bottom = hitbox?.frameLeftTop?.y ?: (entity.position.y - entity.size.y / 2f)
        val top = hitbox?.frameRightBottom?.y ?: (entity.position.y + entity.size.y / 2f)

        val headPos = entity.position + headDelta

        val mapApi = entity.gameCycle.mapApi
        val liquidApi = entity.gameCycle.liquidApi
        val dimension = entity.dimension

        val p1 = mapApi.getPointFromPos(Vec2(left, bottom))
        val p2 = mapApi.getPointFromPos(Vec2(right, top))

        for (x in p1.x..p2.x) {
            for (y in p1.y..p2.y) {
                val amount = liquidApi.getAmount(dimension, x, y)
                if (amount > 0) {
                    val liquidType = liquidApi.getType(dimension, x, y) ?: continue

                    val liquidMinY = y - 0.5f
                    val liquidMaxY = y - 0.5f + (amount / 255f)
                    
                    val intersectsX = left < x + 0.5f && right > x - 0.5f
                    val intersectsY = bottom < liquidMaxY && top > liquidMinY
                    
                    if (intersectsX && intersectsY) {
                        currentTouched.add(liquidType)

                        val headInsideX = headPos.x >= x - 0.5f && headPos.x <= x + 0.5f
                        val headInsideY = headPos.y >= liquidMinY && headPos.y <= liquidMaxY
                        if (headInsideX && headInsideY) {
                            headSubmerged = true
                            headType = liquidType
                        }
                    }
                }
            }
        }

        // Notify start of contact and add buff
        for (liquid in currentTouched) {
            if (liquid !in touchedLiquids) {
                entity.sendEvent(EntityEvent.LiquidContactStart(liquid))
                liquid.touchBuff?.let { buff ->
                    entity.buffController.addBuff(buff)
                }
            }
        }

        // Notify end of contact and remove buff
        for (liquid in touchedLiquids) {
            if (liquid !in currentTouched) {
                entity.sendEvent(EntityEvent.LiquidContactEnd(liquid))
                liquid.touchBuff?.let { buff ->
                    entity.buffController.removeBuff(buff.tag)
                }
            }
        }

        touchedLiquids.clear()
        touchedLiquids.addAll(currentTouched)
        
        maxViscosity = if (entity.slowsDownInLiquids) {
            touchedLiquids.maxOfOrNull { it.viscosityForEntities } ?: 0f
        } else {
            0f
        }
        
        isHeadInLiquid = headSubmerged
        headLiquidType = headType
    }

    override fun onRemoved() {
        for (liquid in touchedLiquids) {
            liquid.touchBuff?.let { buff ->
                entity.buffController.removeBuff(buff.tag)
            }
        }
        touchedLiquids.clear()
        maxViscosity = 0f
        isHeadInLiquid = false
        headLiquidType = null
    }
}
