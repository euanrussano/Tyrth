package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.ecs.component.EntityMovedComponent
import com.sophia.tyrth.ecs.component.PositionComponent
import com.sophia.tyrth.ecs.component.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.plusAssign
import ktx.ashley.remove
import kotlin.math.abs

class MovementSystem : IteratingSystem(
    allOf(
        PositionComponent::class,
        VelocityComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val position = PositionComponent.ID[entity]
        val velocity = VelocityComponent.ID[entity]

        position.x += velocity.dx
        position.y += velocity.dy

        if (velocity.dx != 0 || velocity.dy != 0 ){
            entity += EntityMovedComponent()
        } else {
            entity.remove<EntityMovedComponent>()
        }

        velocity.dx = 0
        velocity.dy = 0
    }

}
