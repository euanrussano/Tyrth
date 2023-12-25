package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.sophia.tyrth.HungerState
import ktx.ashley.mapperFor

class HungerClockComponent : Component {
    var state = HungerState.WellFed
    var duration = 20

    companion object {
        val ID = mapperFor<HungerClockComponent>()
    }
}
