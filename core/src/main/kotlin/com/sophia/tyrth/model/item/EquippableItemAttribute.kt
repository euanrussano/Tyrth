package com.sophia.tyrth.model.item

import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.hero.Hero

class EquippableItemAttribute(
    val itemSlot : EquipItemSlot
) : ItemAttribute {
    override fun applyEffectOn(itemInstance: ItemInstance, entity: Entity) {
        if (entity is Hero){
            entity.equip(itemInstance, itemSlot)
        }
    }
}
