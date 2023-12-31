package com.sophia.tyrth.infrastructure.factory.world

import com.badlogic.gdx.Gdx
import com.sophia.tyrth.infrastructure.factory.hero.HeroFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.monster.MonsterInstance

class WorldFactory(
    val tilemapFactory: TilemapFactory,
    val heroFactory: HeroFactory,
    val monsterRepository: MonsterRepository,
) {

    fun build() : World {
        val (tilemap, spawnPoints) = tilemapFactory.build()
        println(spawnPoints)
        val hero = heroFactory.createHero()
        hero.position = spawnPoints.first()

        val rat = monsterRepository.findByName("Rat")
        val monsterInstances = mutableListOf<MonsterInstance>()
        for ((x, y) in spawnPoints.drop(1)){
            val monsterInstance = MonsterInstance(rat, x to y)
            monsterInstances.add(monsterInstance)
        }

        return World(tilemap, hero, monsterInstances)
    }
}
