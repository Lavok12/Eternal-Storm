package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent

/**
 * Interface for all entity components.
 * Components handle specific logic and can subscribe to entity events.
 */
interface IEntityComponent {
    /**
     * Called when the component is attached to an entity.
     */
    fun onAttach(entity: Entity) {}

    /**
     * Called when the entity is spawned into the world.
     */
    fun onSpawn() {}

    /**
     * Called during the physics update cycle.
     */
    fun onPhysicUpdate() {}

    /**
     * Called during the logical update cycle.
     */
    fun onLogicalUpdate() {}

    /**
     * Called during the render update cycle.
     */
    fun onRenderUpdate() {}

    /**
     * Called when an entity event occurs (damage, death, etc.).
     */
    fun onEvent(event: EntityEvent) {}
    
    /**
     * Called when the entity is removed from the world.
     */
    fun onRemoved() {}
}

/**
 * Sealed class for all entity-related events.
 */
sealed class EntityEvent {
    object Spawned : EntityEvent()
    object Death : EntityEvent()
    data class Damage(val data: DamageData, val hitbox: HitboxComponent) : EntityEvent()
    data class ContactStart(val otherHitbox: HitboxComponent) : EntityEvent()
    data class ContactEnd(val otherHitbox: HitboxComponent) : EntityEvent()
    data class DimensionChanged(val oldDim: String, val newDim: String) : EntityEvent()
    object Hidden : EntityEvent()
    object Shown : EntityEvent()
    data class LiquidContactStart(val liquid: la.vok.Game.GameContent.LiquidTypes.AbstractLiquidType) : EntityEvent()
    data class LiquidContactEnd(val liquid: la.vok.Game.GameContent.LiquidTypes.AbstractLiquidType) : EntityEvent()
}
