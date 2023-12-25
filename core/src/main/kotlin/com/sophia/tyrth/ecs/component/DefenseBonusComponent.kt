package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class DefenseBonusComponent : Component {

    var defense = 1

    companion object{
        val ID = mapperFor<DefenseBonusComponent>()
    }
}
