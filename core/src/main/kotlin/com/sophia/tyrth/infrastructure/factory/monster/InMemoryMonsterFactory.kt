package com.sophia.tyrth.infrastructure.factory.monster

import com.sophia.tyrth.Assets
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.model.monster.Monster
import com.sophia.tyrth.model.monster.MonsterTexturePreFab

class InMemoryMonsterFactory(val monsterRepository: MonsterRepository) {

    init {
        monsterRepository.entities.addAll(
            listOf(
                Monster("Rat", MonsterTexturePreFab(Assets.tiles["rat"]!!))
            )
        )
    }

}
