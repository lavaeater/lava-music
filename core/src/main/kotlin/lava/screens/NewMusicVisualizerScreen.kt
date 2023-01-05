package lava.screens

import com.badlogic.gdx.scenes.scene2d.Stage
import eater.core.MainGame
import eater.screens.ScreenWithStage
import ktx.actors.stage
import ktx.scene2d.actors
import ktx.scene2d.label
import lava.injection.Assets
import lava.music.SamplersManager

class NewMusicVisualizerScreen(
    game: MainGame,
    private val assets: Assets,
    private val samplersManager: SamplersManager
) : ScreenWithStage(game, assets.colors["blueish"]!!) {
    override val stage: Stage by lazy {
        val s = stage(batch, viewport)
        s.actors {
            label("Wuu")
        }
        s
    }
}
