package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.sophia.tyrth.EquipmentSlot
import ktx.ashley.mapperFor

class EquipmentHolderComponent : Component {

    var slots : MutableMap<EquipmentSlot, Entity?> = mutableMapOf(
        EquipmentSlot.MELEE to null,
        EquipmentSlot.SHIELD to null
    )

    companion object{
        val ID = mapperFor<EquipmentHolderComponent>()
    }
}
