package com.sophia.tyrth

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion


object Assets {
    lateinit var hero : TextureRegion
    lateinit var heroWithMelee: TextureRegion
    lateinit var heroWithShield: TextureRegion


    lateinit var downstairs: TextureRegion
    lateinit var floor : TextureRegion
    lateinit var wall : TextureRegion


    lateinit var target : TextureRegion
    lateinit var target2 : TextureRegion

    lateinit var tilesheet: Array<Array<TextureRegion>>
    val tiles = mutableMapOf<String, TextureRegion>()

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

        tiles["damage"] = tilesheet[11][26]
        tiles["heart"] = tilesheet[10][39]

        tiles["health potion"] = tilesheet[13][32]
        tiles["rations"] = tilesheet[13][33]
        tiles["map scroll"] = tilesheet[15][32]

        tiles["bear trap"] = tilesheet[2][23]

        tiles["dagger"] = tilesheet[6][34]
        tiles["shield"] = tilesheet[2][38]
        tiles["longsword"] = tilesheet[6][35]
        tiles["tower shield"] = tilesheet[3][38]


        tiles["scorpion"] = tilesheet[5][24]
        tiles["rat"] = tilesheet[8][31]

        target = tilesheet[14][25]
        target2 = tilesheet[14][19]

        wall = tilesheet[13][0]
        downstairs = tilesheet[10][10]

        hero = tilesheet[0][25]
        heroWithMelee = tilesheet[0][26]
        heroWithShield = tilesheet[0][27]

    }

}
