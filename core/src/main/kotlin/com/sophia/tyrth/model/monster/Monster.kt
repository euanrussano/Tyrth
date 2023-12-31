package com.sophia.tyrth.model.monster

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils

class Monster(
    val name : String,
    val monsterPreFab : MonsterPreFab,
) {
    val timeToThink = 1f
    var currentTimeUntilThink = timeToThink

    fun update(delta : Float, monsterInstance: MonsterInstance) {
        currentTimeUntilThink -= delta
        if (currentTimeUntilThink > 0f) return

        monsterInstance.velocity = MathUtils.random(-1, 1) to MathUtils.random(-1, 1)
        currentTimeUntilThink = timeToThink
    }

    fun draw(batch: SpriteBatch, x: Int, y: Int) {
        monsterPreFab.draw(batch, x, y)
    }

}
