package com.sophia.tyrth.model.terrain

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TerrainTexturePreFab(val textureRegion : TextureRegion) : TerrainPreFab {

    override fun draw(batch: SpriteBatch, x: Int, y: Int) {
        batch.draw(textureRegion, x.toFloat(), y.toFloat(), 1f, 1f)
    }
}
