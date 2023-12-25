package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.XmlWriter
import ktx.ashley.mapperFor

class CombatStatsComponent : Component{

    var power = 1
    var defense = 1

    companion object{
        val ID = mapperFor<CombatStatsComponent>()
    }


}
