package com.sophia.tyrth

import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.sophia.tyrth.screen.WorldScreen
import ktx.assets.toInternalFile
import ktx.scene2d.Scene2DSkin

class TyrthGame : Game() {

    override fun create() {
        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())
        Assets.load()
        GameLog.entries.clear()

        setScreen(WorldScreen(this))
    }
}
