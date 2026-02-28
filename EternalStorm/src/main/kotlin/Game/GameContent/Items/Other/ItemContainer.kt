package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameController.GameCycle

class ItemContainer(var gameCycle: GameCycle, val size: Int) {
    val slots: Array<ItemSlot> = Array(size) { ItemSlot(gameCycle, this, it) }

    fun physicUpdate() {
        slots.forEach { if (it.isActive()) it.item?.cellPhysicUpdate(it) }
    }

    fun logicalUpdate() {
        slots.forEach { slot ->
            if (!slot.isActive()) return@forEach
            slot.item?.cellLogicalUpdate(slot)
            if ((slot.item?.count ?: 1) <= 0) slot.item = null
        }
    }

    fun renderUpdate() {
        slots.forEach { if (it.isActive()) it.item?.cellRenderUpdate(it) }
    }

    fun hasFreeSlot() = slots.any { it.isFree() }
    fun getFreeSlotsCount() = slots.count { it.isFree() }
    fun getFirstFreeSlotIndex() = slots.indexOfFirst { it.isFree() }

    // Добавляет с учётом стаков, возвращает остаток
    fun addItem(item: Item): Int {
        var remaining = item.count

        if (item.itemType.maxInStack > 1) {
            for (slot in slots) {
                if (remaining <= 0) break
                if (!slot.isActive()) continue
                val existing = slot.item ?: continue
                if (!existing.canStackable(item)) continue
                val toAdd = minOf(remaining, existing.leftToStack())
                existing.count += toAdd
                remaining -= toAdd
            }
        }

        while (remaining > 0) {
            val freeSlot = slots.firstOrNull { it.isFree() } ?: break
            val toPlace = minOf(remaining, item.itemType.maxInStack)
            freeSlot.item = item.copy().apply { count = toPlace }
            remaining -= toPlace
        }

        return remaining
    }

    // Добавляет полностью или не добавляет вовсе, возвращает успех
    fun addItemFully(item: Item): Boolean {
        if (getFreeCapacityFor(item) < item.count) return false
        addItem(item)
        return true
    }

    // Сколько единиц данного предмета ещё можно принять
    fun getFreeCapacityFor(item: Item): Int {
        val freeSlotCapacity = getFreeSlotsCount() * item.itemType.maxInStack
        val stackableExtra = if (item.itemType.maxInStack > 1)
            slots.sumOf { slot ->
                if (!slot.isActive()) return@sumOf 0
                slot.item?.takeIf { it.canStackable(item) }?.leftToStack() ?: 0
            } else 0
        return freeSlotCapacity + stackableExtra
    }

    // Можно ли добавить хотя бы 1 единицу
    fun canAddItem(item: Item): Boolean {
        if (hasFreeSlot()) return true
        if (item.itemType.maxInStack <= 1) return false
        return slots.any { it.isActive() && it.item?.canStackable(item) == true && it.item!!.leftToStack() > 0 }
    }

    // Суммарное количество предметов данного типа
    fun countItemsOfType(itemType: AbstractItemType) =
        slots.sumOf { if (it.isActive() && it.item?.itemType === itemType) it.item!!.count else 0 }

    // Все слоты с предметом данного типа
    fun getSlotsWithType(itemType: AbstractItemType) =
        slots.filter { it.isActive() && it.item?.itemType === itemType }

    // Отнять amount из слота по индексу, вернуть сколько сняли
    fun removeAmount(index: Int, amount: Int): Int {
        if (!isValidIndex(index) || !slots[index].isActive()) return 0
        val item = slots[index].item ?: return 0
        val actual = minOf(amount, item.count)
        item.count -= actual
        if (item.count <= 0) slots[index].item = null
        return actual
    }

    // Отнять amount предметов данного типа по всем слотам, вернуть сколько сняли
    fun removeAmount(itemType: AbstractItemType, amount: Int): Int {
        var remaining = amount
        for (slot in slots) {
            if (remaining <= 0) break
            if (!slot.isActive()) continue
            val item = slot.item?.takeIf { it.itemType === itemType } ?: continue
            val actual = minOf(remaining, item.count)
            item.count -= actual
            remaining -= actual
            if (item.count <= 0) slot.item = null
        }
        return amount - remaining
    }

    // Есть ли хотя бы amount единиц предмета данного типа
    fun hasAmount(itemType: AbstractItemType, amount: Int) =
        countItemsOfType(itemType) >= amount

    fun setItem(index: Int, item: Item?) {
        if (isValidIndex(index) && slots[index].isActive()) slots[index].item = item
    }

    fun getItem(index: Int): Item? =
        if (isValidIndex(index) && slots[index].isActive()) slots[index].item else null

    fun getSlot(index: Int): ItemSlot? =
        if (isValidIndex(index)) slots[index] else null

    // Забрать предмет из слота (слот становится пустым)
    fun takeItem(index: Int): Item? {
        if (!isValidIndex(index) || !slots[index].isActive()) return null
        val item = slots[index].item
        slots[index].item = null
        return item
    }

    fun removeItem(index: Int) {
        if (isValidIndex(index) && slots[index].isActive()) slots[index].item = null
    }

    fun swap(indexA: Int, indexB: Int) {
        if (!isValidIndex(indexA) || !isValidIndex(indexB)) return
        if (!slots[indexA].isActive() || !slots[indexB].isActive()) return
        val temp = slots[indexA].item
        slots[indexA].item = slots[indexB].item
        slots[indexB].item = temp
    }

    fun clear() = slots.forEach { if (it.isActive()) it.item = null }

    fun setBlocked(index: Int, blocked: Boolean) {
        if (isValidIndex(index)) slots[index].isBlocked = blocked
    }

    fun unchooseAll() {
        slots.forEach { if (it.item?.isChoose == true) it.item?.endChoose() }
    }

    fun choose(id: Int) {
        if (!isValidIndex(id)) return
        unchooseAll()
        getItem(id)?.startChoose()
    }

    // Вставить в слот: стакаются — докидывает остаток; нет — меняет местами
    fun insertItem(index: Int, item: Item): Item? {
        if (!isValidIndex(index) || !slots[index].isActive()) return item
        val existing = slots[index].item ?: run { slots[index].item = item; return null }

        if (existing.canStackable(item)) {
            val toAdd = minOf(item.count, existing.leftToStack())
            existing.count += toAdd
            item.count -= toAdd
            return if (item.count > 0) item else null
        }

        slots[index].item = item
        return existing
    }

    // Вставить в слот только если стакаются, иначе вернуть item без изменений
    fun insertStackOnly(index: Int, item: Item): Item? {
        if (!isValidIndex(index) || !slots[index].isActive()) return item
        val existing = slots[index].item ?: run {
            val toPlace = minOf(item.count, item.itemType.maxInStack)
            slots[index].item = item.copy().apply { count = toPlace }
            item.count -= toPlace
            return if (item.count > 0) item else null
        }

        if (!existing.canStackable(item)) return item
        val toAdd = minOf(item.count, existing.leftToStack())
        existing.count += toAdd
        item.count -= toAdd
        return if (item.count > 0) item else null
    }

    fun isValidIndex(index: Int) = index in 0 until size
}