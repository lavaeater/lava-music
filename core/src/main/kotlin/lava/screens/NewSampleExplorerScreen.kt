package lava.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import twodee.core.MainGame
import twodee.core.SelectedItemList
import twodee.core.selectedItemListOf
import twodee.input.KeyPress
import twodee.input.command
import twodee.screens.ScreenWithStage
import ktx.actors.onChange
import ktx.actors.onKeyDown
import ktx.actors.stage
import ktx.assets.toExternalFile
import ktx.collections.toGdxArray
import ktx.scene2d.*
import lava.injection.Assets
import lava.music.SamplersManager
import lava.music.SimpleSampler
import lava.music.SoundScheduler

class NewSampleExplorerScreen(
    game: MainGame,
    private val assets: Assets,
    private val samplersManager: SamplersManager,
    private val soundScheduler: SoundScheduler,
    camera: OrthographicCamera,
    batch: PolygonSpriteBatch
) : ScreenWithStage(game, ExtendViewport(400f, 600f, camera), camera, batch) {
    private var sampleBaseDir = "projects/games/music-samples-explorer"
    private val beforeAndAfter = 15
    private lateinit var samplesList: KListWidget<SampleFile>
    private lateinit var samplersList: KListWidget<SimpleSampler>

    private fun selectedItemUpdated(newIndex: Int, sampleFile: SampleFile) {

        /**
         * Simply get the next n items from this index
         */
        val tempList = getItems(newIndex)
        samplesList.setItems(tempList.toGdxArray())
        samplesList.selected = sampleFile
    }

    private fun getItems(selectedIndex: Int): List<SampleFile> {
        return if(allSamples.any()) allSamples.getNItemsBeforeAndAfterIndex(beforeAndAfter, selectedIndex) else emptyList()
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
                table {
                    pad(25f)
                    samplesList =
                        listWidgetOf(getItems(0).toGdxArray(), "samplestyle").apply {
                            alignment = Align.left
                            color = assets.colors["dark"]!!
                            onChange {
                                allSamples.selectedItem = this.selected
                            }
                            onKeyDown {
                                commandMap.execute(it, KeyPress.Down)
                            }
                        }
                    setFillParent(true)
                    pack()
                }.apply {
                    align(Align.left)
                }
                table {
                    pad(25f)
                    samplersList = listWidgetOf<SimpleSampler>(emptyArray<SimpleSampler>().toGdxArray(), "samplestyle").apply {
                        alignment = Align.left
                        color = assets.colors["dark"]!!
                    }
                    setFillParent(true)
                    pack()
                }.apply {
                    align(Align.right)
                }
            }
        }
        commandMap = command("commands") {
            setDown(Input.Keys.DOWN, "next item") { samplesList.selectedIndex++ }
            setDown(Input.Keys.UP, "previous item") { samplesList.selectedIndex-- }
            setDown(Input.Keys.PAGE_UP, "previous item") { samplesList.selectedIndex -= 15 }
            setDown(Input.Keys.PAGE_DOWN, "previous item") { samplesList.selectedIndex += 15 }
            setDown(Input.Keys.SPACE, "play item") {tryToPlaySample(samplesList.selected, samplesList.selectedIndex)}
            setDown(Input.Keys.ENTER, "play item") { addSampler(samplesList.selected, samplesList.selectedIndex)}
            setDown(Input.Keys.V, "go to visualizer") { mainGame.goToGameScreen()}
//            setDown(Input.Keys.B, "choose base folder") {
//                scene2d.dialog("Enter root folder, below ${Gdx.files.externalStoragePath}") {
//                    titleTable.addActor(scene2d.table {
//                        label("Enter root folder, below ${Gdx.files.externalStoragePath}").cell(
//                            grow = true,
//                            align = Align.center
//                        )
//                        setFillParent(true)
//                    })
//
//                    contentTable.addActor(scene2d.table {
//                        textField(sampleBaseDir).cell(grow = true)
//                        row()
//                        setFillParent(true)
//                    })
//                    buttonTable.addActor(scene2d.horizontalGroup {
//                        button {
//                            label("what")
//                        }
//                        setFillParent(true)
//                    })
//                }.apply {
//                    show(staaage)
//                }
//
//            }
        }
//        Gdx.input.inputProcessor = staaage
        samplesList.layout()
        staaage
    }

    override fun renderBatch(delta: Float) {
        //No op for you!
    }

    private fun addSampler(sampleFile: SampleFile, selectedIndex: Int) {
        if(!samplersManager.samplers.containsKey(sampleFile))
            samplersManager.samplers[sampleFile] = SimpleSampler("${sampleFile.tags.last()}-${sampleFile.name}", Gdx.audio.newSound(sampleFile.path.toExternalFile()),soundScheduler)

        samplersList.setItems(samplersManager.samplers.values.toGdxArray())
    }

    private val samples = mutableMapOf<SampleFile, Sound>()
    private fun tryToPlaySample(selected: SampleFile, selectedIndex: Int) {
        if (!samples.containsKey(selected)) {
            try {
                samples[selected] = Gdx.audio.newSound(selected.path.toExternalFile())
            } catch (someException: Exception) {
                samples.remove(selected)
                allSamples.remove(selected)
                samplesList.selectedIndex += 1
            }
        }
        samples[selected]?.play()
    }
}
