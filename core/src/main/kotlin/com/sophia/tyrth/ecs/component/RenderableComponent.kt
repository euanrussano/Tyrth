package com.sophia.tyrth.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.mapperFor

class RenderableComponent : Component {

    companion object {
        val ID = mapperFor<RenderableComponent>()
    }
}
