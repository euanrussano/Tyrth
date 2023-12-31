package com.sophia.tyrth.infrastructure.factory.tilemap

import com.sophia.tyrth.model.tilemap.Tilemap

interface TilemapFactory {

    // returns the tilemap and a list of possible spawn points (for entities, items, etc placement)
    fun build() : Pair<Tilemap, List<Pair<Int, Int>>>

}
