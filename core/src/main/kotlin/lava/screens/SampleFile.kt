package lava.screens

import kotlinx.serialization.Serializable

@Serializable
class NewSampler() {

}

@Serializable
data class SampleFile(var name: String, var path: String, val excludes: List<String>) {
    private fun extractTags(): List<String> {
        var splitPath = path.split("/").toMutableList()
        splitPath = splitPath.map{ it.replace("musicradar-","") }.toMutableList()
        splitPath.remove(splitPath.first { it.contains(".wav") })
        splitPath.removeAll(excludes)
        return splitPath
    }
    val tags = extractTags()


    override fun toString(): String {
        return "${tags.joinToString(" - ")} - $name"
    }
}
