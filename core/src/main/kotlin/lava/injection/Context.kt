package lava.injection

import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.core.GameSettings
import eater.core.MainGame
import eater.injection.InjectionContext
import ktx.assets.disposeSafely
import lava.music.SamplersManager
import lava.music.SoundScheduler
import lava.music.SoundsToPlayScheduler
import lava.screens.NewMusicVisualizerScreen
import lava.screens.NewSampleExplorerScreen
import space.earlygrey.shapedrawer.ShapeDrawer

object Context : InjectionContext() {
    private val shapeDrawerRegion: TextureRegion by lazy {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.drawPixel(0, 0)
        val texture = Texture(pixmap) //remember to dispose of later
        pixmap.disposeSafely()
        TextureRegion(texture, 0, 0, 1, 1)
    }

    fun initialize(game: MainGame, debugBox2d: Boolean) {
        buildContext {
            val gameSettings = GameSettings(256f)
            bindSingleton(gameSettings)
            bindSingleton(Assets(inject()))
            bindSingleton(game)
            bindSingleton(PolygonSpriteBatch())
            bindSingleton(OrthographicCamera())
            bindSingleton(
                ExtendViewport(
                    gameSettings.gameWidth,
                    gameSettings.gameHeight,
                    inject<OrthographicCamera>() as Camera
                )
            )
            bindSingleton(ShapeDrawer(inject<PolygonSpriteBatch>() as Batch, shapeDrawerRegion))
            bindSingleton(SoundsToPlayScheduler() as SoundScheduler)
            bindSingleton(SamplersManager())
            bindSingleton(NewMusicVisualizerScreen(inject(), inject(), inject()))
            bindSingleton(NewSampleExplorerScreen(inject(), inject(), inject(), inject()))
        }
    }
}
