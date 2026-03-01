package la.vok.Game.GameSystems.WorldSystems.Crafts

import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.Crafts.CraftType
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import kotlin.collections.get
import kotlin.collections.minusAssign
import kotlin.compareTo
import kotlin.times

// --- Результат анализа крафта ---

data class CraftIngredientStatus(
    val itemType: AbstractItemType,
    val required: Int,
    val available: Int
) {
    val missing: Int get() = maxOf(0, required - available)
    val isSatisfied: Boolean get() = available >= required
    val percent: Float get() = if (required == 0) 1f else (available.toFloat() / required).coerceIn(0f, 1f)
}

data class CraftAnalysis(
    val craft: CraftType,
    val ingredients: List<CraftIngredientStatus>
) {
    val canCraft: Boolean get() = ingredients.all { it.isSatisfied }
    val missingIngredients: List<CraftIngredientStatus> get() = ingredients.filter { !it.isSatisfied }
    val satisfiedIngredients: List<CraftIngredientStatus> get() = ingredients.filter { it.isSatisfied }
    val completionPercent: Float get() =
        if (ingredients.isEmpty()) 1f
        else ingredients.map { it.percent }.average().toFloat()
}

// --- API ---

class CraftApi(val gameCycle: GameCycle) {

    private val objectRegistration: ObjectRegistration
        get() = gameCycle.gameController.coreController.objectRegistration

    val allCrafts: List<CraftType>
        get() = objectRegistration.crafts

    // =========================================================
    // СНАПШОТ — подсчёт всех предметов за один проход
    // =========================================================

    fun snapshot(vararg containers: ItemContainer): Map<AbstractItemType, Int> {
        val map = HashMap<AbstractItemType, Int>()
        for (container in containers) {
            for (slot in container.slots) {
                val item = slot.item ?: continue
                map[item.itemType] = (map[item.itemType] ?: 0) + item.count
            }
        }
        return map
    }

    fun snapshot(containers: List<ItemContainer>): Map<AbstractItemType, Int> =
        snapshot(*containers.toTypedArray())

    // =========================================================
    // АНАЛИЗ
    // =========================================================

    fun analyze(craft: CraftType, snap: Map<AbstractItemType, Int>): CraftAnalysis {
        val statuses = craft.ingredientTypes.map { (type, required) ->
            val available = snap[type] ?: 0
            CraftIngredientStatus(type, required, available)
        }
        return CraftAnalysis(craft, statuses)
    }

    fun analyze(craft: CraftType, vararg containers: ItemContainer): CraftAnalysis =
        analyze(craft, snapshot(*containers))

    fun analyze(craft: CraftType, containers: List<ItemContainer>): CraftAnalysis =
        analyze(craft, snapshot(containers))

    // =========================================================
    // ПАКЕТНЫЙ АНАЛИЗ — один снапшот на все крафты
    // =========================================================

    fun analyzeAll(vararg containers: ItemContainer): List<CraftAnalysis> {
        val snap = snapshot(*containers)
        return allCrafts.map { analyze(it, snap) }
    }

    fun analyzeAll(containers: List<ItemContainer>): List<CraftAnalysis> =
        analyzeAll(*containers.toTypedArray())

    fun analyzeAll(snap: Map<AbstractItemType, Int>): List<CraftAnalysis> =
        allCrafts.map { analyze(it, snap) }

    // =========================================================
    // ФИЛЬТРАЦИЯ
    // =========================================================

    fun getAvailableCrafts(vararg containers: ItemContainer): List<CraftType> {
        val snap = snapshot(*containers)
        return allCrafts.filter { analyze(it, snap).canCraft }
    }

    fun getAvailableCrafts(containers: List<ItemContainer>): List<CraftType> =
        getAvailableCrafts(*containers.toTypedArray())

    fun getCrafts(filter: (CraftType) -> Boolean): List<CraftType> =
        allCrafts.filter(filter)

    fun getCraftsByPriority(priority: Int): List<CraftType> =
        objectRegistration.craftsByPriority[priority] ?: emptyList()

    // =========================================================
    // ЧТО ХВАТАЕТ / НЕ ХВАТАЕТ
    // =========================================================

