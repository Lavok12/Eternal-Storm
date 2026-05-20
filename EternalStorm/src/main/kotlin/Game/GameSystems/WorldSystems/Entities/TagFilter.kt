package la.vok.Game.GameSystems.WorldSystems.Entities

sealed class TagFilter {
    abstract fun matches(tags: List<String>): Boolean

    object Any : TagFilter() {
        override fun matches(tags: List<String>): Boolean = true
    }

    data class Single(val tag: String) : TagFilter() {
        override fun matches(tags: List<String>): Boolean = tags.contains(tag)
    }

    data class HasAny(val requiredTags: List<String>) : TagFilter() {
        override fun matches(tags: List<String>): Boolean = requiredTags.any { tags.contains(it) }
    }

    data class HasAll(val requiredTags: List<String>) : TagFilter() {
        override fun matches(tags: List<String>): Boolean = tags.containsAll(requiredTags)
    }
    
    data class Exclude(val excludedTag: String) : TagFilter() {
        override fun matches(tags: List<String>): Boolean = !tags.contains(excludedTag)
    }
}

/**
 * Extension to quickly convert a String to a Single TagFilter.
 * Usage: "player".toTagFilter()
 */
fun String.toTagFilter() = TagFilter.Single(this)