package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class CollisionComponent : Component {

    companion object{
        val ID = mapperFor<CollisionComponent>()
    }

}
