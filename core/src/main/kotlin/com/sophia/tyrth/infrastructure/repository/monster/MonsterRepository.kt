package com.sophia.tyrth.infrastructure.repository.monster

import com.sophia.tyrth.infrastructure.repository.Repository
import com.sophia.tyrth.model.monster.Monster

class MonsterRepository : Repository<Monster>() {

    override fun getName(it: Monster): String {
        return it.name
    }

}
