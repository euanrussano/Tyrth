package com.sophia.tyrth.infrastructure.factory.tilemap

import com.sophia.tyrth.model.tilemap.Tilemap

interface TilemapFactory {

    // returns the tilemap and a list of possible spawn points (for entities, items, etc placement)
    fun build(depth : Int) : TilemapFactoryResult

    data class TilemapFactoryResult(
        val tilemap: Tilemap,
        val heroSpawnPoint : Pair<Int, Int>,
        val monsterSpawnPoints : List<Pair<Int, Int>>,
        val itemSpawnPoints :  List<Pair<Int, Int>>,
    )

}


