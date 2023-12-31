package com.sophia.tyrth.infrastructure.factory.tilemap

import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.tilemap.Tile
import com.sophia.tyrth.model.tilemap.Tilemap
import com.sophia.tyrth.model.monster.MonsterInstance

class SimpleTilemapFactory(
    val terrainRepository : TerrainRepository,
) : TilemapFactory {

    override fun build(): Pair<Tilemap, List<Pair<Int, Int>>> {
        val wallTerrain = terrainRepository.findByName("Wall")
        val floorTerrain = terrainRepository.findByName("Floor")

        val heroPosition = 2 to 2
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
            if (x to y != heroPosition){
                isWall[x][y] = true
            }
        }

        val tiles = Array(width){ x -> Array(height){y ->
            val terrain = if (isWall[x][y]) wallTerrain else floorTerrain
            Tile(x, y, terrain)
        } }

        val spawnPoints = mutableListOf<Pair<Int, Int>>()
        spawnPoints.add(heroPosition)
        for ( i in 1 .. 10){
            val x = MathUtils.random(1, width - 1)
            val y = MathUtils.random(1, height - 1)
            if (!isWall[x][y]){
                spawnPoints.add(x to y)
            }
        }

        return Tilemap(tiles) to spawnPoints

    }
}
