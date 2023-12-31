package com.sophia.tyrth.model.monster

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.model.Entity

class MonsterInstance(val monster : Monster, position : Pair<Int, Int>) : Entity(position, monster.viewRange, monster.maxHP, monster.power, monster.defense){

    val stateMachine = DefaultStateMachine(this, Monster.MonsterState.IDLE)

    fun update(delta: Float) {
        stateMachine.update()
    }

    fun draw(batch: SpriteBatch) {
        monster.draw(batch, position.first, position.second)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        return false
    }

}
