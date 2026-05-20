package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
class DamageEntity(gameCycle: GameCycle, var d_position: Vec2, var d_size: Vec2, var damage: DamageData, var tagFilter: TagFilter = TagFilter.Any) : EmptyEntity(gameCycle) {
    override fun spawn() {
        this.position = d_position
        // Inflate size slightly for better contact reliability
        this.size = d_size + (0.1f v 0.1f)

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
        
        // Force collision grid rebuild for current dimension to see THIS entity
        gameCycle.collisionSystem.requestRebuild(dimension!!)
        collisionDetector!!.update()

        gameCycle.entityApi.killInSystem(dimension!!, this)
    }
}