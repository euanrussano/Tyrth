package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.plusAssign

class CollisionSystem : IteratingSystem(
    allOf(
        PositionComponent::class,
        VelocityComponent::class,
        CollisionComponent::class
    ).get()
) {

    val bounds1 = Rectangle(0f, 0f, 1f, 1f)
    val bounds2 = Rectangle(0f, 0f, 1f, 1f)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val position = PositionComponent.ID[entity]
        val velocity = VelocityComponent.ID[entity]

        val newX = position.x + velocity.dx
        val newY = position.y + velocity.dy

        bounds1.setPosition(newX.toFloat(), newY.toFloat())

        for (entity2 in engine.getEntitiesFor(allOf(PositionComponent::class, CollisionComponent::class).get())){
            if (entity == entity2) continue

            val position2 = PositionComponent.ID[entity2]
            bounds2.setPosition(position2.x.toFloat(), position2.y.toFloat())

            if (bounds1.overlaps(bounds2)){
                velocity.dx = 0
                velocity.dy = 0

                val combatStats1 = CombatStatsComponent.ID[entity]
                val combatStats2 = CombatStatsComponent.ID[entity2]
                if (combatStats1 != null && combatStats2 != null){
                    entity += WantsToMeeleComponent().apply{ target = entity2 }
                }
                return
            }
        }

    }

}
