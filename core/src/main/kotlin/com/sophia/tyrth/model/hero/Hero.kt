package com.sophia.tyrth.model.hero

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.HeroAction
import com.sophia.tyrth.model.Entity

class Hero(
    position: Pair<Int, Int>,
    val heroPreFab: HeroPreFab
) : Entity(position){

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

}
