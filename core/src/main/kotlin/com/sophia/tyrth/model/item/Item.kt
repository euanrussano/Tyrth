package com.sophia.tyrth.model.item

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.model.Entity

class Item(
    val name : String,
    val itemPreFab : ItemPreFab,
    val itemAttributes:  MutableList<ItemAttribute>,
    val itemCombatBonus : ItemCombatBonus? = null
) {


    fun draw(batch: SpriteBatch, x: Int, y: Int) {
        itemPreFab.draw(batch, x, y)
    }

    fun applyEffectOn(itemInstance: ItemInstance, entity: Entity) {
        itemAttributes.forEach { it.applyEffectOn(itemInstance, entity) }
    }

}
