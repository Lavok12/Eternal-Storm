package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.Entities.Special.ItemEntity
import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.Game.GameContent.EntityTags

class PickUpComponent(
    entity: Entity,
    var pickUpHitbox: HitboxComponent,
    var attractHitbox: HitboxComponent,
    var container: ItemContainer,
    val checkInterval: Long = 3L,
    val attractForce: Float = 0.08f,
    val gravityPower: Float = 1f
) : EntityComponent(entity) {

    private val pickUpDetector = CollisionDetector(
        entity = entity,
        hitboxComponent = pickUpHitbox,
        gameCycle = entity.gameCycle,
        tagFilter = TagFilter.HasAll(listOf(EntityTags.item))
    )

    private val attractDetector = CollisionDetector(
        entity = entity,
        hitboxComponent = attractHitbox,
        gameCycle = entity.gameCycle,
        tagFilter = TagFilter.HasAll(listOf(EntityTags.item))
    )

    private fun canPickUp(itemEntity: ItemEntity): Boolean {
        if (!entity.gameCycle.entityApi.isExist(itemEntity)) return false
        val item = itemEntity.item ?: return false
        return container.canAddItem(item)
    }

    private fun attract(itemEntity: ItemEntity) {
        val dir = entity.position - itemEntity.position
        val dist = dir.length()
        if (dist < 0.01f) return
        val force = dir.normalized() * (attractForce * (1f - (dist / 3f)).coerceAtLeast(0.1f))
        itemEntity.rigidBody?.addForce(force * gravityPower)
    }

    private fun tryPickUp(itemEntity: ItemEntity) {
        val item = itemEntity.item ?: return
        val remaining = container.addItem(item)
        if (remaining <= 0) {
            entity.gameCycle.entityApi.killInSystem(itemEntity)
        } else {
            item.count = remaining
        }
    }

    private fun updateAttract() {
        attractDetector.activeContacts.toList().forEach { hitbox ->
            val itemEntity = hitbox.entity as? ItemEntity ?: return@forEach
            if (!canPickUp(itemEntity)) return@forEach
            attract(itemEntity)
        }
    }

    private fun updatePickUp() {
        pickUpDetector.activeContacts.toList().forEach { hitbox ->
            val itemEntity = hitbox.entity as? ItemEntity ?: return@forEach
            if (!canPickUp(itemEntity)) return@forEach
            tryPickUp(itemEntity)
        }
    }

    fun physicUpdate() {
        pickUpDetector.update()
        attractDetector.update()
        if (entity.physicTicks % checkInterval != 0L) return
        updateAttract()
        updatePickUp()
    }
}