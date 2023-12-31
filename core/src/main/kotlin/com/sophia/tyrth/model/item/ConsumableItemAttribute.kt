package com.sophia.tyrth.model.item

import com.sophia.tyrth.model.Entity

class ConsumableItemAttribute : ItemAttribute {

    override fun applyEffectOn(itemInstance: ItemInstance, entity: Entity) {
        entity.backpack?.remove(itemInstance)
    }
}
