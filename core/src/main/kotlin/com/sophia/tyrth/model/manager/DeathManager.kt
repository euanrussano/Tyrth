package com.sophia.tyrth.model.manager

import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.Messages
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.monster.MonsterInstance

class DeathManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        for (entity in world.entities){
            if (entity.hp <= 0) {
                if (entity is MonsterInstance){
                    world.monsterInstances.remove(entity)
                    GameLog.add("${entity.monster.name} is dead...")
                }
            }
        }
    }

}
