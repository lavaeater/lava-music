package lava.core

import twodee.core.MainGame
import twodee.injection.InjectionContext.Companion.inject
import lava.injection.Context
import lava.screens.MusicVisualizerScreen
import lava.screens.NewSampleExplorerScreen
import lava.screens.SampleExplorerScreen

class LavaMusic : MainGame() {

    override fun goToGameSelect() {
        setScreen<MusicVisualizerScreen>()
    }

    override fun goToGameScreen() {
        setScreen<SampleExplorerScreen>()
    }

    override fun goToGameOver() {
        TODO("Not yet implemented")
    }

    override fun gotoGameVictory() {
        TODO("Not yet implemented")
    }

    override fun create() {
        Context.initialize(this, false)
        addScreen(inject<MusicVisualizerScreen>())
        addScreen(inject<SampleExplorerScreen>())
        goToGameSelect()
    }
}
