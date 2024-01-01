package com.sophia.tyrth.infrastructure.factory.tilemap

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultConnection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import com.sophia.tyrth.map.TileType
import com.sophia.tyrth.model.tilemap.Tile
import com.sophia.tyrth.model.tilemap.Tilemap
import kotlin.math.max
import kotlin.math.min
import kotlin.random.asKotlinRandom


class DungeonTilemapFactory(
    val terrainRepository: TerrainRepository
) : TilemapFactory {

    override fun build(depth: Int): TilemapFactory.TilemapFactoryResult {
        val wallTerrain = terrainRepository.findByName("Wall")
        val floorTerrain = terrainRepository.findByName("Floor")
        val downstairsTerrain = terrainRepository.findByName("Downstairs")

        val rooms = mutableListOf<Rectangle>()

        val width = 80
        val height = 50
        val isWall = Array(width){Array(height){ true } }
        val isDownStairs = Array(width){Array(height){ false } }

        val MAX_ROOMS = 30
        val MIN_SIZE = 6
        val MAX_SIZE = 10

        for (i in 0 .. MAX_ROOMS){
            val w = MathUtils.random(MIN_SIZE, MAX_SIZE)
            val h = MathUtils.random(MIN_SIZE, MAX_SIZE)
            val x = MathUtils.random(1, width - w - 1)
            val y = MathUtils.random(1, height - h - 1)
            val newRoom = Rectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
            val ok = rooms.firstOrNull { rectangle -> rectangle.overlaps(newRoom) } == null
            if (ok){
                applyRoomToMap(newRoom, isWall)

                if (rooms.isNotEmpty()){
                    val center = Vector2()
                    val prevCenter = Vector2()
                    newRoom.getCenter(center)
                    rooms.last().getCenter(prevCenter)
                    if (MathUtils.randomBoolean()){
                        applyHorizontalTunnel(
                            isWall,
                            prevCenter.x.toInt(),
                            center.x.toInt(),
                            prevCenter.y.toInt()
                        )
                        applyVerticalTunnel(isWall, prevCenter.y.toInt(), center.y.toInt(), center.x.toInt())
                    } else {
                        applyHorizontalTunnel(
                            isWall,
                            prevCenter.x.toInt(),
                            center.x.toInt(),
                            center.y.toInt()
                        )
                        applyVerticalTunnel(
                            isWall,
                            prevCenter.y.toInt(),
                            center.y.toInt(),
                            prevCenter.x.toInt()
                        )
                    }
                }

                rooms.add(newRoom)
            }
        }

        val center = Vector2()
        rooms.first().getCenter(center)
        val heroPosition = center.x.toInt() to center.y.toInt()

        val stairsPosition = Vector2()
        rooms.first().getCenter(stairsPosition)
        isDownStairs[stairsPosition.x.toInt() + 1][stairsPosition.y.toInt()] = true


        val tiles = Array(width){ x -> Array(height){y ->
            val terrain = if (isDownStairs[x][y]){
                downstairsTerrain
            } else if (isWall[x][y]){
                wallTerrain
            }else{
                floorTerrain
            }
            Tile(x, y, terrain)
        } }


        val numberMonsters = MathUtils.random(3, 7) + depth
        val spawnPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until numberMonsters){
            val room = rooms.drop(1).random(MathUtils.random.asKotlinRandom())
            val x = (room.x + 1 + MathUtils.random(0, room.width.toInt()-2)).toInt()
            val y = (room.y + 1 + MathUtils.random(0, room.height.toInt()-2)).toInt()
            if (x to y !in spawnPoints){
                spawnPoints.add(x to y)
            }
        }

        val numberItems = max(1, MathUtils.random(7, 11) - depth)
        val itemSpawnPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until numberItems){
            val room = rooms.drop(1).random(MathUtils.random.asKotlinRandom())
            val x = (room.x + 1 + MathUtils.random(0, room.width.toInt()-2)).toInt()
            val y = (room.y + 1 + MathUtils.random(0, room.height.toInt()-2)).toInt()
            if (x to y !in spawnPoints){
                itemSpawnPoints.add(x to y)
            }
        }


        return TilemapFactory.TilemapFactoryResult(Tilemap(tiles), heroPosition, spawnPoints, itemSpawnPoints)
    }

    fun applyRoomToMap(room : Rectangle, isWall : Array<Array<Boolean>>){
        for (x in (room.x).toInt() until (room.x + room.width).toInt()){
            for (y in (room.y).toInt() until (room.y + room.height).toInt()){
                isWall[x][y] = false
            }
        }
    }

    fun applyHorizontalTunnel(isWall: Array<Array<Boolean>>, x1 : Int, x2 : Int, y : Int){
        for (x in min(x1, x2).. max(x1, x2)){
            isWall[x][y] = false
        }
    }

    fun applyVerticalTunnel(isWall: Array<Array<Boolean>>, y1 : Int, y2 : Int, x : Int){
        for (y in min(y1, y2).. max(y1, y2)){
            isWall[x][y] = false
        }
    }


    fun removeUnreachableAreasReturnsMostDistant(isWall: Array<Array<Boolean>>, startIdx: Pair<Int, Int>) : Pair<Int, Int>{
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
        val (aStarGraph, allPairs) = aStarGraph(isWall)
        //        println(aStarGraph.nodeCount)
        val path = DefaultGraphPath<Pair<Int, Int>>()
        val pathFinder = IndexedAStarPathFinder(aStarGraph)


        var downStairPosition = -1 to -1
        var mostFar = 0
        for (x in isWall.indices) {
            for (y in isWall[0].indices) {
                if (isWall[x][y]) continue
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
                    isWall[x][y] = true
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

    private fun aStarGraph(isWall: Array<Array<Boolean>>): Pair<IndexedGraph<Pair<Int, Int>>, Array<Array<Pair<Int, Int>>>> {
        val graph = object : IndexedGraph<Pair<Int, Int>> {

            val allConnections = mutableMapOf<Pair<Int, Int>, com.badlogic.gdx.utils.Array<Connection<Pair<Int, Int>>>>()
            val allNodes = mutableListOf<Pair<Int, Int>>()
            val allPairs = Array(isWall.size){x -> Array(isWall[0].size){ y-> x to y } }
            init {
                for (x in isWall.indices){
                    for (y in isWall[0].indices){
                        if (isWall[x][y]) continue
                        val connections = com.badlogic.gdx.utils.Array<Connection<Pair<Int, Int>>>()
                        for (i in -1 .. 1){
                            for (j in -1 .. 1){
                                if (i==0 && j ==0) continue
                                if (i !=0 && j!= 0) continue
                                if (x + i !in isWall.indices || y+j !in isWall[0].indices) continue
                                if (isWall[x + i][ y + j]) continue
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
