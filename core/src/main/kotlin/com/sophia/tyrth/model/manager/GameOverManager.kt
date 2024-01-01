package com.sophia.tyrth.model.manager

import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.Messages
import com.sophia.tyrth.model.World

class GameOverManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        val hero = world.hero
        if (hero.hp <= 0){
            world.isGameOver = true
            MessageManager.getInstance().dispatchMessage(Messages.GAME_OVER)
        }
    }


}
