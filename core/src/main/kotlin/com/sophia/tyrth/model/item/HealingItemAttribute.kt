package com.sophia.tyrth.model.item

import com.sophia.tyrth.model.Entity
import kotlin.math.min

class HealingItemAttribute(val amount : Int) : ItemAttribute {

    override fun applyEffectOn(itemInstance: ItemInstance, entity: Entity) {
        entity.heal(amount)
    }
}
