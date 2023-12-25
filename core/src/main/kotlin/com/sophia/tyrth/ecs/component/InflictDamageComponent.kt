package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class InflictDamageComponent : Component {

    var damage = 1

    companion object{
        val ID = mapperFor<InflictDamageComponent>()
    }

}
