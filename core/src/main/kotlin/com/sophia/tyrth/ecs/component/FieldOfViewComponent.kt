package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.XmlWriter
import ktx.ashley.mapperFor

class FieldOfViewComponent : Component{

    var range = 1
    val visibleTiles = mutableSetOf<Pair<Int, Int>>()
    val revealedTiles = mutableSetOf<Pair<Int, Int>>()

    companion object{
        val ID = mapperFor<FieldOfViewComponent>()
    }
}
