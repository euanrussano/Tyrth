package com.sophia.tyrth

import com.badlogic.gdx.ai.msg.MessageManager

object GameLog {

    private val __entries = mutableListOf<String>()
    val entries : List<String>
        get() = __entries


    fun add(msg : String){
        if (__entries.lastOrNull()?.equals(msg) == true) return

        __entries.add(msg)
        MessageManager.getInstance().dispatchMessage(Messages.LOG_CHANGED)
    }

    fun clear() {
        __entries.clear()
        MessageManager.getInstance().dispatchMessage(Messages.LOG_CHANGED)
    }


}
