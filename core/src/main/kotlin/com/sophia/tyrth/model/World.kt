package com.sophia.tyrth.model

import com.sophia.tyrth.HeroAction
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.manager.CollisionManager
import com.sophia.tyrth.model.manager.MovementManager
import com.sophia.tyrth.model.tilemap.Tilemap
import com.sophia.tyrth.model.monster.MonsterInstance

class World(
    val tilemap: Tilemap,
    val hero : Hero,
    val monsterInstances : MutableList<MonsterInstance>
) {

    val entities: List<Entity>
        get() = listOf(hero).plus(monsterInstances)

    val collisionManager = CollisionManager(this)
    val movementManager = MovementManager(this)

    fun update(delta: Float, heroAction: HeroAction) {
        hero.update(delta, heroAction)
        monsterInstances.forEach { it.update(delta) }

        collisionManager.update(delta)
        movementManager.update(delta)


    }

}
