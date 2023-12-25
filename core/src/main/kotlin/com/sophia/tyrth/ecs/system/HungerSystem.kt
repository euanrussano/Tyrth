package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Game
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.HungerState
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.HealthComponent
import com.sophia.tyrth.ecs.component.HeroComponent
import com.sophia.tyrth.ecs.component.HungerClockComponent
import ktx.ashley.allOf

class HungerSystem : IntervalIteratingSystem(
    allOf(
        HungerClockComponent::class,
        HealthComponent::class
    ).get(),
    1f
) {

    override fun processEntity(entity: Entity?) {
        val hunger = HungerClockComponent.ID[entity]
        val health = HealthComponent.ID[entity]

        hunger.duration -= 1

        val isHero = HeroComponent.ID[entity] != null

        if (hunger.duration <= 0){
            when(hunger.state){
                HungerState.WellFed ->{
                    hunger.state = HungerState.Normal
                    hunger.duration = hunger.state.duration
                    if (isHero){
                        GameLog.add("You are no longer well fed.")
                    }
                }
                HungerState.Normal ->{
                    hunger.state = HungerState.Hungry
                    hunger.duration = hunger.state.duration
                    if (isHero){
                        GameLog.add("You are hungry.")
                    }
                }
                HungerState.Hungry ->{
                    hunger.state = HungerState.Starving
                    hunger.duration = hunger.state.duration
                    if (isHero){
                        GameLog.add("You are starving.")
                    }
                }
                HungerState.Starving ->{
                    // Inflict damage from hunger
                    health.hp -= 1

                    if (isHero){
                        GameLog.add("Your hunger pangs are getting painful.")
                    }
                }
            }
        }

        if (isHero){
            MessageManager.getInstance().dispatchMessage(Messages.HERO_HEALTH_CHANGED)
        }
    }

}
