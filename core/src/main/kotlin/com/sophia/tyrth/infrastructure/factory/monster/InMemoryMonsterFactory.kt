package com.sophia.tyrth.infrastructure.factory.monster

import com.sophia.tyrth.Assets
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.model.monster.Monster
import com.sophia.tyrth.model.monster.MonsterTexturePreFab

class InMemoryMonsterFactory(val monsterRepository: MonsterRepository) {

    init {
        monsterRepository.entities.addAll(
            listOf(
                Monster("Rat", 3, MonsterTexturePreFab(Assets.tilesheet[8][31]), 16,4,1),
                Monster("Scorpion", 3, MonsterTexturePreFab(Assets.tilesheet[5][24]),16,4,1)
            )
        )
    }

}
