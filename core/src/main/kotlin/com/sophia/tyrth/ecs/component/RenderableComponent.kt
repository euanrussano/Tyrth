package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.mapperFor

class RenderableComponent : Component {

    //name of the sprite to be retrieved by Assets
    var name = "<No Name>"

    companion object {
        val ID = mapperFor<RenderableComponent>()
    }
}
