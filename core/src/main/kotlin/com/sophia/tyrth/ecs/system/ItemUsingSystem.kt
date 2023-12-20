package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
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

        ConsumableComponent.ID[item]?.let {
            val backpack = BackpackComponent.ID[entity]
            backpack.items.remove(item)
            engine.removeEntity(item)
        }

        entity.remove<WantsToUseItemComponent>()
    }

}
