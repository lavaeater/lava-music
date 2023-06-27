package lava.core

import twodee.core.MainGame
import twodee.injection.InjectionContext.Companion.inject
import lava.injection.Context
import lava.screens.MusicVisualizerScreen
import lava.screens.NewSampleExplorerScreen

class LavaMusic : MainGame() {

    override fun goToGameSelect() {
        setScreen<NewSampleExplorerScreen>()
    }

    override fun goToGameScreen() {
        setScreen<MusicVisualizerScreen>()
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
        addScreen(inject<NewSampleExplorerScreen>())
        goToGameSelect()
    }
}
