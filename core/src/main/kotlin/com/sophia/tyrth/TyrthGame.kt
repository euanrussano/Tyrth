package com.sophia.tyrth

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.screen.MainMenuScreen
import com.sophia.tyrth.screen.WorldScreen
import ktx.assets.toInternalFile
import ktx.scene2d.Scene2DSkin

class TyrthGame : Game() {

    lateinit var batch: Batch
    val UIViewport: Viewport = ExtendViewport(500f, 500f)

    val engine = PooledEngine()
    override fun create() {
        batch = SpriteBatch()

        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())
        Assets.load()
        GameLog.clear()

        setScreen(MainMenuScreen(this))
    }
}
