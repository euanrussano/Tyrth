package com.sophia.tyrth.infrastructure.factory.world

import com.sophia.tyrth.infrastructure.factory.hero.HeroFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
import com.sophia.tyrth.infrastructure.repository.item.ItemRepository
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.model.monster.MonsterInstance

class WorldFactory(
    val tilemapFactory: TilemapFactory,
    val heroFactory: HeroFactory,
    val monsterRepository: MonsterRepository,
    val itemRepository : ItemRepository
) {

    fun build() : World {
        val (tilemap, heroPosition, spawnPoints, itemSpawnPoints) = tilemapFactory.build()

        val hero = heroFactory.createHero()
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

        return World(mutableListOf(tilemap) , hero, monsterInstances, itemInstances)
    }
}
