package com.sophia.tyrth.model.manager

import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.World

class MovementManager(val world: World) {

    fun update(delta: Float) {
        for (entity in world.entities){
            update(entity)
        }
    }

    private fun update(entity: Entity) {
        val (x, y) = entity.position
        val (dx, dy) = entity.velocity
        entity.position = x + dx to y + dy

        entity.velocity = 0 to 0
    }

}
