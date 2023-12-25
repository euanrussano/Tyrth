package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine

object MapUtils {

    fun randomBuilder(depth : Int) : MapBuilder{
        val builder = SimpleMapBuilder(depth)
        return builder
    }



}
