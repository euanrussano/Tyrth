package com.sophia.tyrth

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion


object Assets {

    private lateinit var tilesheet: Array<Array<TextureRegion>>
    val tiles = mutableMapOf<String, TextureRegion>()

    fun load(){
        val floor : TextureRegion
        Pixmap(64, 64, Pixmap.Format.RGBA8888).apply {
            setColor(Color(Color.WHITE).apply { a = 0.5f })
            fillCircle(32,32, 4)
            floor = TextureRegion(Texture(this))
            dispose()
        }


        tilesheet = TextureRegion(Texture("tilesheet.png")
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
            .split(16,16)


        tiles["floor"] = floor
        tiles["hero"] = tilesheet[0][25]
        tiles["health_potion"] = tilesheet[11][42]
        tiles["scorpion"] = tilesheet[5][24]
        tiles["rat"] = tilesheet[8][31]
        tiles["wall"] = tilesheet[13][0]
        tiles["target"] = tilesheet[14][25]
        tiles["target2"] = tilesheet[14][19]

    }

}
