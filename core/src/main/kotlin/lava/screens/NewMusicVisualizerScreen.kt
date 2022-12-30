package lava.screens

import com.badlogic.gdx.scenes.scene2d.Stage
import eater.core.MainGame
import eater.screens.ScreenWithStage
import lava.injection.Assets
import lava.music.SamplersManager

class NewMusicVisualizerScreen(
    game: MainGame,
    private val assets: Assets,
    private val samplersManager: SamplersManager
) : ScreenWithStage(game, assets.colors["blueish"]!!) {
    override val stage: Stage
        get() = TODO("Not yet implemented")
}
