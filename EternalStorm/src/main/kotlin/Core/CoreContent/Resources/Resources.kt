package la.vok.Core.CoreContent.Resources

data class ResourceLocation(val namespace: String, val path: String) {
    
    override fun toString() = "$namespace:$path"
    
    val fullPath: String get() = "$namespace:$path"

    companion object {
        fun parse(text: String): ResourceLocation {
            val parts = text.split(":")
            return if (parts.size >= 2) {
                // Ignore empty parts or handle multi-colon (though not expected)
                ResourceLocation(parts[0], parts.subList(1, parts.size).joinToString(":"))
            } else {
                ResourceLocation("eternal_storm", text)
            }
        }
    }
}

data class ResourceSource(
    val namespace: String,
    val rootPath: String,
    val priority: Int = 0
)
