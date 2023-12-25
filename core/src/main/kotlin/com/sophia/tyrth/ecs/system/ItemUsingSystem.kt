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
import kotlin.math.min

class ItemUsingSystem : IteratingSystem(
    allOf(
        WantsToUseItemComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val wantsToUseItem = WantsToUseItemComponent.ID[entity]
        val item = wantsToUseItem.item
        val itemName = NameComponent.ID[item]?.name?:"<No Name>"

        ProvidesHealingComponent.ID[item]?.let {providesHealing ->
            val health = HealthComponent.ID[entity]?:return@let
            val realAmountHealed = min(health.maxHP - health.hp, providesHealing.healAmount)
            health.hp += realAmountHealed

            HeroComponent.ID[entity]?.let {
                GameLog.add("You consumed the $itemName, healing $realAmountHealed hp.")
            }
        }
        EquippableComponent.ID[item]?.let { equippableComponent ->
            val slot = equippableComponent.slot

            val backpack = BackpackComponent.ID[entity]
            val equipmentHold = EquipmentHolderComponent.ID[entity]

            backpack.items.remove(item)

            if (equipmentHold.slots[slot] != null){
                val item2 = equipmentHold.slots[slot]!!
                equipmentHold.slots[slot] = null

                val name = NameComponent.ID[item2].name
                HeroComponent.ID[entity]?.let {
                    GameLog.add("You unequip $name")
                }

                if (backpack.items.size >= backpack.maxWeight){
                    // if backpack is full, drop the item
                    HeroComponent.ID[entity]?.let {
                        GameLog.add("Unable to insert item in backpack.")
                    }
                    entity += WantsToDropItemComponent().apply { this.item = item2 }
                } else {
                    backpack.items.add(item2)
                }
            }

            equipmentHold.slots[slot] = item

            val name = NameComponent.ID[item].name
            HeroComponent.ID[entity]?.let {
                GameLog.add("You equip $name")
            }


        }

        ConsumableComponent.ID[item]?.let {
            val backpack = BackpackComponent.ID[entity]
            //backpack.currentWeight -= 1
            backpack.items.remove(item)
            engine.removeEntity(item)
        }

        entity.remove<WantsToUseItemComponent>()

        HeroComponent.ID[entity]?.let {
            MessageManager.getInstance().dispatchMessage(Messages.HERO_EQUIPMENT_CHANGED)
            MessageManager.getInstance().dispatchMessage(Messages.HERO_INVENTORY_CHANGED)
            MessageManager.getInstance().dispatchMessage(Messages.HERO_HEALTH_CHANGED)
        }
    }

}
