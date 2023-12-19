package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class WantsToMeeleComponent : Component{

    lateinit var target : Entity
    companion object{
        val ID = mapperFor<WantsToMeeleComponent>()
    }

}
