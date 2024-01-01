package com.sophia.tyrth.infrastructure.factory.world

import com.sophia.tyrth.infrastructure.factory.LevelFactory
import com.sophia.tyrth.infrastructure.factory.hero.HeroFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
import com.sophia.tyrth.infrastructure.repository.item.ItemRepository
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.model.monster.MonsterInstance

class WorldFactory(
    val heroFactory: HeroFactory,
) {

    fun build() : World {
        val (tilemap, heroPosition, monsterInstances, itemInstances) = LevelFactory.createLevel(0)

        val hero = heroFactory.createHero()
        hero.position = heroPosition

        return World(mutableListOf(tilemap) , hero, mutableListOf(monsterInstances) , mutableListOf(itemInstances))
    }
}
