package com.sophia.tyrth

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion


object Assets {

    lateinit var tilesheet: Array<Array<TextureRegion>>
    lateinit var floor : TextureRegion

    fun load(){
        Pixmap(64, 64, Pixmap.Format.RGBA8888).apply {
            setColor(Color(Color.WHITE).apply { a = 0.5f })
            fillCircle(32,32, 4)
            floor = TextureRegion(Texture(this))
            dispose()
        }


        tilesheet = TextureRegion(Texture("tilesheet.png")
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
            .split(16,16)
    }

}
