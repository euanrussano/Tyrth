package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.XmlReader
import com.badlogic.gdx.utils.XmlWriter
import ktx.ashley.mapperFor

class PositionComponent : Component{

    var x = 0
    var y = 0

    companion object{
        val ID = mapperFor<PositionComponent>()
    }
}
