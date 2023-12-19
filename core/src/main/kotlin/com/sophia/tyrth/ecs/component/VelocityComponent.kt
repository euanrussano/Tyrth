package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class VelocityComponent : Component {
    var dx = 0
    var dy = 0

    companion object{
        val ID = mapperFor<VelocityComponent>()
    }

}
