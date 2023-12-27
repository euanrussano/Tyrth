package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultConnection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.EntityFactory
import com.sophia.tyrth.map.shortestpath.Edge
import com.sophia.tyrth.map.shortestpath.Node
import com.sophia.tyrth.map.shortestpath.ShortestPathResult
import com.sophia.tyrth.map.shortestpath.findShortestPath
import kotlin.math.max
import kotlin.math.min

class CellularAutomataBuilder(override var depth: Int) : MapBuilder {

    private val spawnAreas = mutableListOf<Pair<Int, Int>>()
    val MIN_ROOM_SIZE = 8
    override var map: Array<Array<TileType>> = Array(80){Array(50){TileType.WALL} }
    override var heroPosition: Pair<Int, Int> = -1 to -1

    override fun build() {

        // First we completely randomize the map, setting 55% of it to be floor.
        for (x in 1 until map.size - 2){
            for (y in 1 until map[0].size - 2){
                if (MathUtils.randomBoolean(0.55f)) map[x][y] = TileType.WALL
                else map[x][y] = TileType.FLOOR
            }
        }

        // Now we iteratively apply cellular automata rules
        for (int in 0 .. 15){
            val newTiles = Array(map.size){ x -> Array(map[0].size){ y-> map[x][y]} }
            for (x in 1 until map.size - 2){
                for (y in 1 until map[0].size - 2){
                    var neighbors = 0
                    for (i in -1 .. 1){
                        for (j in -1 .. 1){
                            if (i == 0 && j == 0) continue
                            if (x+i !in newTiles.indices) continue
                            if (y+j !in newTiles[0].indices) continue
                            if (map[x+i][y+j] == TileType.WALL){
                                neighbors += 1
                            }
                        }
                    }

                    if (neighbors > 4 || neighbors == 0){
                        newTiles[x][y] = TileType.WALL
                    } else {
                        newTiles[x][y] = TileType.FLOOR
                    }
                }
            }

            map = Array(map.size){x->Array(map[0].size){y-> newTiles[x][y]} }
        }



        // Find a starting point; start at the middle and walk left until we find an open tile
        var startIdx = map.size/2 to map[0].size/2

        while(map[startIdx.first][startIdx.second] != TileType.FLOOR){
            startIdx = startIdx.first-1 to map[0].size/2
        }
        heroPosition = startIdx
//        println("startIdx = $startIdx")

        val mostDistantPosition = MapCommons.removeUnreachableAreasReturnsMostDistant(map, startIdx)

        //        println("DOWNSTAIR = $downStairPosition")
        map[mostDistantPosition.first][mostDistantPosition.second] = TileType.DOWNSTAIRS


        // (The original article uses Vornoi diagram https://bfnightly.bracketproductions.com/chapter_27.html)
        for (x in map.indices) {
            for (y in map[0].indices) {
                if (map[x][y] == TileType.FLOOR) {
                    spawnAreas.add(x to y)
                }
            }
        }

    }



    override fun spawnEntities(engine: Engine) {
        // spawn the environment (tile) entities
        for (i in map.indices){
            for (j in map[i].indices){
                val isWall = map[i][j] == TileType.WALL
                val isDownstairs = map[i][j] == TileType.DOWNSTAIRS
                EntityFactory.tile(engine, i, j, isWall, isDownstairs)
            }
        }

        // the original article uses regions
        for (i in 0 .. 10){
            val firstIdx = MathUtils.random(0, spawnAreas.size-1)
            val secondIdx = MathUtils.random(firstIdx, spawnAreas.size-1)
            val subList = mutableListOf<Pair<Int, Int>>()
            for (j in firstIdx until secondIdx){
                subList.add(spawnAreas[j])
            }
            EntityFactory.spawnRegion(engine, subList, depth)
            spawnAreas.removeAll(subList)
        }
    }

}
