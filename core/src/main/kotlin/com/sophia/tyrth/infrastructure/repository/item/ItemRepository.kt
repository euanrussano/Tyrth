package com.sophia.tyrth.infrastructure.repository.item

import com.sophia.tyrth.infrastructure.repository.Repository
import com.sophia.tyrth.model.item.Item

class ItemRepository : Repository<Item>(){

    override fun getName(it: Item): String {
        return it.name
    }

}
