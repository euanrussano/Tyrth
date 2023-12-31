package com.sophia.tyrth.model.item

import com.sophia.tyrth.model.Entity

interface ItemAttribute {

    fun applyEffectOn(itemInstance: ItemInstance, entity: Entity)


}
