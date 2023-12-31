package com.sophia.tyrth.infrastructure.repository.terrain

import com.sophia.tyrth.infrastructure.repository.Repository
import com.sophia.tyrth.model.terrain.Terrain

class TerrainRepository : Repository<Terrain>() {

    override fun getName(it: Terrain): String {
        return it.name
    }

}
