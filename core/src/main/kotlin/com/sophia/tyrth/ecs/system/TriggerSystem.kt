package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.remove
import ktx.ashley.with

class TriggerSystem : IteratingSystem(
    allOf(
        EntryTriggerComponent::class,
        PositionComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val position = PositionComponent.ID[entity]
        val name = NameComponent.ID[entity].name

        for (entity2 in engine.getEntitiesFor(allOf(EntityMovedComponent::class).get())){
            val position2 = PositionComponent.ID[entity2]

            // Triggering condition
            if (position.x == position2.x && position.y == position2.y){
                // is no more hidden
                entity.remove<HiddenComponent>()

                // if the triggering entity has inflict damage and the target
                // has health, apply damage
                InflictDamageComponent.ID[entity]?.let {inflictDamage ->
                    HealthComponent.ID[entity2]?.let {health2 ->
                        health2.hp -= inflictDamage.damage

                        // show damage effect
                        engine.entity {
                            with<PositionComponent>{
                                x = position2.x
                                y = position2.y
                            }
                            with<RenderableComponent>()
                            with<NameComponent>{
                                this.name = "damage"
                            }
                            with<ParticleLifeTimeComponent>{
                                lifetime_ms = 200
                            }

                        }
                    }
                }

                HeroComponent.ID[entity2]?.let{
                    GameLog.add("$name triggers!")
                    MessageManager.getInstance().dispatchMessage(Messages.HERO_HEALTH_CHANGED)
                }

                engine.removeEntity(entity)
                return

            }
        }
    }

}
