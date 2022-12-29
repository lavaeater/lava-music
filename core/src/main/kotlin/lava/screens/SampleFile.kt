package lava.screens

import kotlinx.serialization.Serializable

@Serializable
data class SampleFile(val name: String, val path: String) {
    val tags = path.split("/")-path.split("/").last()

    fun extractTags(): String {
        val splitPath = path.split("/")

    }

    override fun toString(): String {
        return "${tags[tags.lastIndex - 1]}-$name"
    }
}
