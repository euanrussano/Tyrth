package com.sophia.tyrth

import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.infrastructure.factory.tilemap.DungeonTilemapFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.SimpleTilemapFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import kotlin.random.asKotlinRandom

object TilemapUtils {

    lateinit var tilemapFactories : List<TilemapFactory>

    fun initialize(terrainRepository: TerrainRepository){
        tilemapFactories = listOf(
            SimpleTilemapFactory(terrainRepository),
            DungeonTilemapFactory(terrainRepository)
        )
    }

    fun random() : TilemapFactory{

        return tilemapFactories.random(MathUtils.random.asKotlinRandom())
    }

}
