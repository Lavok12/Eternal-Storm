package la.vok.Game.GameSystems.Entities

sealed class TagFilter {

    abstract fun matches(tags: Collection<String>): Boolean

    object Any : TagFilter() {
        override fun matches(tags: Collection<String>) = true
    }

    data class HasAny(val required: Collection<String>) : TagFilter() {
        override fun matches(tags: Collection<String>) = required.any { it in tags }

        override fun toString(): String {
            return required.toString()
        }
    }

    data class HasAll(val required: Collection<String>) : TagFilter() {
        override fun matches(tags: Collection<String>) = required.all { it in tags }

        override fun toString(): String {
            return required.toString()
        }
    }

    data class HasNone(val excluded: Collection<String>) : TagFilter() {
        override fun matches(tags: Collection<String>) = excluded.none { it in tags }

        override fun toString(): String {
            return excluded.toString()
        }
    }

    data class HasNotAll(val required: Collection<String>) : TagFilter() {
        override fun matches(tags: Collection<String>) = !required.all { it in tags }

        override fun toString(): String {
            return required.toString()
        }
    }

    data class And(val filters: Collection<TagFilter>) : TagFilter() {
        override fun matches(tags: Collection<String>) = filters.all { it.matches(tags) }

        override fun toString(): String {
            return filters.toString()
        }
    }

    data class Or(val filters: Collection<TagFilter>) : TagFilter() {
        override fun matches(tags: Collection<String>) = filters.any { it.matches(tags) }

        override fun toString(): String {
            return filters.toString()
        }
    }
}