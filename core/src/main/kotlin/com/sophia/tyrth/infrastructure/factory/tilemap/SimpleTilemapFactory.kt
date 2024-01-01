package com.sophia.tyrth.infrastructure.factory.tilemap

import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.tilemap.Tile
import com.sophia.tyrth.model.tilemap.Tilemap
import com.sophia.tyrth.model.monster.MonsterInstance
import kotlin.math.max

class SimpleTilemapFactory(
    val terrainRepository : TerrainRepository,
) : TilemapFactory {


    override fun build(depth: Int): TilemapFactory.TilemapFactoryResult {
        val wallTerrain = terrainRepository.findByName("Wall")
        val floorTerrain = terrainRepository.findByName("Floor")
        val downstairsTerrain = terrainRepository.findByName("Downstairs")

        val heroPosition = 2 to 2
        val downstairsPosition = 8 to 8
        val width = 10
        val height = 10
        val isWall = Array(width){Array(height){false}}

        for (x in 0 until width){
            isWall[x][0] = true
            isWall[x][height-1] = true
        }

        for (y in 0 until height){
            isWall[0][y] = true
            isWall[width - 1][y] = true
        }

        for (i in 0 until 10){
            val x = MathUtils.random(1, width-1)
            val y = MathUtils.random(1, height-1)
            if (x to y != heroPosition && x to y != downstairsPosition){
                isWall[x][y] = true
            }
        }

        val tiles = Array(width){ x -> Array(height){y ->
            val terrain = if (x to y == downstairsPosition) downstairsTerrain else if (isWall[x][y]) wallTerrain else floorTerrain
            Tile(x, y, terrain)
        } }

        val numberMonsters = MathUtils.random(3, 7) + depth
        val spawnPoints = mutableListOf<Pair<Int, Int>>()
        for ( i in 1 .. numberMonsters){
            val x = MathUtils.random(1, width - 1)
            val y = MathUtils.random(1, height - 1)
            if (!isWall[x][y] && x to y != heroPosition){
                spawnPoints.add(x to y)
            }
        }

        val numberItems = max(1, MathUtils.random(7, 11) - depth)

        val itemSpawnPoints = mutableListOf<Pair<Int, Int>>()
        for ( i in 1 .. numberItems){
            val x = MathUtils.random(1, width - 1)
            val y = MathUtils.random(1, height - 1)
            if (!isWall[x][y] && x to y != heroPosition && x to y !in spawnPoints){
                itemSpawnPoints.add(x to y)
            }
        }

        return TilemapFactory.TilemapFactoryResult(Tilemap(tiles),heroPosition, spawnPoints, itemSpawnPoints)

    }
}
