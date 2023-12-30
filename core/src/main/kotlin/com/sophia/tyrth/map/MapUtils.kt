package com.sophia.tyrth.map

import com.badlogic.gdx.math.MathUtils

object MapUtils {

    fun builders(depth: Int)  : Map<String, MapBuilder>{
        return mapOf(
            "Bsp Dungeon" to BspDungeonBuilder(depth),
            "Bsp Interior" to BspInteriorBuilder(depth),
            "Cellular Automata" to CellularAutomataBuilder(depth),
            "Drunkards Walk (arena)" to  DrunkardsWalkBuilder.openArea(depth),
            "Drunkards Walk (halls)" to DrunkardsWalkBuilder.openHalls(depth),
            "Drunkards Walk (passages)" to DrunkardsWalkBuilder.windingPassages(depth),
            "Maze" to MazeBuilder(depth),
            "Simple Map" to  SimpleMapBuilder(depth)
        )
    }

    fun randomBuilder(depth : Int) : MapBuilder{
        val roll = MathUtils.random(1, 7)
//        val builder = when(roll){
//            1 -> BspDungeonBuilder(depth)
//            2 -> BspInteriorBuilder(depth)
//            3 -> CellularAutomataBuilder(depth)
//            4 -> DrunkardsWalkBuilder.openArea(depth)
//            5 -> DrunkardsWalkBuilder.openHalls(depth)
//            6 -> DrunkardsWalkBuilder.windingPassages(depth)
//            7 -> MazeBuilder(depth)
//            else -> SimpleMapBuilder(depth)
//        }
        val builders = builders(depth)
        val builder = builders.get(builders.keys.random())!!
        return builder
    }



}
