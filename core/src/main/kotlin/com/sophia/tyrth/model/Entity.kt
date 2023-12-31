package com.sophia.tyrth.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegraph
import com.sophia.tyrth.Messages
import kotlin.math.min

abstract class Entity(
    var position : Pair<Int, Int>,
    viewRange : Int,
    var maxHP : Int,
    var power : Int,
    var defense : Int,
    var backpack : Backpack? = null
) : Telegraph{
    var hp = maxHP
        private set

    var velocity = 0 to 0
    var fieldOfView = FieldOfView(viewRange)

    init {
        backpack?.owner = this
    }

    fun takeDamage(damage : Int){
        if (damage < 0){
            Gdx.app.log(this::class.simpleName, "Negative damage")
            return
        }
        hp -= damage
        MessageManager.getInstance().dispatchMessage(this, Messages.HEALTH_CHANGED)
    }

    fun heal(amount: Int){
        if (amount < 0){
            Gdx.app.log(this::class.simpleName, "Negative healing amount")
            return
        }
        println(amount)
        val realAmount = min(maxHP - hp, amount)
        println(realAmount)
        hp += realAmount
        MessageManager.getInstance().dispatchMessage(this, Messages.HEALTH_CHANGED)
    }

}
