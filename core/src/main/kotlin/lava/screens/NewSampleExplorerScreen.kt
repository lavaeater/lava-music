package lava.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import eater.core.MainGame
import eater.core.toColor
import eater.screens.ScreenWithStage
import ktx.actors.stage
import ktx.collections.toGdxArray
import ktx.scene2d.actors
import ktx.scene2d.container
import ktx.scene2d.listWidgetOf

class NewSampleExplorerScreen(
    game: MainGame,
    val colors: Map<String, Color> = arrayOf(
        "414858",
        "C7CCD4",
        "80585B",
        "674F5C",
        "A8C0C8"
    ).associateWith { it.toColor() }
) : ScreenWithStage(game, colors["A8C0C8"]!!) {
    var sampleBaseDir = "projects/games/music-samples-explorer"

    val allSamples = mutableListOf<SampleFile>()

    fun getAllSamples() {
        allSamples.clear()
        getSamplesRecursive(allSamples, sampleBaseDir)
    }

    private fun getSamplesRecursive(sampleList: MutableList<SampleFile>, directoryPath: String) {
        for (file in Gdx.files.external(directoryPath).list()) {
            if (file.isDirectory)
                getSamplesRecursive(sampleList, file.path())
            else if (file.extension() == "wav") {
                sampleList.add(SampleFile(file.nameWithoutExtension(), file.path()))
            }
        }
    }


    override val stage: Stage by lazy {
        getAllSamples()
        stage(batch, viewport).apply {
            actors {
                container {
                    listWidgetOf(allSamples.toTypedArray().toGdxArray()).apply {
                        alignment = Align.center

                    }
                    setFillParent(true)
                    pack()
                }
            }
        }
    }

}
