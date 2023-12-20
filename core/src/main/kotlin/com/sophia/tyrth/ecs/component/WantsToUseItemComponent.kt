package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class WantsToUseItemComponent : Component{

    lateinit var item : Entity

    companion object{
        val ID = mapperFor<WantsToUseItemComponent>()
    }

}
