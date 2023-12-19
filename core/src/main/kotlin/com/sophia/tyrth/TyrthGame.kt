package com.sophia.tyrth

import com.badlogic.gdx.Game
import com.sophia.tyrth.screen.WorldScreen

class TyrthGame : Game() {

    override fun create() {
        Assets.load()

        setScreen(WorldScreen(this))
    }
}
