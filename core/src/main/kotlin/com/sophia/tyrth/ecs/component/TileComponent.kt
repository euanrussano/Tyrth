package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TileComponent : Component {

    companion object{
        val ID = mapperFor<TileComponent>()
    }

}
