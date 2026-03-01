package la.vok.Game.GameContent.Entities.Entities

import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.Vec2

@Suppress("UNCHECKED_CAST")
class DamageEntity(gameCycle: GameCycle, var d_position: Vec2, var d_size: Vec2, var damage: DamageData, var tagFilter: TagFilter = TagFilter.Any) : EmptyEntity(gameCycle) {
    init {
        spawn()
    }
    override fun spawn() {
        this.position = d_position
        this.size = d_size

        hasDownTrigger = true
        hasCollisionDetector = true

        addHitbox("main", HitboxTypes.ONLY_TRIGGER, null).size = size.copy()
        if (hasCollisionDetector) {
            createBaseCollisionDetector()
        }
    }

    fun damage() {
        collisionDetector?.tagFilter = tagFilter

        collisionDetector!!.onContactStart = {it ->
            gameCycle.entityApi.logicalDamage(it.entity, damage, it)
        }
        collisionDetector!!.update()

        gameCycle.entityApi.killInSystem(this)
    }
}