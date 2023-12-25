package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.HealthComponent
import com.sophia.tyrth.ecs.component.HeroComponent
import ktx.ashley.allOf

class GameOverSystem : IteratingSystem(
    allOf(
        HeroComponent::class
    ).get()
) {

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val health = HealthComponent.ID[entity]

        if (health.hp <= 0){
            engine.removeAllEntities()
            MessageManager.getInstance().dispatchMessage(Messages.GAME_OVER)
        }


    }

}
