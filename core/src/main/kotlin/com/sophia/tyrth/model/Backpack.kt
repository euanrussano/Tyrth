package com.sophia.tyrth.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.Messages
import com.sophia.tyrth.model.item.ItemInstance

class Backpack(
    val maxWeight : Int = 1,
    var owner : Entity? = null
) {

    private val __itemInstances = mutableListOf<ItemInstance>()

    val itemInstances : List<ItemInstance>
        get() = __itemInstances


    fun addItemInstance(itemInstance: ItemInstance) : Boolean{
        if (__itemInstances.size >= maxWeight){
            Gdx.app.log(this::class.simpleName, "Cannot add item to full backpack")
            return false
        }

        __itemInstances.add(itemInstance)
        itemInstance.position = null
        owner?.let { MessageManager.getInstance().dispatchMessage(owner, Messages.INVENTORY_CHANGED) }
        return true

    }

    fun remove(itemInstance: ItemInstance) {
        __itemInstances.remove(itemInstance)
        owner?.let { MessageManager.getInstance().dispatchMessage(owner, Messages.INVENTORY_CHANGED) }
    }

}
