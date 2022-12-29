package lava.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import eater.core.MainGame
import eater.input.command
import eater.screens.ScreenWithStage
import ktx.actors.stage
import ktx.collections.toGdxArray
import ktx.scene2d.*
import lava.injection.Assets

class NewSampleExplorerScreen(
    game: MainGame,
    private val assets: Assets
) : ScreenWithStage(game, assets.colors["blueish"]!!) {
    private var sampleBaseDir = "projects/games/music-samples-explorer"
    private val allSamples = mutableListOf<SampleFile>()

    fun getAllSamples() {
        allSamples.clear()
        getSamplesRecursive(allSamples, sampleBaseDir)
    }

    private fun getSamplesRecursive(sampleList: MutableList<SampleFile>, directoryPath: String) {
        for (file in Gdx.files.external(directoryPath).list()) {
            if (file.isDirectory)
                getSamplesRecursive(sampleList, file.path())
            else if (file.extension() == "wav") {
                sampleList.add(SampleFile(file.nameWithoutExtension(), file.path(), sampleBaseDir.split("/")))
            }
        }
    }

    override val stage: Stage by lazy {
        getAllSamples()
        lateinit var listWidgetSample: KListWidget<SampleFile>
        val staaage = stage(batch, viewport).apply {
            actors {
                container {
                    pad(25f)
                    scrollPane {
                        listWidgetSample =
                            listWidgetOf(allSamples.toTypedArray().toGdxArray(), "samplestyle").apply {
                                alignment = Align.left
                                color = assets.colors["dark"]!!
                            }
                        layout()
                        setForceScroll(false, true)
                    }
                    setFillParent(true)
                    pack()
                }
            }
        }
        commandMap = command("commands") {
//            setDown(Input.Keys.DOWN, "next item") { listWidgetSample.selectedIndex++ }
//            setDown(Input.Keys.UP, "previous item") { listWidgetSample.selectedIndex-- }
        }
        Gdx.input.inputProcessor = staaage
        listWidgetSample.layout()
        staaage
    }

    override fun show() {
        super.show()
    }
}
