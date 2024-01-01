package com.sophia.tyrth.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegraph
import com.sophia.tyrth.Messages
import com.sophia.tyrth.model.item.EquipItemSlot
import com.sophia.tyrth.model.item.ItemInstance
import kotlin.math.min

abstract class Entity(
    var position : Pair<Int, Int>,
    viewRange : Int,
    var maxHP : Int,
    var power : Int,
    var defense : Int,
    var backpack : Backpack? = null
) : Telegraph{

    val itemSlotMap : MutableMap<EquipItemSlot, ItemInstance?> = mutableMapOf()

    var hp = maxHP
        private set

    var velocity = 0 to 0
    var fieldOfView = FieldOfView(viewRange)

    init {
        backpack?.owner = this
    }

    fun takeDamage(damage : Int){
        if (damage < 0){
            Gdx.app.log(this::class.simpleName, "Negative damage")
            return
        }
        hp -= damage
        MessageManager.getInstance().dispatchMessage(this, Messages.HEALTH_CHANGED)
    }

    fun heal(amount: Int){
        if (amount < 0){
            Gdx.app.log(this::class.simpleName, "Negative healing amount")
            return
        }
        println(amount)
        val realAmount = min(maxHP - hp, amount)
        println(realAmount)
        hp += realAmount
        MessageManager.getInstance().dispatchMessage(this, Messages.HEALTH_CHANGED)
    }

    fun equip(itemInstance: ItemInstance, itemSlot: EquipItemSlot) {
        // if there is already an item in the slot, put it back in the backpack (or drop it)
        itemSlotMap[itemSlot]?.let { unequip(it, itemSlot) }

        itemSlotMap[itemSlot] = itemInstance
        backpack?.remove(itemInstance)
        MessageManager.getInstance().dispatchMessage(this, Messages.EQUIPMENT_CHANGED)
    }

    fun unequip(itemInstance: ItemInstance, itemSlot: EquipItemSlot) {
        val currentItemEquipped = itemSlotMap[itemSlot]
        if (currentItemEquipped != itemInstance) return

        val added = backpack?.addItemInstance(itemInstance) ?: false
        if (!added){
            itemInstance.position = position
        }
        itemSlotMap[itemSlot] = null
        MessageManager.getInstance().dispatchMessage(this, Messages.EQUIPMENT_CHANGED)
    }

}
