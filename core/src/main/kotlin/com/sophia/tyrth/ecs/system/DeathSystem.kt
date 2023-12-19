package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.HealthComponent
import com.sophia.tyrth.ecs.component.HeroComponent
import com.sophia.tyrth.ecs.component.NameComponent
import ktx.ashley.allOf

class DeathSystem : IteratingSystem(
    allOf(
        HealthComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val hp = HealthComponent.ID[entity].hp

        if (hp <= 0){
            val hero = HeroComponent.ID[entity]
            if (hero != null) {
                println("You are dead")
            } else {
                val name = NameComponent.ID[entity]?.name ?: "<No Name>"
                GameLog.entries.add("$name is dead")
                engine.removeEntity(entity)
            }
        }
    }

}
