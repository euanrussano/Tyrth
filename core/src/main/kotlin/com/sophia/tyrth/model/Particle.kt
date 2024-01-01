package com.sophia.tyrth.model

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Particle(
    val position : Pair<Int, Int>,
    var lifeTimeMs : Int = 200,
    var textureRegion : TextureRegion
) {

    fun update(delta : Float){
        lifeTimeMs -= (delta*1000).toInt()
    }

}
