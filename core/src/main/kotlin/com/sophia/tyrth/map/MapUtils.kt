package com.sophia.tyrth.map

import com.badlogic.gdx.math.MathUtils

object MapUtils {

    fun randomBuilder(depth : Int) : MapBuilder{
        val roll = MathUtils.random(1, 7)
        val builder = when(roll){
            1 -> BspDungeonBuilder(depth)
            2 -> BspInteriorBuilder(depth)
            3 -> CellularAutomataBuilder(depth)
            4 -> DrunkardsWalkBuilder.openArea(depth)
            5 -> DrunkardsWalkBuilder.openHalls(depth)
            6 -> DrunkardsWalkBuilder.windingPassages(depth)
            else -> SimpleMapBuilder(depth)
        }
        
//        val builder = DrunkardsWalkBuilder(depth, drunkSettings)
        return builder
    }



}
