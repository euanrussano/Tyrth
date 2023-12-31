package com.sophia.tyrth.model.hero

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.HeroAction
import com.sophia.tyrth.model.Backpack
import com.sophia.tyrth.model.Entity

class Hero(
    val name : String,
    position: Pair<Int, Int>,
    val heroPreFab: HeroPreFab,
    backpack : Backpack
) : Entity(position, 4, 30, 5, 2, backpack){

    fun update(delta : Float, heroAction: HeroAction){
        when(heroAction){
            HeroAction.NONE -> {}
            HeroAction.MOVE_UP -> velocity = 0 to 1
            HeroAction.MOVE_DOWN -> velocity = 0 to -1
            HeroAction.MOVE_LEFT -> velocity = -1 to 0
            HeroAction.MOVE_RIGHT -> velocity = 1 to 0
        }


    }

    fun draw(batch: SpriteBatch) {
        heroPreFab.draw(batch, position.first, position.second)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        return false
    }

}
