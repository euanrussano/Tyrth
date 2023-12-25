package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class WantsToZoomIn : Component{

    companion object{
        val ID = mapperFor<WantsToZoomIn>()
    }

}
