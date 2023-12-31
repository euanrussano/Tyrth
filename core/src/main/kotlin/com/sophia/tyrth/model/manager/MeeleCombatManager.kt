package com.sophia.tyrth.model.manager

import com.sophia.tyrth.GameLog
import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.monster.MonsterInstance
import kotlin.math.max

class MeeleCombatManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        for (entity in world.entities){
            update(entity)
        }
    }

    private fun update(entity: Entity) {
        if (entity.hp <= 0) return

        val (x, y) = entity.position
        val (dx, dy) = entity.velocity

        for (entity2 in world.entities){
            if (entity == entity2) continue
            if (entity2.position == x + dx to y + dy){
                val damage = max(0, entity.power - entity2.defense)
                if (damage == 0){
                    if (entity is Hero){
                        GameLog.add("you are unable to hurt.")
                    }
                }
                 else {
                     entity2.takeDamage(damage)
                    if (entity is Hero && entity2 is MonsterInstance){
                        GameLog.add("You hurt ${entity2.monster.name} for $damage hp.")
                    } else if (entity is MonsterInstance && entity2 is Hero){
                        GameLog.add("${entity.monster.name} hurts you for $damage hp.")
                    }
                 }

                return
            }
        }
    }

}
