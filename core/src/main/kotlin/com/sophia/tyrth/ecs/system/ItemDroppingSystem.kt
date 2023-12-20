package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.oneOf
import ktx.ashley.plusAssign
import ktx.ashley.remove

/*
    The item is dropped in an adjacent position from the entity (otherwise it could
    automatically pick again the item it dropped)
 */
class ItemDroppingSystem : IteratingSystem(
    allOf(
        WantsToDropItemComponent::class
    ).get()
) {

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val wantsToDropItem = WantsToDropItemComponent.ID[entity]
        val backpack = BackpackComponent.ID[entity]

        val itemToDrop = wantsToDropItem.item
        val name = NameComponent.ID[itemToDrop]?.name?:"<No Name>"

        val position = PositionComponent.ID[entity]
        // around the position of the entity that wants to drop, look for the first
        // free space (no other item or CollisionComponent entity) and drop it
        for (x in position.x-1 .. position.x+1){
            for (y in position.y-1 ..position.y + 1){
                var isBlocked = false
                for (entity2 in engine.getEntitiesFor(allOf(PositionComponent::class).oneOf(ItemComponent::class, CollisionComponent::class).get())){
                    val position2 = PositionComponent.ID[entity2]
                    if (position2.x == x && position2.y == y){
                        isBlocked = true
                        break
                    }
                }
                if (!isBlocked){
                    backpack.items.remove(itemToDrop)
                    itemToDrop += PositionComponent().apply { this.x = x; this.y = y }
                    entity.remove<WantsToDropItemComponent>()
                    HeroComponent.ID[entity]?.let {
                        GameLog.entries.add("You dropped the $name.")
                    }
                    return
                }

            }
        }

        // if reached here, means is not possible to drop the item at the current adjacent position
        entity.remove<WantsToDropItemComponent>()
        HeroComponent.ID[entity]?.let {
            GameLog.entries.add("Is not possible to drop item here...")
        }
    }

}
