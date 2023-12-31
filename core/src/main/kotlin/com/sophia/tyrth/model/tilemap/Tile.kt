package com.sophia.tyrth.model.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tyrth.model.terrain.Terrain

class Tile(
    val x : Int,
    val y : Int,
    val terrain : Terrain
) {

    fun draw(batch: SpriteBatch){
        terrain.draw(batch, x, y)
    }

}
