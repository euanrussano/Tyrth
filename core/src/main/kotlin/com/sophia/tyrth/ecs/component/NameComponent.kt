package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class NameComponent : Component {

    var name = "<Missing Name>"

    companion object{
        val ID = mapperFor<NameComponent>()
    }
}
