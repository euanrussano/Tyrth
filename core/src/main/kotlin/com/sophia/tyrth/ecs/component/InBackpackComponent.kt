package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class InBackpackComponent : Component {

    var backpackID : Int = 0

    companion object {
        val ID = mapperFor<InBackpackComponent>()
    }
}
