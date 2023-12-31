package com.sophia.tyrth.model.monster

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.model.hero.Hero
import kotlin.math.absoluteValue
import kotlin.math.sign

class Monster(
    val name : String,
    val viewRange : Int,
    val monsterPreFab : MonsterPreFab,
    val maxHP : Int,
    val power : Int,
    val defense : Int
) {


    fun draw(batch: SpriteBatch, x: Int, y: Int) {
        monsterPreFab.draw(batch, x, y)
    }


    enum class MonsterState : State<MonsterInstance> {
        IDLE(){
            override fun update(entity: MonsterInstance) {
                if (!canAct(entity)) return

                // if hero is visible, start shouting insults
                entity.fieldOfView.visibileEntities.firstOrNull { entity -> entity is Hero }?.let {
                    GameLog.add("${entity.monster.name} shouts insults!")
                    entity.stateMachine.changeState(CHASE_PLAYER)
                }
                entity.velocity = MathUtils.random(-1, 1) to MathUtils.random(-1, 1)

            }

        },
        CHASE_PLAYER(){
            override fun update(entity: MonsterInstance) {
                if (!canAct(entity)) return
                entity.fieldOfView.visibileEntities.firstOrNull { entity -> entity is Hero } ?.let {hero ->
                    val dx = hero.position.first - entity.position.first
                    val dy = hero.position.second - entity.position.second
                    if (dx.absoluteValue > dy.absoluteValue){
                        entity.velocity = dx.sign to 0
                    } else {
                        entity.velocity = 0 to dy.sign
                    }

                }?: run{
                    println("player left visibility")
                    entity.stateMachine.changeState(IDLE)
                }


            }

        };
        val timeToThink = 1f
        var currentTimeUntilThink = timeToThink

        fun canAct(entity: MonsterInstance) : Boolean {

            val delta = GdxAI.getTimepiece().deltaTime
            currentTimeUntilThink -= delta
            if (currentTimeUntilThink > 0f) return false
            currentTimeUntilThink = timeToThink
            return true

        }

        override fun enter(entity: MonsterInstance?) {
            //TODO("Not yet implemented")
        }

        override fun exit(entity: MonsterInstance?) {
            //TODO("Not yet implemented")
        }

        override fun onMessage(entity: MonsterInstance?, telegram: Telegram?): Boolean {
            //TODO("Not yet implemented")
            return false
        }

    }

}
