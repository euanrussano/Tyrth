package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Game
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.HungerState
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.*
import kotlin.math.min
import kotlin.with

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

            // show a heart particle effect
            val (x, y) = with(PositionComponent.ID[entity]){ x to y}
            engine.entity {
                with<PositionComponent>{
                    this.x = x
                    this.y = y
                }
                with<NameComponent>{
                    name = "heart"
                }
                with<RenderableComponent>()
                with<ParticleLifeTimeComponent>{
                    lifetime_ms = 200
                }
            }
        }

        ProvidesFoodComponent.ID[item]?.let {
            val name = NameComponent.ID[item].name
            HungerClockComponent.ID[entity]?.let { hunger ->
                hunger.state = HungerState.WellFed
                hunger.duration = hunger.state.duration
                HeroComponent.ID[entity]?.let {
                    GameLog.add("You eat the $name")
                }
            }
        }

        MapRevealerComponent.ID[item]?.let {
            FieldOfViewComponent.ID[entity]?.let { fieldOfView ->
                val maxX = engine.getEntitiesFor(allOf(PositionComponent::class).get()).map { PositionComponent.ID[it].x }.max()
                val maxY = engine.getEntitiesFor(allOf(PositionComponent::class).get()).map { PositionComponent.ID[it].y }.max()
                for (i in 0 .. maxX){
                    for (j in 0 .. maxY){
                        fieldOfView.revealedTiles.add(i to j)

                    }
                }

                HeroComponent.ID[entity]?.let {
                    GameLog.add("The map is revealed to you!")
                }
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
