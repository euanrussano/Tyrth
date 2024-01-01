package com.sophia.tyrth.infrastructure.factory

import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
import com.sophia.tyrth.infrastructure.repository.item.ItemRepository
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.model.monster.MonsterInstance
import com.sophia.tyrth.model.tilemap.Tilemap

object LevelFactory {

    private lateinit var itemRepository: ItemRepository
    private lateinit var monsterRepository: MonsterRepository
    private lateinit var tilemapFactory: TilemapFactory

    fun initialize(tilemapFactory: TilemapFactory, monsterRepository: MonsterRepository, itemRepository: ItemRepository){
        this.tilemapFactory = tilemapFactory
        this.monsterRepository = monsterRepository
        this.itemRepository = itemRepository
    }

    fun createLevel(depth : Int): LevelData {
        val (tilemap, heroPosition, spawnPoints, itemSpawnPoints) = tilemapFactory.build(depth)

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

        return LevelData(tilemap, heroPosition, monsterInstances, itemInstances)
    }

    data class LevelData(
        val tilemap : Tilemap,
        val heroPosition : Pair<Int, Int>,
        val monsterInstances : MutableList<MonsterInstance>,
        val itemInstances : MutableList<ItemInstance>
    )

}
