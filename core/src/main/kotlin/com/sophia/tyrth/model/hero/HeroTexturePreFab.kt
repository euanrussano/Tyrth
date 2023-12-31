package com.sophia.tyrth.model.hero

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class HeroTexturePreFab(val textureRegion : TextureRegion) : HeroPreFab {

    override fun draw(batch: SpriteBatch, x: Int, y: Int) {
        batch.draw(textureRegion, x.toFloat(), y.toFloat(), 1f, 1f)
    }
}
