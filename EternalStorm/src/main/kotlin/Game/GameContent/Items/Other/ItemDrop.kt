package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.random.Random

sealed class DropEntry {
    abstract fun resolve(): HashMap<String, Int>
}

// --- Entries ---

class ItemDrop(
    val tag: String,
    val count: Int = 1,
    val countMin: Int? = null,
    val countMax: Int? = null
) : DropEntry() {
    override fun resolve(): HashMap<String, Int> {
        val resolvedCount = if (countMin != null && countMax != null)
            Random.nextInt(countMin, countMax + 1) else count
        return hashMapOf(tag to resolvedCount)
    }
}

class RandomDrop(val variants: List<Pair<DropEntry, Float>>) : DropEntry() {
    override fun resolve(): HashMap<String, Int> {
        val totalWeight = variants.sumOf { it.second.toDouble() }.toFloat()
        var roll = Random.nextFloat() * totalWeight
        for ((entry, weight) in variants) {
            roll -= weight
            if (roll <= 0f) return entry.resolve()
        }
        return variants.last().first.resolve()
    }
}

class DropTable(val entries: List<DropEntry>) : DropEntry() {
    override fun resolve(): HashMap<String, Int> {
        val result = HashMap<String, Int>()
        entries.forEach { entry ->
            entry.resolve().forEach { (tag, count) ->
                result[tag] = (result[tag] ?: 0) + count
            }
        }
        return result
    }

    fun spawn(dimension: AbstractDimension, pos: Vec2) {
        resolve().forEach { (tag, count) ->
            val item = dimension.gameCycle.itemsApi.getRegisteredItem(tag, count)
            dimension.gameCycle.itemsApi.spawnItemEntity(dimension, item, pos, randomVelocity = true)
        }
    }
}

// --- DSL ---

class DropTableBuilder {
    private val entries = mutableListOf<DropEntry>()

    fun drop(entry: DropEntry) {
        entries += entry
    }

    fun item(tag: String, count: Int = 1) {
        entries += ItemDrop(tag, count)
    }

    fun item(tag: String, countMin: Int, countMax: Int) {
        entries += ItemDrop(tag, countMin = countMin, countMax = countMax)
    }

    fun random(block: RandomDropBuilder.() -> Unit) {
        entries += RandomDropBuilder().apply(block).build()
    }

    fun build() = DropTable(entries.toList())
}

object NothingDrop : DropEntry() {
    override fun resolve() = hashMapOf<String, Int>()
}
class RandomDropBuilder {
    private val variants = mutableListOf<Pair<DropEntry, Float>>()

    fun item(tag: String, count: Int = 1, weight: Float = 1f) {
        variants += ItemDrop(tag, count) to weight
    }

    fun item(tag: String, countMin: Int, countMax: Int, weight: Float = 1f) {
        variants += ItemDrop(tag, countMin = countMin, countMax = countMax) to weight
    }

    fun random(weight: Float = 1f, block: RandomDropBuilder.() -> Unit) {
        variants += RandomDropBuilder().apply(block).build() to weight
    }

    fun table(weight: Float = 1f, block: DropTableBuilder.() -> Unit) {
        variants += DropTableBuilder().apply(block).build() to weight
    }

    fun nothing(weight: Float = 1f) {
        variants += NothingDrop to weight
    }

    fun build() = RandomDrop(variants.toList())
}

class SingleDrop(val tag: String, val count: Int = 1) : DropEntry() {
    override fun resolve(): HashMap<String, Int> = hashMapOf(tag to count)
}

fun dropTable(block: DropTableBuilder.() -> Unit): DropTable =
    DropTableBuilder().apply(block).build()