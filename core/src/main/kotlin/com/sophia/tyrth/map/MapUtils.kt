package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils

object MapUtils {

    fun randomBuilder(depth : Int) : MapBuilder{
        val builder = if (MathUtils.randomBoolean(1f)) BspDungeonBuilder(depth) else SimpleMapBuilder(depth)
        return builder
    }



}
