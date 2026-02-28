package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Items.Other.ItemContainer

class MobInventory(entity: Entity, size: Int) : EntityComponent(entity) {
    var itemContainer: ItemContainer = ItemContainer(entity.gameCycle, size)

    fun physicUpdate() {
        itemContainer.physicUpdate()
    }

    fun logicalUpdate() {
        itemContainer.logicalUpdate()
    }

    fun renderUpdate() {
        itemContainer.renderUpdate()
    }

    fun choose(id: Int, handItemComponent: HandItemComponent?) {
        handItemComponent?.setItem(itemContainer.getItem(id))
        itemContainer.choose(id)
    }
    fun unchooseAll(handItemComponent: HandItemComponent?) {
        handItemComponent?.setItem(null)
        itemContainer.unchooseAll()
    }
}