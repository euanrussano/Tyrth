package com.sophia.tyrth.map.shortestpath

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultConnection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.map.TileType

/*
/ A small test of the A* algorithm with gdx-ai
 */
fun main() {

    val heuristics = object : Heuristic<GraphNode> {
        override fun estimate(node: GraphNode, endNode: GraphNode): Float {
            return Vector2.dst(node.x.toFloat(), node.y.toFloat(), endNode.x.toFloat(), endNode.y.toFloat())
        }

    }

    println("run test")
    val testMap = Array(10) { Array(10){ TileType.FLOOR} }
    val aStarGraph2 = Graph(testMap)
    val path = DefaultGraphPath<GraphNode>()
    val pathFinder = IndexedAStarPathFinder(aStarGraph2)

    val start = aStarGraph2.allNodes[0]
    val goal = aStarGraph2.allNodes[1]
    println((2 to 2).equals(2 to 2))
    println("$start index = ${aStarGraph2.getIndex(start)}")
    println("$goal index = ${aStarGraph2.getIndex(goal)}")
    println("heuristics = ${heuristics.estimate(start, goal)}")
    println("node count = ${aStarGraph2.nodeCount}")
    println("$start connections = ")
    aStarGraph2.getConnections(start).forEach { connection -> println("from: ${connection.fromNode} to:  ${connection.toNode}") }
    println("$goal connections = ")
    aStarGraph2.getConnections(goal).forEach { connection -> println("from: ${connection.fromNode} to:  ${connection.toNode}") }
    val result = IndexedAStarPathFinder(aStarGraph2).searchNodePath(start, goal, heuristics, path)
    val distance = path.count
    if (result){
        println("found path")
        println(distance)
    }else {
        println("path not found")
    }
    path.clear()

}

data class GraphNode(val x : Int, val y : Int)

class Graph(map : Array<Array<TileType>>): IndexedGraph<GraphNode> {

    val allConnections = mutableMapOf<GraphNode, com.badlogic.gdx.utils.Array<Connection<GraphNode>>>()
    val allNodes = mutableListOf<GraphNode>()
    init {
        val graphNodes = Array(map.size){ x -> Array(map[0].size){ y-> GraphNode(x, y)} }

        for (x in map.indices){
            for (y in map[0].indices){
//                if (map[x][y] == TileType.WALL) continue
                val connections = com.badlogic.gdx.utils.Array<Connection<GraphNode>>()
                for (i in -1 .. 1){
                    for (j in -1 .. 1){
//                        if (i==0 && j ==0) continue
                        if (x + i !in graphNodes.indices || y+j !in graphNodes[0].indices) continue
//                        if (map[x + i][ y + j] ==TileType.WALL) continue
                        connections.add(DefaultConnection(graphNodes[x][y], graphNodes[x+i][y+j]))
                    }
                }
                allConnections[graphNodes[x][y]] = connections
                allNodes.add(graphNodes[x][y])
            }
        }
    }
    override fun getConnections(fromNode: GraphNode): com.badlogic.gdx.utils.Array<Connection<GraphNode>> {
        return allConnections[fromNode]!!
    }

    override fun getIndex(node: GraphNode): Int {
        return allNodes.indexOf(node)
    }

    override fun getNodeCount(): Int {
        return allNodes.size
    }

}

