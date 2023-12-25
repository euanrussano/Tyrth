package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.XmlWriter
import ktx.ashley.mapperFor

class ProvidesHealingComponent : Component{

    var healAmount = 1

    companion object{
        val ID = mapperFor<ProvidesHealingComponent>()
    }

}
