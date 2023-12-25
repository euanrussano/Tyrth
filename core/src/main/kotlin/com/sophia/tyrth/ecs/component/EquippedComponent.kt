package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class EquippedComponent : Component {

    lateinit var owner : Entity

    companion object{
        val ID = mapperFor<EquippedComponent>()
    }
}
