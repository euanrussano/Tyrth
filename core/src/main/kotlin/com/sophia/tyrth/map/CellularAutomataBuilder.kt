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

        val heuristics = object : Heuristic<Pair<Int, Int>>{
            override fun estimate(node: Pair<Int, Int>, endNode: Pair<Int, Int>): Float {
                return Vector2.dst(node.first.toFloat(), node.second.toFloat(), endNode.first.toFloat(), endNode.second.toFloat())
            }

        }

        // Find all tiles we can reach from the starting point
        val (aStarGraph, allPairs) = aStarGraph(map)
//        println(aStarGraph.nodeCount)
        val path = DefaultGraphPath<Pair<Int, Int>>()
        val pathFinder = IndexedAStarPathFinder(aStarGraph)



        var downStairPosition = -1 to -1
        var mostFar = 0
        for (x in map.indices){
            for (y in map[0].indices) {
                if (map[x][y] == TileType.WALL) continue
                val start = allPairs[startIdx.first][startIdx.second]
                val goal = allPairs[x][y]
                val result = pathFinder.searchNodePath(start, goal, heuristics, path)
//                if (result){ println("found path")}
                val distance = path.count
                path.clear()
//                if (distance > 0){
//                    println("distance = $distance")
//                }
                // We can't get to this tile - so we'll make it a wall
                if (distance == 0){
                    map[x][y] = TileType.WALL
                }else {
                    // If it is further away than our current exit candidate, move the exit
                    if (distance > mostFar){
                        mostFar = distance
                        downStairPosition = x to y
                    }
                }
            }
        }
//        println("DOWNSTAIR = $downStairPosition")
        map[downStairPosition.first][downStairPosition.second] = TileType.DOWNSTAIRS

        // for each floor tile, set a probability of 10%+2*depth of having an entity
        // (The original article uses Vornoi diagram https://bfnightly.bracketproductions.com/chapter_27.html)
        for (x in map.indices){
            for (y in map[0].indices){
                if (map[x][y] == TileType.FLOOR){
                    spawnAreas.add(x to y)
                }
            }
        }


    }

    private fun aStarGraph(map: Array<Array<TileType>>): Pair<IndexedGraph<Pair<Int, Int>>, Array<Array<Pair<Int, Int>>>> {
        val graph = object : IndexedGraph<Pair<Int, Int>>{

            val allConnections = mutableMapOf<Pair<Int, Int>, com.badlogic.gdx.utils.Array<Connection<Pair<Int, Int>>>>()
            val allNodes = mutableListOf<Pair<Int, Int>>()
            val allPairs = Array(map.size){x -> Array(map[0].size){ y-> x to y } }
            init {
                for (x in map.indices){
                    for (y in map[0].indices){
                        if (map[x][y] == TileType.WALL) continue
                        val connections = com.badlogic.gdx.utils.Array<Connection<Pair<Int, Int>>>()
                        for (i in -1 .. 1){
                            for (j in -1 .. 1){
                                if (i==0 && j ==0) continue
                                if (x + i !in map.indices || y+j !in map[0].indices) continue
                                if (map[x + i][ y + j] ==TileType.WALL) continue
                                connections.add(DefaultConnection(allPairs[x][y], allPairs[x+i][y+j]))
                            }
                        }
                        allConnections[allPairs[x][y]] = connections
                        allNodes.add(allPairs[x][y])
                    }
                }
            }
            override fun getConnections(fromNode: Pair<Int, Int>): com.badlogic.gdx.utils.Array<Connection<Pair<Int, Int>>> {
                return allConnections[fromNode]!!
            }

            override fun getIndex(node: Pair<Int, Int>): Int {
                return allNodes.indexOf(node)
            }

            override fun getNodeCount(): Int {
                return allNodes.size
            }

        }
        return graph to graph.allPairs
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
