package com.sophia.tyrth.model.manager

import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.World

class CollisionManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        for (entity in world.entities) {
            update(entity)
        }

    }

    private fun update(entity: Entity){
        val (x, y) = entity.position
        val (dx, dy) = entity.velocity

        // if not moving, not need to check collision
        if (dx to dy == 0 to 0) return

        if (x + dx !in world.tilemap.tiles.indices || y + dy !in world.tilemap.tiles[0].indices){
            entity.velocity = 0 to 0
            return
        }

        // check collision on map
        val tile = world.tilemap.tiles[x + dx][y + dy]
        if (tile.terrain.isWall){
            entity.velocity = 0 to 0
            return
        }

        // check collision against other entities
        for (entity2 in world.entities){
            if (entity2 == entity) continue
            if (x + dx to y + dy == entity2.position){
                entity.velocity = 0 to 0
                return
            }
        }
    }

}
