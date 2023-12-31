package com.sophia.tyrth.infrastructure.factory.item

import com.sophia.tyrth.Assets
import com.sophia.tyrth.infrastructure.repository.item.ItemRepository
import com.sophia.tyrth.model.item.ConsumableItemAttribute
import com.sophia.tyrth.model.item.HealingItemAttribute
import com.sophia.tyrth.model.item.Item
import com.sophia.tyrth.model.item.ItemTexturePreFab

class InMemoryItemFactory(
    itemRepository: ItemRepository
) {

    init {
        itemRepository.entities.addAll(listOf(
            Item("Health Potion", ItemTexturePreFab(Assets.tilesheet[13][32])).apply {
                itemAttributes.addAll(listOf(
                    HealingItemAttribute(8),
                    ConsumableItemAttribute()
                ))
            }
        ))

    }

}
