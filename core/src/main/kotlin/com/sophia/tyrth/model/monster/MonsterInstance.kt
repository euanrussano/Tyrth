package com.sophia.tyrth.model.monster

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.model.Entity

class MonsterInstance(val monster : Monster, position : Pair<Int, Int>) : Entity(position){

    fun update(delta: Float) {
        monster.update(delta, this)
    }

    fun draw(batch: SpriteBatch) {
        monster.draw(batch, position.first, position.second)
    }

}
