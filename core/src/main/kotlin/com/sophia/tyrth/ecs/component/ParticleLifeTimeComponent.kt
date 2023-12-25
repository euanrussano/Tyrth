package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ParticleLifeTimeComponent : Component {
    var lifetime_ms = 1

    companion object{
        val ID = mapperFor<ParticleLifeTimeComponent>()
    }
}
