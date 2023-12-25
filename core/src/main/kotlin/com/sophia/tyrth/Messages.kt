package com.sophia.tyrth

object Messages {

    var value = 0
    fun nextInt() = value++

    val HERO_HEALTH_CHANGED = nextInt()
    val HERO_EQUIPMENT_CHANGED: Int = nextInt()
    val HERO_INVENTORY_CHANGED: Int = nextInt()
    val LOG_CHANGED: Int = nextInt()

}