    fun getMissing(craft: CraftType, vararg containers: ItemContainer) =
        analyze(craft, *containers).missingIngredients

    fun getSatisfied(craft: CraftType, vararg containers: ItemContainer) =
        analyze(craft, *containers).satisfiedIngredients

    fun getCompletionPercent(craft: CraftType, vararg containers: ItemContainer): Float =
        analyze(craft, snapshot(*containers)).completionPercent

    fun getCompletionPercent(craft: CraftType, snap: Map<AbstractItemType, Int>): Float =
        analyze(craft, snap).completionPercent

    // Один вызов — один снапшот для всех крафтов
    fun getCompletionPercentForAll(vararg containers: ItemContainer): Map<CraftType, Float> {
        val snap = snapshot(*containers)
        return allCrafts.associateWith { analyze(it, snap).completionPercent }
    }

    fun getMissingForAll(vararg containers: ItemContainer): Map<CraftType, List<CraftIngredientStatus>> {
        val snap = snapshot(*containers)
        return allCrafts.associateWith { analyze(it, snap).missingIngredients }
    }

    fun getSatisfiedForAll(vararg containers: ItemContainer): Map<CraftType, List<CraftIngredientStatus>> {
        val snap = snapshot(*containers)
        return allCrafts.associateWith { analyze(it, snap).satisfiedIngredients }
    }

    fun canCraft(craft: CraftType, vararg containers: ItemContainer): Boolean =
        analyze(craft, snapshot(*containers)).canCraft

    fun canCraft(craft: CraftType, snap: Map<AbstractItemType, Int>): Boolean =
        analyze(craft, snap).canCraft

    fun maxCraftAmount(craft: CraftType, vararg containers: ItemContainer): Int {
        val snap = snapshot(*containers)
        return maxCraftAmount(craft, snap)
    }

    fun maxCraftAmount(craft: CraftType, snap: Map<AbstractItemType, Int>): Int {
        if (craft.ingredientTypes.isEmpty()) return Int.MAX_VALUE
        return craft.ingredientTypes.minOf { (type, required) ->
            if (required == 0) Int.MAX_VALUE
            else (snap[type] ?: 0) / required
        }
    }

    // =========================================================
    // КРАФТ
    // =========================================================

    fun craft(
        container: ItemContainer,
        craft: CraftType,
        entity: Entity? = null,
        amount: Int = 1
    ): Boolean {
        if (amount <= 0) return false
        val snap = snapshot(container)

        for ((type, required) in craft.ingredientTypes) {
            if ((snap[type] ?: 0) < required * amount) return false
        }

        for ((type, required) in craft.ingredientTypes) {
            container.removeAmount(type, required * amount)
        }

        spawnResults(craft, container, entity, amount)
        return true
    }

    fun craft(
        sourceContainers: List<ItemContainer>,
        resultContainer: ItemContainer,
        craft: CraftType,
        entity: Entity? = null,
        amount: Int = 1
    ): Boolean {
        if (amount <= 0) return false
        val snap = snapshot(sourceContainers)

        for ((type, required) in craft.ingredientTypes) {
            if ((snap[type] ?: 0) < required * amount) return false
        }

        for ((type, required) in craft.ingredientTypes) {
            var toRemove = required * amount
            for (src in sourceContainers) {
                if (toRemove <= 0) break
                toRemove -= src.removeAmount(type, toRemove)
            }
        }

        spawnResults(craft, resultContainer, entity, amount)
        return true
    }

    private fun spawnResults(craft: CraftType, container: ItemContainer, entity: Entity?, amount: Int) {
        fun giveItem(type: AbstractItemType, count: Int) {
            val item = gameCycle.itemsApi.getRegisteredItem(type, count)
            val remaining = container.addItem(item)
            if (remaining > 0 && entity != null) {
                gameCycle.itemsApi.spawnItemEntity(
                    gameCycle.itemsApi.getRegisteredItem(type, remaining),
                    entity.position, randomVelocity = true
                )
            }
        }

        val resultType = craft.resultType ?: return
        giveItem(resultType, craft.result.count * amount)
        for ((type, count) in craft.extraResultTypes) {
            giveItem(type, count * amount)
        }
    }
}