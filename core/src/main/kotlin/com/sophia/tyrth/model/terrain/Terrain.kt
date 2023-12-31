package com.sophia.tyrth.model.terrain

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Terrain(
    val name : String,
    val prefab : TerrainPreFab,
    val isWall : Boolean,
    val isDepthIncrease : Boolean
) {
    fun draw(batch: SpriteBatch, x: Int, y: Int) {
        prefab.draw(batch, x, y)
    }

}
