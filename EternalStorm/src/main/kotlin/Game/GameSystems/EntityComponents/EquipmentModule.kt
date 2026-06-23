package la.vok.Game.GameSystems.EntityComponents

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.Game.GameContent.Items.Other.ItemSlot
import la.vok.Game.GameSystems.WorldSystems.Equipment.EquipmentSlot

class EquipmentModule(entity: Entity) : EntityComponent(entity) {
    // Map slot names to indices in the ItemContainer
    private val slotToIndex = EquipmentSlot.allSlots.withIndex().associate { it.value to it.index }
    
    val itemContainer = ItemContainer(entity.gameCycle, EquipmentSlot.allSlots.size)

    fun setEquipment(slot: String, item: Item?): Item? {
        val index = slotToIndex[slot] ?: return item
        val oldItem = itemContainer.getItem(index)
        itemContainer.setItem(index, item)
        return oldItem
    }

    fun getEquipment(slot: String): Item? {
        val index = slotToIndex[slot] ?: return null
        return itemContainer.getItem(index)
    }

    fun getSlot(slot: String): ItemSlot? {
        val index = slotToIndex[slot] ?: return null
        return itemContainer.getSlot(index)
    }

    fun physicUpdate() {
        itemContainer.physicUpdate()
    }

    fun logicalUpdate() {
        itemContainer.logicalUpdate()
    }

    fun renderUpdate() {
        itemContainer.renderUpdate()
    }
}
