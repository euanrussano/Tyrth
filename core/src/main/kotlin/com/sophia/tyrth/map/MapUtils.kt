package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils

object MapUtils {

    fun randomBuilder(depth : Int) : MapBuilder{
        val roll = MathUtils.random(0, 3)
        val builder = when(roll){
            0 -> SimpleMapBuilder(depth)
            1 -> BspDungeonBuilder(depth)
            2 -> BspInteriorBuilder(depth)
            3 -> CellularAutomataBuilder(depth)
            else -> SimpleMapBuilder(depth)
        }
        //val builder = CellularAutomataBuilder(depth)
        return builder
    }



}
