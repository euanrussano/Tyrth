package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine

interface MapBuilder {

    var depth : Int
    var map : Array<Array<TileType>>
    var heroPosition : Pair<Int, Int>

    fun build()
    fun spawnEntities(engine: Engine)
}
