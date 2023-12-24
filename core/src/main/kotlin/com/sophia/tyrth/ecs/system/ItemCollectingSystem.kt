package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.remove

class ItemCollectingSystem : IteratingSystem(
    allOf(
        BackpackComponent::class,
        PositionComponent::class
    ).get()
) {

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val position = PositionComponent.ID[entity]
        val backpack = BackpackComponent.ID[entity]

        for (entity2 in engine.getEntitiesFor(allOf(NameComponent::class, ItemComponent::class, PositionComponent::class).get())){
            val position2 = PositionComponent.ID[entity2]
            val name2 = NameComponent.ID[entity2].name

            if (position.x != position2.x || position.y != position2.y) continue

            if (backpack.currentWeight >= backpack.maxWeight){
                HeroComponent.ID[entity]?.let {
                    val msg = "Your backpack is full! Cannot pick up item"
                    if (GameLog.entries.lastOrNull()?.equals(msg) != true){
                        GameLog.entries.add(msg)
                    }

                }
                return
            }

            entity2.remove<PositionComponent>()
            entity2.add(InBackpackComponent().apply { backpackID = backpack.ID })
            backpack.currentWeight += 1
            HeroComponent.ID[entity]?.let {
                GameLog.entries.add("You pick up the $name2")
            }
            return
        }
    }

}
