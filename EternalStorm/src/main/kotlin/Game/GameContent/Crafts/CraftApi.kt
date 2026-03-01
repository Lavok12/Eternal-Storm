package la.vok.Game.GameSystems.WorldSystems.Crafts

import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.Crafts.CraftType
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.ItemContainer
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter

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
    // АНАЛИЗ
    // =========================================================

    fun analyze(craft: CraftType, vararg containers: ItemContainer): CraftAnalysis {
        val statuses = craft.ingredientTypes.map { (type, required) ->
            val available = containers.sumOf { it.countItemsOfType(type) }
            CraftIngredientStatus(type, required, available)
        }
        return CraftAnalysis(craft, statuses)
    }

    fun analyze(craft: CraftType, containers: List<ItemContainer>): CraftAnalysis =
        analyze(craft, *containers.toTypedArray())

    // =========================================================
    // ФИЛЬТРАЦИЯ КРАФТОВ
    // =========================================================

    fun getAvailableCrafts(vararg containers: ItemContainer): List<CraftType> =
        allCrafts.filter { analyze(it, *containers).canCraft }

    fun getAvailableCrafts(containers: List<ItemContainer>): List<CraftType> =
        getAvailableCrafts(*containers.toTypedArray())

    fun getCrafts(filter: (CraftType) -> Boolean): List<CraftType> =
        allCrafts.filter(filter)

    fun getCraftsByPriority(priority: Int): List<CraftType> =
        objectRegistration.craftsByPriority[priority] ?: emptyList()

    fun getCraftsWithTagFilter(tagFilter: TagFilter): List<CraftType> =
        allCrafts  // расширяемо если у CraftType появятся теги

    fun analyzeAll(vararg containers: ItemContainer): List<CraftAnalysis> =
        allCrafts.map { analyze(it, *containers) }

    fun analyzeAll(containers: List<ItemContainer>): List<CraftAnalysis> =
        analyzeAll(*containers.toTypedArray())

    // =========================================================
    // ЧТО ХВАТАЕТ / НЕ ХВАТАЕТ
    // =========================================================

    fun getMissing(craft: CraftType, vararg containers: ItemContainer): List<CraftIngredientStatus> =
        analyze(craft, *containers).missingIngredients

    fun getSatisfied(craft: CraftType, vararg containers: ItemContainer): List<CraftIngredientStatus> =
        analyze(craft, *containers).satisfiedIngredients

    fun getCompletionPercent(craft: CraftType, vararg containers: ItemContainer): Float =
        analyze(craft, *containers).completionPercent

    fun getMissingForAll(vararg containers: ItemContainer): Map<CraftType, List<CraftIngredientStatus>> =
        allCrafts.associateWith { getMissing(it, *containers) }

    fun getSatisfiedForAll(vararg containers: ItemContainer): Map<CraftType, List<CraftIngredientStatus>> =
        allCrafts.associateWith { getSatisfied(it, *containers) }

    fun getCompletionPercentForAll(vararg containers: ItemContainer): Map<CraftType, Float> =
        allCrafts.associateWith { getCompletionPercent(it, *containers) }

    // =========================================================
    // КРАФТ
    // =========================================================

    /**
     * Выполнить крафт.
     * @param container контейнер с ингредиентами и для результата
     * @param craft крафт
     * @param entity сущность для выброса лишнего (если нет места)
     * @param amount сколько раз скрафтить
     * @return true если успешно
     */
    fun craft(
        container: ItemContainer,
        craft: CraftType,
        entity: Entity? = null,
        amount: Int = 1
    ): Boolean {
        if (amount <= 0) return false

        // Проверяем что хватает ингредиентов на amount раз
        for ((type, required) in craft.ingredientTypes) {
            val totalRequired = required * amount
            if (!container.hasAmount(type, totalRequired)) return false
        }

        // Снимаем ингредиенты
        for ((type, required) in craft.ingredientTypes) {
            container.removeAmount(type, required * amount)
        }

        // Выдаём основной результат
        val resultType = craft.resultType ?: return false
        val resultItem = gameCycle.itemsApi.getRegisteredItem(resultType, craft.result.count * amount)
        val remaining = container.addItem(resultItem)
        if (remaining > 0 && entity != null) {
            val spawnItem = gameCycle.itemsApi.getRegisteredItem(resultType, remaining)
            gameCycle.itemsApi.spawnItemEntity(spawnItem, entity.position, randomVelocity = true)
        }

        // Выдаём доп результаты
        for ((type, count) in craft.extraResultTypes) {
            val extraItem = gameCycle.itemsApi.getRegisteredItem(type, count * amount)
            val extraRemaining = container.addItem(extraItem)
            if (extraRemaining > 0 && entity != null) {
                val spawnItem = gameCycle.itemsApi.getRegisteredItem(type, extraRemaining)
                gameCycle.itemsApi.spawnItemEntity(spawnItem, entity.position, randomVelocity = true)
            }
        }

        return true
    }

    /**
     * Крафт из нескольких контейнеров-источников в контейнер-результат.
     */
    fun craft(
        sourceContainers: List<ItemContainer>,
        resultContainer: ItemContainer,
        craft: CraftType,
        entity: Entity? = null,
        amount: Int = 1
    ): Boolean {
        if (amount <= 0) return false

        // Проверяем наличие ингредиентов суммарно
        for ((type, required) in craft.ingredientTypes) {
            val totalAvailable = sourceContainers.sumOf { it.countItemsOfType(type) }
            if (totalAvailable < required * amount) return false
        }

        // Снимаем ингредиенты из источников по очереди
        for ((type, required) in craft.ingredientTypes) {
            var toRemove = required * amount
            for (src in sourceContainers) {
                if (toRemove <= 0) break
                val removed = src.removeAmount(type, toRemove)
                toRemove -= removed
            }
        }

        // Выдаём результат
        val resultType = craft.resultType ?: return false
        val resultItem = gameCycle.itemsApi.getRegisteredItem(resultType, craft.result.count * amount)
        val remaining = resultContainer.addItem(resultItem)
        if (remaining > 0 && entity != null) {
            gameCycle.itemsApi.spawnItemEntity(
                gameCycle.itemsApi.getRegisteredItem(resultType, remaining),
                entity.position, randomVelocity = true
            )
        }

        for ((type, count) in craft.extraResultTypes) {
            val extraItem = gameCycle.itemsApi.getRegisteredItem(type, count * amount)
            val extraRemaining = resultContainer.addItem(extraItem)
            if (extraRemaining > 0 && entity != null) {
                gameCycle.itemsApi.spawnItemEntity(
                    gameCycle.itemsApi.getRegisteredItem(type, extraRemaining),
                    entity.position, randomVelocity = true
                )
            }
        }

        return true
    }

    /**
     * Максимальное количество раз, которое можно скрафтить
     */
    fun maxCraftAmount(craft: CraftType, vararg containers: ItemContainer): Int {
        if (craft.ingredientTypes.isEmpty()) return Int.MAX_VALUE
        return craft.ingredientTypes.minOf { (type, required) ->
            if (required == 0) Int.MAX_VALUE
            else containers.sumOf { it.countItemsOfType(type) } / required
        }
    }

    fun maxCraftAmount(craft: CraftType, containers: List<ItemContainer>): Int =
        maxCraftAmount(craft, *containers.toTypedArray())

    fun canCraft(craft: CraftType, vararg containers: ItemContainer): Boolean =
        analyze(craft, *containers).canCraft

    fun canCraft(craft: CraftType, containers: List<ItemContainer>): Boolean =
        canCraft(craft, *containers.toTypedArray())
}