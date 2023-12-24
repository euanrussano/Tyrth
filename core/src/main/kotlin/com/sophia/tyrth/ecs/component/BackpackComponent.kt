package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class BackpackComponent : Component {

    val ID = nextInt()
    var maxWeight: Int = 1
    var currentWeight = 0

    companion object{
        val ID = mapperFor<BackpackComponent>()
        var value = 0
        fun nextInt() : Int = value++
    }

}
