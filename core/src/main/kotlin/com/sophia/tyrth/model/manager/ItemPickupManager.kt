package com.sophia.tyrth.model.manager

import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.Messages
import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.hero.Hero

class ItemPickupManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        for (entity in world.entities.filter { entity -> entity.backpack != null }){
            update(entity)
        }
    }

    private fun update(entity: Entity) {
        for (itemInstance in world.itemInstances){
            if (itemInstance.position != entity.position) continue

            val added = entity.backpack!!.addItemInstance(itemInstance)
            if (added && entity is Hero){
                GameLog.add("You pick up the ${itemInstance.item.name}")
                MessageManager.getInstance().dispatchMessage(entity, Messages.INVENTORY_CHANGED)
            }
            return
        }
    }

}
