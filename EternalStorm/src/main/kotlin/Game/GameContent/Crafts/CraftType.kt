package la.vok.Game.GameContent.Crafts

import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.Items.Other.AbstractItemType

data class CraftIngredient(val tag: String, val count: Int)
data class CraftResult(val tag: String, val count: Int)

class CraftType(
    val result: CraftResult,
    val ingredients: List<CraftIngredient>,
    val extraResults: List<CraftResult> = emptyList(),
    val priority: Int = 0
) {
    var resultType: AbstractItemType? = null
    var ingredientTypes: List<Pair<AbstractItemType, Int>> = emptyList()
    var extraResultTypes: List<Pair<AbstractItemType, Int>> = emptyList()

    fun resolve(objectRegistration: ObjectRegistration) {
        resultType = objectRegistration.getItemType(result.tag)
        ingredientTypes = ingredients.map { objectRegistration.getItemType(it.tag) to it.count }
        extraResultTypes = extraResults.map { objectRegistration.getItemType(it.tag) to it.count }
    }
}

class CraftBuilder {
    private var result: CraftResult? = null
    private val ingredients = mutableListOf<CraftIngredient>()
    private val extraResults = mutableListOf<CraftResult>()
    private var priority: Int = 0

    fun result(tag: String, count: Int = 1) { result = CraftResult(tag, count) }
    fun ingredient(tag: String, count: Int = 1) { ingredients += CraftIngredient(tag, count) }
    fun extra(tag: String, count: Int = 1) { extraResults += CraftResult(tag, count) }
    fun priority(value: Int) { priority = value }

    fun build() = CraftType(
        result = result ?: error("CraftType: result is not set"),
        ingredients = ingredients.toList(),
        extraResults = extraResults.toList(),
        priority = priority
    )
}

fun craft(block: CraftBuilder.() -> Unit): CraftType =
    CraftBuilder().apply(block).build()