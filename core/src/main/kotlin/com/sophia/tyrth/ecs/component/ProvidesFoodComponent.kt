package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ProvidesFoodComponent : Component  {

    companion object{
        val ID = mapperFor<ProvidesFoodComponent>()
    }
}
