package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class BackpackComponent : Component {

    var maxWeight: Int = 1
    val items = mutableListOf<Entity>()

    companion object{
        val ID = mapperFor<BackpackComponent>()
    }

}
