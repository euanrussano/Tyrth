package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.plusAssign
import ktx.ashley.remove

class ItemUnequippingSystem : IteratingSystem(
    allOf(
        WantsToUnequipItemComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val wantsToUnequip = WantsToUnequipItemComponent.ID[entity]
        val equipmentHold = EquipmentHolderComponent.ID[entity]

        val item = wantsToUnequip.item
        val equippable = EquippableComponent.ID[item]

        entity.remove<WantsToUnequipItemComponent>()

        equipmentHold.slots[equippable.slot] = null

        HeroComponent.ID[entity]?.let {
            val name = NameComponent.ID[item].name
            GameLog.add("You unequip $name")
            MessageManager.getInstance().dispatchMessage(Messages.HERO_EQUIPMENT_CHANGED)
        }


        // if entity has a backpack, first try to insert the item into the backpack
        BackpackComponent.ID[entity]?.let {backpack ->
            if (backpack.items.size >= backpack.maxWeight) return@let

            backpack.items.add(item)

            HeroComponent.ID[entity]?.let {
                MessageManager.getInstance().dispatchMessage(Messages.HERO_INVENTORY_CHANGED)
            }
            return
        }

        // otherwise simply drop the item
        entity += WantsToDropItemComponent().apply { this.item = item }




    }

}
