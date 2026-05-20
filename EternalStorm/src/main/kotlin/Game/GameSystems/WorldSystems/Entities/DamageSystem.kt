package la.vok.Game.GameSystems.WorldSystems.Entities

import la.vok.Game.GameContent.Entities.Entities.Special.DamageEntity
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class DamageSystem(val gameCycle: GameCycle) {

    fun damageZone(dimension: AbstractDimension, pos: Vec2, size: Vec2, damage: DamageData, tagFilter: TagFilter = TagFilter.Any): Entity {
        AppState.logger.info("Spawn Damage Zone $pos $size $damage $tagFilter")
        val entity = DamageEntity(gameCycle, pos, size, damage, tagFilter)
        gameCycle.entityApi.spawnEntity(dimension, entity, pos)
        entity.damage()
        return entity
    }

    fun logicalDamage(entity: Entity, damage: DamageData, hitboxComponent: HitboxComponent) {
        entity.takeDamage(damage, hitboxComponent)
    }

    fun absoluteDamage(dimension: AbstractDimension, entity: Entity, damage: DamageData) {
        if (entity.hpBody == null) return
        entity.knockback(damage.force)
        entity.hpBody?.hp -= damage.value
        checkEntityHp(dimension, entity)
        entity.damage(damage)
        gameCycle.vfxObjectsApi.spawnDamageValue(dimension, entity.position, damage.value)
    }

    fun checkEntityHp(dimension: AbstractDimension, entity: Entity) {
        if (entity.hpBody == null) return
        if (entity.hpBody!!.hp <= 0) {
            gameCycle.entityApi.killInSystem(dimension, entity)
        }
    }
}
