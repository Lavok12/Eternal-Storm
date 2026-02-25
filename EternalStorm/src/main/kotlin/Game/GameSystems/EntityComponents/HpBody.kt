package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.LavokLibrary.Vectors.v

class HpBody(entity: Entity) : EntityComponent(entity) {
    var hp: Int = 0;

    fun setMaxHp() {
        this.hp = entity.entityType.baseHp
    }
}