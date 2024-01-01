package com.sophia.tyrth.infrastructure.factory.tilemap

import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import kotlin.random.asKotlinRandom

class RandomTilemapFactory(val terrainRepository: TerrainRepository) : TilemapFactory {

    val tilemapFactories = listOf(
        SimpleTilemapFactory(terrainRepository),
        DungeonTilemapFactory(terrainRepository)
    )

    override fun build(depth: Int): TilemapFactory.TilemapFactoryResult {
        val actualFactory = tilemapFactories.random(MathUtils.random.asKotlinRandom())
        println(actualFactory)
        return actualFactory.build(depth)
    }
}
