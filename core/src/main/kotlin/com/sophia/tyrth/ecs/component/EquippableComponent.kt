package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.sophia.tyrth.EquipmentSlot
import ktx.ashley.mapperFor

class EquippableComponent : Component{

    var slot = EquipmentSlot.MELEE
    companion object{
        val ID = mapperFor<EquippableComponent>()
    }


}
