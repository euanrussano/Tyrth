package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.sophia.tyrth.ecs.component.ParticleLifeTimeComponent
import ktx.ashley.allOf

class ParticleVanishingSystem : IteratingSystem(
    allOf(
        ParticleLifeTimeComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        // age out the particle
        val particleLife = ParticleLifeTimeComponent.ID[entity]
        particleLife.lifetime_ms -= (deltaTime*1000).toInt()

        if (particleLife.lifetime_ms <= 0){
            engine.removeEntity(entity)
        }
    }

}
