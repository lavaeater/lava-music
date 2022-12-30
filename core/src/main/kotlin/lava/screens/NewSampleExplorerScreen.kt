package lava.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import eater.core.MainGame
import eater.core.SelectedItemList
import eater.core.selectedItemListOf
import eater.input.command
import eater.screens.ScreenWithStage
import ktx.actors.onChange
import ktx.actors.onKeyDown
import ktx.actors.stage
import ktx.assets.toExternalFile
import ktx.collections.toGdxArray
import ktx.scene2d.*
import lava.injection.Assets

class NewSampleExplorerScreen(
    game: MainGame,
    private val assets: Assets
) : ScreenWithStage(game, assets.colors["blueish"]!!) {
    private var sampleBaseDir = "projects/games/music-samples-explorer"
    private val beforeAndAfter = 15
    private lateinit var listWidgetSample: KListWidget<SampleFile>

    private fun selectedItemUpdated(newIndex: Int, sampleFile: SampleFile) {

        /**
         * Simply get the next n items from this index
         */
        val tempList = getItems(newIndex)
        listWidgetSample.setItems(tempList.toGdxArray())
        listWidgetSample.selected = sampleFile
    }

    private fun getItems(selectedIndex: Int): List<SampleFile> {
        return allSamples.getNItemsBeforeAndAfterIndex(beforeAndAfter, selectedIndex)
    }

    private val allSamples: SelectedItemList<SampleFile> = selectedItemListOf(::selectedItemUpdated)


    private fun getAllSamples() {
        allSamples.clear()
        getSamplesRecursive(allSamples, sampleBaseDir)
    }

    private fun getSamplesRecursive(sampleList: SelectedItemList<SampleFile>, directoryPath: String) {
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
        val staaage = stage(batch, viewport).apply {
            actors {
                container {
                    pad(25f)
                    listWidgetSample =
                        listWidgetOf(getItems(0).toGdxArray(), "samplestyle").apply {
                            alignment = Align.left
                            color = assets.colors["dark"]!!
                            onChange {
                                allSamples.selectedItem = this.selected
                            }
                            onKeyDown {
                                when(it) {
                                    Input.Keys.PAGE_DOWN -> this.selectedIndex -= 15
                                    Input.Keys.PAGE_UP -> this.selectedIndex += 15
                                    Input.Keys.ENTER -> tryToPlaySample(this.selected, this.selectedIndex)
                                 }
                            }
                        }
                    setFillParent(true)
                    pack()
                }
            }
        }
        commandMap = command("commands") {
//            setDown(Input.Keys.DOWN, "next item") { listWidgetSample.selectedIndex++ }
//            setDown(Input.Keys.UP, "previous item") { listWidgetSample.selectedIndex-- }
//            setDown(Input.Keys.PAGE_UP, "previous item") { listWidgetSample.selectedIndex-=15 }
//            setDown(Input.Keys.PAGE_DOWN, "previous item") { listWidgetSample.selectedIndex+=15 }
        }
        Gdx.input.inputProcessor = staaage
        listWidgetSample.layout()
        staaage
    }

    private val samples = mutableMapOf<SampleFile, Sound>()
    private fun tryToPlaySample(selected: SampleFile, selectedIndex: Int) {
        if(!samples.containsKey(selected)) {
            try {
                samples[selected] = Gdx.audio.newSound(selected.path.toExternalFile())
            } catch (someException: Exception) {
                samples.remove(selected)
                allSamples.remove(selected)
                listWidgetSample.selectedIndex += 1
            }
        }
        samples[selected]?.play()
    }
}
