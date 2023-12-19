package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class HealthComponent : Component {

    var maxHP: Int = 1
    var hp: Int = 1

    companion object{
        val ID = mapperFor<HealthComponent>()
    }

}
