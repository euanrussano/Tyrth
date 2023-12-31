package com.sophia.tyrth.model.item

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.model.Entity

class ItemInstance(
    val item : Item,
    var position : Pair<Int, Int>?
) {

    fun draw(batch: SpriteBatch) {
        position ?.let { (x, y) ->
            item.draw(batch, x, y)
        }
    }

    fun applyEffectOn(entity: Entity) {
        item.applyEffectOn(this, entity)
    }

}
