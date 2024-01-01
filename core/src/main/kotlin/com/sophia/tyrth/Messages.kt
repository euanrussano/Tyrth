package com.sophia.tyrth

object Messages {


    val EQUIPMENT_CHANGED: Int = nextInt()
    val DEPTH_CHANGED: Int = nextInt()
    val INVENTORY_CHANGED: Int = nextInt()
    val HEALTH_CHANGED: Int = nextInt()
    var value = 0
    fun nextInt() = value++

    val HERO_HEALTH_CHANGED = nextInt()
    val HERO_EQUIPMENT_CHANGED: Int = nextInt()
    val HERO_INVENTORY_CHANGED: Int = nextInt()
    val LOG_CHANGED: Int = nextInt()
    val GAME_OVER = nextInt()

}
