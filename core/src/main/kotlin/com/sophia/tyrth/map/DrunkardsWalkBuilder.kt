package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.EntityFactory

class DrunkardsWalkBuilder(override var depth: Int, val settings: DrunkardSettings) : MapBuilder {

    companion object{
        fun openArea(depth: Int) = DrunkardsWalkBuilder(depth, DrunkardSettings(DrunkSpawnMode.STARTING_POSITION, 400, 50))
        fun openHalls(depth: Int) = DrunkardsWalkBuilder(depth, DrunkardSettings(DrunkSpawnMode.RANDOM, 400, 50))
        fun windingPassages(depth: Int) = DrunkardsWalkBuilder(depth, DrunkardSettings(DrunkSpawnMode.RANDOM, 100, 40))
    }
    enum class DrunkSpawnMode{
        STARTING_POSITION,
        RANDOM
    }

    data class DrunkardSettings(
        val spawnMode : DrunkSpawnMode,
        val drunkenLifetime : Int,
        val floorPercent : Int
    )

    val spawnAreas = mutableListOf<Pair<Int, Int>>()
    override var map: Array<Array<TileType>> = Array(50){ Array(50){TileType.WALL} }
    override var heroPosition: Pair<Int, Int> = -1 to -1

    override fun build() {


        // Find a starting point; start at the middle and walk left until we find an open tile
        var startIdx = map.size/2 to map[0].size/2
        heroPosition = startIdx

        drunkardWalk()

        val downStairPosition = MapCommons.removeUnreachableAreasReturnsMostDistant(map, startIdx)

//        println("DOWNSTAIR = $downStairPosition")
        map[downStairPosition.first][downStairPosition.second] = TileType.DOWNSTAIRS


        // (The original article uses Vornoi diagram https://bfnightly.bracketproductions.com/chapter_27.html)
        for (x in map.indices){
            for (y in map[0].indices){
                if (map[x][y] == TileType.FLOOR){
                    spawnAreas.add(x to y)
                }
            }
        }


    }

    private fun drunkardWalk() {
        // Set central starting position
        val startingPosition = map.size/2 to map[0].size/2
        map[startingPosition.first][startingPosition.second] = TileType.FLOOR

        val totalTiles = map.flatten().size
        val desiredFloorTiles = (totalTiles*settings.floorPercent)/100
        var floorTileCount = map.flatten().filter { it == TileType.FLOOR }.size
        var diggerCount = 0
        var activeDiggerCount = 0

        while (floorTileCount < desiredFloorTiles){
            var didSomething = false
            var (drunkX, drunkY) = when(settings.spawnMode){
                DrunkSpawnMode.STARTING_POSITION -> startingPosition
                DrunkSpawnMode.RANDOM ->{
                    if (diggerCount == 0){
                        startingPosition
                    } else {
                        MathUtils.random(1, map.size-2) to MathUtils.random(1, map[0].size-2)
                    }
                }
            }

            var drunkLife = settings.drunkenLifetime

            while(drunkLife > 0){
                if (map[drunkX][drunkY] == TileType.WALL){
                    didSomething = true
                }

                map[drunkX][drunkY] = TileType.FLOOR

                val staggerDirection = MathUtils.random(1, 4)
                when(staggerDirection){
                    1 -> { if (drunkX > 2) drunkX -= 1}
                    2 -> { if (drunkX < map.size - 2) drunkX += 1}
                    3 -> { if (drunkY > 2) drunkY -= 1}
                    4 -> { if (drunkY < map[0].size - 2) drunkY += 1}
                }
                drunkLife -= 1
            }

            if (didSomething){
                activeDiggerCount += 1
            }

            diggerCount += 1
            floorTileCount = map.flatten().filter { it == TileType.FLOOR }.size
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
