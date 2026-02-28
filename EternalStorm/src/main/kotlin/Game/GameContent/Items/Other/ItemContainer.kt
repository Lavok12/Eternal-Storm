package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameController.GameCycle

class ItemContainer(var gameCycle: GameCycle, val size: Int) {
    val slots: Array<ItemSlot> = Array(size) { ItemSlot(gameCycle, this, it) }

    // --- Обновления (игнорируют заблокированные слоты) ---

    fun physicUpdate() {
        slots.forEach { slot ->
            if (slot.isActive()) slot.item?.cellPhysicUpdate(slot)
        }
    }

    fun logicalUpdate() {
        slots.forEach { slot ->
            if (slot.isActive()) {
                slot.item?.cellLogicalUpdate(slot)
                if ((slot.item?.count ?: 1) <= 0) {
                    slot.item = null
                }
            }
        }
    }

    fun renderUpdate() {
        slots.forEach { slot ->
            if (slot.isActive()) slot.item?.cellRenderUpdate(slot)
        }
    }

    // --- Информационные методы ---

    fun hasFreeSlot(): Boolean {
        return slots.any { it.isFree() }
    }

    fun getFreeSlotsCount(): Int {
        return slots.count { it.isFree() }
    }

    fun getFirstFreeSlotIndex(): Int {
        return slots.indexOfFirst { it.isFree() }
    }

    // --- Взаимодействие ---

    /**
     * Добавляет предмет в контейнер с учётом стакинга.
     * Возвращает количество единиц, которые НЕ удалось добавить (0 = всё влезло).
     */
    fun addItem(item: Item): Int {
        var remaining = item.count

        // 1. Докидываем в существующие стаки
        if (item.itemType.maxInStack > 1) {
            for (slot in slots) {
                if (remaining <= 0) break
                if (!slot.isActive()) continue
                val existing = slot.item ?: continue
                if (!existing.canStackable(item)) continue

                val canFit = existing.leftToStack()
                val toAdd = minOf(remaining, canFit)
                existing.count += toAdd
                remaining -= toAdd
            }
        }

        // 2. Остаток раскладываем по свободным слотам
        while (remaining > 0) {
            val freeSlot = slots.firstOrNull { it.isFree() } ?: break

            val toPlace = minOf(remaining, item.itemType.maxInStack)
            val newItem = item.copy().apply { count = toPlace }
            freeSlot.item = newItem
            remaining -= toPlace
        }

        return remaining
    }

    /**
     * Добавляет предмет и возвращает true, если всё удалось разместить.
     */
    fun addItemFully(item: Item): Boolean = addItem(item) == 0

    /**
     * Считает сколько единиц предмета данного типа можно ещё принять.
     */
    fun getFreeCapacityFor(item: Item): Int {
        val freeSlotCapacity = getFreeSlotsCount() * item.itemType.maxInStack
        val stackableExtra = if (item.itemType.maxInStack > 1) {
            slots.sumOf { slot ->
                if (!slot.isActive()) return@sumOf 0
                val existing = slot.item ?: return@sumOf 0
                if (existing.canStackable(item)) existing.leftToStack() else 0
            }
        } else 0
        return freeSlotCapacity + stackableExtra
    }

    /**
     * Считает суммарное количество предметов данного типа в контейнере.
     */
    fun countItemsOfType(itemType: AbstractItemType): Int {
        return slots.sumOf { slot ->
            if (slot.isActive() && slot.item?.itemType === itemType) slot.item!!.count else 0
        }
    }

    /**
     * Возвращает все слоты, содержащие предмет данного типа.
     */
    fun getSlotsWithType(itemType: AbstractItemType): List<ItemSlot> {
        return slots.filter { it.isActive() && it.item?.itemType === itemType }
    }

    /**
     * Отнимает [amount] единиц из слота по индексу.
     * Возвращает сколько фактически удалось снять.
     */
    fun removeAmount(index: Int, amount: Int): Int {
        if (!isValidIndex(index) || !slots[index].isActive()) return 0
        val item = slots[index].item ?: return 0

        val actual = minOf(amount, item.count)
        item.count -= actual
        if (item.count <= 0) slots[index].item = null

        return actual
    }

    /**
     * Отнимает [amount] единиц предмета данного типа, начиная с первых найденных слотов.
     * Возвращает сколько фактически удалось снять.
     */
    fun removeAmount(itemType: AbstractItemType, amount: Int): Int {
        var remaining = amount
        for (slot in slots) {
            if (remaining <= 0) break
            if (!slot.isActive()) continue
            val item = slot.item ?: continue
            if (item.itemType !== itemType) continue

            val actual = minOf(remaining, item.count)
            item.count -= actual
            remaining -= actual
            if (item.count <= 0) slot.item = null
        }
        return amount - remaining
    }

    fun setItem(index: Int, item: Item?) {
        if (isValidIndex(index) && slots[index].isActive()) {
            slots[index].item = item
        }
    }

    fun getItem(index: Int): Item? {
        return if (isValidIndex(index) && slots[index].isActive()) slots[index].item else null
    }

    fun getSlot(index: Int): ItemSlot? {
        if (!isValidIndex(index)) return null
        return slots[index]
    }

    fun takeItem(index: Int): Item? {
        if (!isValidIndex(index) || !slots[index].isActive()) return null
        val item = slots[index].item
        slots[index].item = null
        return item
    }

    fun removeItem(index: Int) {
        if (isValidIndex(index) && slots[index].isActive()) {
            slots[index].item = null
        }
    }

    fun swap(indexA: Int, indexB: Int) {
        if (isValidIndex(indexA) && isValidIndex(indexB)) {
            if (slots[indexA].isActive() && slots[indexB].isActive()) {
                val temp = slots[indexA].item
                slots[indexA].item = slots[indexB].item
                slots[indexB].item = temp
            }
        }
    }

    fun clear() {
        slots.forEach { if (it.isActive()) it.item = null }
    }

    fun setBlocked(index: Int, blocked: Boolean) {
        if (isValidIndex(index)) {
            slots[index].isBlocked = blocked
        }
    }

    fun unchooseAll() {
        for (i in slots) {
            if (i.item?.isChoose ?: false) {
                i.item?.endChoose()
            }
        }
    }

    fun choose(id: Int) {
        if (!isValidIndex(id)) return
        unchooseAll()
        getItem(id)?.startChoose()
    }

    fun insertItem(index: Int, item: Item): Item? {
        if (!isValidIndex(index) || !slots[index].isActive()) return item

        val existing = slots[index].item

        if (existing == null) {
            slots[index].item = item
            return null
        }

        if (existing.canStackable(item)) {
            val canFit = existing.leftToStack()
            val toAdd = minOf(item.count, canFit)
            existing.count += toAdd
            item.count -= toAdd
            return if (item.count > 0) item else null
        }

        // Не стакаются — меняем местами
        slots[index].item = item
        return existing
    }

    fun insertStackOnly(index: Int, item: Item): Item? {
        if (!isValidIndex(index) || !slots[index].isActive()) return item

        val existing = slots[index].item

        if (existing == null) {
            val toPlace = minOf(item.count, item.itemType.maxInStack)
            val newItem = item.copy().apply { count = toPlace }
            slots[index].item = newItem
            item.count -= toPlace
            return if (item.count > 0) item else null
        }

        if (!existing.canStackable(item)) return item

        val canFit = existing.leftToStack()
        val toAdd = minOf(item.count, canFit)
        existing.count += toAdd
        item.count -= toAdd
        return if (item.count > 0) item else null
    }

    fun isValidIndex(index: Int): Boolean = index in 0 until size
}