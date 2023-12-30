package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.ecs.component.FieldOfViewComponent
import com.sophia.tyrth.ecs.component.HeroComponent
import com.sophia.tyrth.ecs.component.PositionComponent
import ktx.ashley.allOf

class CameraFollowHeroSystem(val viewport: Viewport) : EntitySystem() {

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).firstOrNull() ?: return
        val position = PositionComponent.ID[hero]

        viewport.camera.position.set(position.x.toFloat(), position.y.toFloat(), 0f)
    }

}
