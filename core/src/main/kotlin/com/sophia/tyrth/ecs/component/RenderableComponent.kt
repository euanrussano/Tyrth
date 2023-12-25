package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.mapperFor

class RenderableComponent : Component {

    // coordinates on the main atlas
    var x = 0
    var y = 0

    companion object {
        val ID = mapperFor<RenderableComponent>()
    }
}
