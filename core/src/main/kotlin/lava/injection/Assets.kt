package lava.injection

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import eater.core.GameSettings
import eater.injection.InjectionContext
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.scene2d.Scene2DSkin

fun assets(): Assets {
    return InjectionContext.inject()
}

class Assets(private val gameSettings: GameSettings) : DisposableRegistry by DisposableContainer() {
    init {

        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())
    }

    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}
