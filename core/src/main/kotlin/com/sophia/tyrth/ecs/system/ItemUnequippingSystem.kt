package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.GameLog
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
        val item = wantsToUnequip.item

        entity.remove<WantsToUnequipItemComponent>()

        HeroComponent.ID[entity]?.let {
            val name = NameComponent.ID[item].name
            GameLog.entries.add("You unequip $name")
        }

        // if entity has a backpack, first try to insert the item into the backpack
        BackpackComponent.ID[entity]?.let {backpack ->
            if (backpack.currentWeight >= backpack.maxWeight) return@let

            item.remove<EquippedComponent>()
            item += InBackpackComponent().apply { this.backpackID = backpack.ID }
            return
        }


        item.remove<EquippedComponent>()
        entity += WantsToDropItemComponent().apply { this.item = item }



        // otherwise simply drop the item
    }

}
