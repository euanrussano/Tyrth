package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import kotlin.math.absoluteValue
import kotlin.math.sign

class MonsterAI : IntervalIteratingSystem(
    allOf(
        MonsterComponent::class
    ).get(),
    1f
) {

    override fun processEntity(entity: Entity?) {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val (heroX, heroY) = with(PositionComponent.ID[hero]){ x to y}

        val fieldOfView = FieldOfViewComponent.ID[entity]
        val name = NameComponent.ID[entity].name
        val velocity = VelocityComponent.ID[entity]
        val (x, y) = with(PositionComponent.ID[entity]){ x to y}

        if (heroX to heroY in fieldOfView.visibleTiles){
            println("$name shouts insults")
            val dx = heroX - x
            val dy = heroY - y
            if (dx.absoluteValue > dy.absoluteValue){
                velocity.dx = dx.sign
            } else {
                velocity.dy = dy.sign
            }
        }

    }
}
