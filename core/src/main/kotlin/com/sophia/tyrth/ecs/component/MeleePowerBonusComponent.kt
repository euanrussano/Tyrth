package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class MeleePowerBonusComponent : Component {

    var power = 1

    companion object{
        val ID = mapperFor<MeleePowerBonusComponent>()
    }
}
