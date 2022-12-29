package lava.injection

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Bitmap
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import eater.core.GameSettings
import eater.core.toColor
import eater.injection.InjectionContext
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.scene2d.Scene2DSkin
import ktx.style.list
import kotlin.reflect.typeOf

fun assets(): Assets {
    return InjectionContext.inject()
}

class Assets(private val gameSettings: GameSettings, val colors: Map<String, Color> = mapOf(
    "dark" to "414858".toColor(),
    "light" to "C7CCD4".toColor(),
    "reddish" to "80585B".toColor(),
    "oxenblood" to "674F5C".toColor(),
    "blueish" to "A8C0C8".toColor()
)
) : DisposableRegistry by DisposableContainer() {

    init {

        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile()).apply {
            list("samplestyle", "default") {
                fontColorUnselected = colors["dark"]
                fontColorSelected = colors["light"]
            }
        }
    }
    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}
