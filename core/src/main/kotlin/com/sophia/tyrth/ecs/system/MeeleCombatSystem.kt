package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.HungerState
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.remove
import ktx.ashley.with
import kotlin.math.max

class MeeleCombatSystem : IteratingSystem(
    allOf(
        WantsToMeeleComponent::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?: return

        var offensiveBonus = 0

        // bonus for equipment
        EquipmentHolderComponent.ID[entity]?.let {
            for (item in it.slots.values.filterNotNull()){
                MeleePowerBonusComponent.ID[item]?.let { offensiveBonus += it.power }
            }
        }
        // bonus for well fed
        HungerClockComponent.ID[entity]?.let {hunger ->
            if (hunger.state == HungerState.WellFed){
                offensiveBonus += 1
            }
        }

        val power = CombatStatsComponent.ID[entity].power
        val hp = HealthComponent.ID[entity].hp
        val target = WantsToMeeleComponent.ID[entity].target
        val name = NameComponent.ID[entity]?.name ?: "<No Name>"

        val totalPower = power + offensiveBonus


        if (hp <= 0){
            entity.remove<WantsToMeeleComponent>()
            return
        }
        var deffenseBonus = 0
        EquipmentHolderComponent.ID[target]?.let {
            for (item in it.slots.values.filterNotNull()) {
                DefenseBonusComponent.ID[item]?.let { deffenseBonus += it.defense }
            }
        }

        val defense2 = CombatStatsComponent.ID[target]?.defense ?: return
        val health2 = HealthComponent.ID[target]
        val name2 = NameComponent.ID[target]?.name ?: "<No Name>"

        val totalDefense = defense2 + deffenseBonus

        val damage = max(0, totalPower - totalDefense)

        if (damage == 0){
            GameLog.add("$name is unable to hurt $name2")
        } else {
            GameLog.add("$name hits $name2 for $damage hp.")
            health2.hp -= damage

            // spawn damage particle
            val (targetX, targetY) = with(PositionComponent.ID[target]){x to y}
            engine.entity {
                with<PositionComponent>{
                    x = targetX
                    y = targetY
                }
                with<NameComponent>{
                    this.name = "damage"
                }
                with<RenderableComponent>()
                with<ParticleLifeTimeComponent>{
                    lifetime_ms = 200
                }
            }
        }

        HeroComponent.ID[entity]?.let {
            MessageManager.getInstance().dispatchMessage(Messages.HERO_HEALTH_CHANGED)
        }
        HeroComponent.ID[target]?.let {
            MessageManager.getInstance().dispatchMessage(Messages.HERO_HEALTH_CHANGED)
        }

        entity.remove<WantsToMeeleComponent>()



    }

}
