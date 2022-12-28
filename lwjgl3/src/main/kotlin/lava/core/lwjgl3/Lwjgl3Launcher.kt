@file:JvmName("Lwjgl3Launcher")

package lava.core.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import lava.core.LavaMusic

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(LavaMusic(), Lwjgl3ApplicationConfiguration().apply {
        disableAudio(true)
        setTitle("lava-music")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
