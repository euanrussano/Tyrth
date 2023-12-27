package com.sophia.tyrth.map

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultConnection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.max
import kotlin.math.min

object MapCommons {

    fun applyRoomToMap(room : Rectangle, map : Array<Array<TileType>>){
        for (x in (room.x).toInt() until (room.x + room.width).toInt()){
            for (y in (room.y).toInt() until (room.y + room.height).toInt()){
                map[x][y] = TileType.FLOOR
            }
        }
    }

    fun applyHorizontalTunnel(map: Array<Array<TileType>>, x1 : Int, x2 : Int, y : Int){
        for (x in min(x1, x2).. max(x1, x2)){
            map[x][y] = TileType.FLOOR
        }
    }

    fun applyVerticalTunnel(map: Array<Array<TileType>>, y1 : Int, y2 : Int, x : Int){
        for (y in min(y1, y2).. max(y1, y2)){
            map[x][y] = TileType.FLOOR
        }
    }


    fun removeUnreachableAreasReturnsMostDistant(map: Array<Array<TileType>>, startIdx: Pair<Int, Int>) : Pair<Int, Int>{
        val heuristics = object : Heuristic<Pair<Int, Int>> {
            override fun estimate(node: Pair<Int, Int>, endNode: Pair<Int, Int>): Float {
                return Vector2.dst(
                    node.first.toFloat(),
                    node.second.toFloat(),
                    endNode.first.toFloat(),
                    endNode.second.toFloat()
                )
            }

        }

        // Find all tiles we can reach from the starting point
        val (aStarGraph, allPairs) = aStarGraph(map)
        //        println(aStarGraph.nodeCount)
        val path = DefaultGraphPath<Pair<Int, Int>>()
        val pathFinder = IndexedAStarPathFinder(aStarGraph)


        var downStairPosition = -1 to -1
        var mostFar = 0
        for (x in map.indices) {
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
                if (distance == 0) {
                    map[x][y] = TileType.WALL
                } else {
                    // If it is further away than our current exit candidate, move the exit
                    if (distance > mostFar) {
                        mostFar = distance
                        downStairPosition = x to y
                    }
                }
            }
        }

        return downStairPosition

    }

    private fun aStarGraph(map: Array<Array<TileType>>): Pair<IndexedGraph<Pair<Int, Int>>, Array<Array<Pair<Int, Int>>>> {
        val graph = object : IndexedGraph<Pair<Int, Int>> {

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
                                if (i !=0 && j!= 0) continue
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
}
