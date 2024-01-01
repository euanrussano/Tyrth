package com.sophia.tyrth.infrastructure.factory.item

import com.sophia.tyrth.Assets
import com.sophia.tyrth.infrastructure.repository.item.ItemRepository
import com.sophia.tyrth.model.item.*

class InMemoryItemFactory(
    itemRepository: ItemRepository
) {

    init {
        itemRepository.entities.addAll(listOf(
            Item("Health Potion", ItemTexturePreFab(Assets.tilesheet[13][32]), mutableListOf(
                    HealingItemAttribute(8),
                    ConsumableItemAttribute()
                )
            ),
            Item("Dagger", ItemTexturePreFab(Assets.tilesheet[6][34]), mutableListOf(
                    EquippableItemAttribute(EquipItemSlot.MELEE)
                ),
                MeleeItemCombatBonus(2)
            ),
        ))

    }

}
