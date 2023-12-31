package com.sophia.tyrth.model.manager

import com.sophia.tyrth.TilemapUtils
import com.sophia.tyrth.infrastructure.factory.tilemap.DungeonTilemapFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.SimpleTilemapFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
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
        val factory = TilemapUtils.random()
        val (tilemap, heroPosition, spawnPoints, itemSpawnPoints) = factory.build()

        world.tilemaps.add(tilemap)
        world.currentDepth += 1

        val hero = world.hero
        hero.position = heroPosition

        val monsterInstances = mutableListOf<MonsterInstance>()
        for ((x, y) in spawnPoints.drop(1)){
            val monster = monsterRepository.random()
            val monsterInstance = MonsterInstance(monster, x to y)
            monsterInstances.add(monsterInstance)
        }

        val itemInstances = mutableListOf<ItemInstance>()
        for ((x, y) in itemSpawnPoints){
            val item = itemRepository.random()
            val itemInstance = ItemInstance(item, x to y)
            itemInstances.add(itemInstance)
        }

    }

}
