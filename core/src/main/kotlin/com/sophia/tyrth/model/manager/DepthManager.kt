package com.sophia.tyrth.model.manager

import com.sophia.tyrth.infrastructure.factory.LevelFactory
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.model.monster.MonsterInstance

class DepthManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        val downStairTiles = world.tilemap.tiles.flatten().filter {
            it.terrain.isDepthIncrease && it.x == world.hero.position.first && it.y == world.hero.position.second
        }

        if (downStairTiles.isNotEmpty()){
            goDown()
        }
    }

    private fun goDown() {
        world.currentDepth += 1

        // if there is no tilemap for this depth, generate a new one
        if (world.currentDepth in world.tilemaps.indices) return

        val (tilemap, heroPosition, monsterInstances, itemInstances) = LevelFactory.createLevel(world.currentDepth)

        world.tilemaps.add(tilemap)
        world.monsterInstancesByDepth.add(monsterInstances)
        world.itemInstancesByDepth.add(itemInstances)
        world.hero.position = heroPosition
        world.hero.fieldOfView.clear()



    }

}
