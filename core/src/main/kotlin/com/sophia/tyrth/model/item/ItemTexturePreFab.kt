package com.sophia.tyrth.model.item

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class ItemTexturePreFab(val textureRegion : TextureRegion) : ItemPreFab {

    override fun draw(batch: SpriteBatch, x: Int, y: Int) {
        batch.draw(textureRegion, x.toFloat(), y.toFloat(), 1f, 1f)
    }
}
