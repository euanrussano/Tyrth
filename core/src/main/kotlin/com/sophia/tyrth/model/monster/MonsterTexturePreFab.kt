package com.sophia.tyrth.model.monster

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.sophia.tyrth.model.monster.MonsterPreFab

class MonsterTexturePreFab(val textureRegion : TextureRegion) : MonsterPreFab {

    override fun draw(batch: SpriteBatch, x: Int, y: Int) {
        batch.draw(textureRegion, x.toFloat(), y.toFloat(), 1f, 1f)
    }
}
