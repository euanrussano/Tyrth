package com.sophia.tyrth.infrastructure.factory.terrain

import com.sophia.tyrth.Assets
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import com.sophia.tyrth.model.terrain.Terrain
import com.sophia.tyrth.model.terrain.TerrainTexturePreFab

class InMemoryTerrainFactory(val terrainRepository: TerrainRepository) {

    init {
        terrainRepository.entities.addAll(
            listOf(
                Terrain("Wall", TerrainTexturePreFab(Assets.wall), true),
                Terrain("Floor", TerrainTexturePreFab(Assets.floor), false)
            )
        )
    }

}
