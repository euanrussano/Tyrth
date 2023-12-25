package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.GameLog
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
                GameLog.entries.add("You consumed the $itemName, healing $realAmountHealed hp.")
            }
        }
        EquippableComponent.ID[item]?.let { equippableComponent ->
            val slot = equippableComponent.slot

            val backpack = BackpackComponent.ID[entity]
            entity.remove<InBackpackComponent>()
            backpack.currentWeight = engine.getEntitiesFor(allOf(InBackpackComponent::class).get())
                .filter { entity -> InBackpackComponent.ID[entity].backpackID == backpack.ID }.size

            // check if is already equipped the same slot. If yes, try to put the item in the
            // backpack (drop if not possible) and then equip the new item
            for (entity2 in engine.getEntitiesFor(allOf(EquippedComponent::class).get())){
                val equipped = EquippedComponent.ID[entity2]
                if (equipped.owner != entity) continue
                val equippable = EquippableComponent.ID[entity2]
                if (equippable.slot == slot){
                    // if the entity is already equipping another item in the same slot,
                    // remove that item from the slot, put it back in the backpack
                    entity2.remove<EquippedComponent>()
                    val name = NameComponent.ID[entity2].name
                    HeroComponent.ID[entity]?.let {
                        GameLog.entries.add("You unequip $name")
                    }

                    if (backpack.currentWeight >= backpack.maxWeight){
                        // if backpack is full, drop the item
                        HeroComponent.ID[entity]?.let {
                            GameLog.entries.add("Unable to insert item in backpack.")
                        }
                        entity += WantsToDropItemComponent().apply { this.item = entity2 }
                    } else {
                        entity += InBackpackComponent().apply { this.backpackID = backpack.ID }
                    }
                    break
                }
            }

            item += EquippedComponent().apply { owner = entity }
            item.remove<InBackpackComponent>()

            val name = NameComponent.ID[item].name
            HeroComponent.ID[entity]?.let {
                GameLog.entries.add("You equip $name")
            }


        }

        ConsumableComponent.ID[item]?.let {
            val backpack = BackpackComponent.ID[entity]
            backpack.currentWeight -= 1
            engine.removeEntity(item)
        }

        entity.remove<WantsToUseItemComponent>()
    }

}
